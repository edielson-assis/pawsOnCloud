package br.com.pawsoncloud.servicos.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import br.com.pawsoncloud.dtos.UsuarioDto;
import br.com.pawsoncloud.dtos.UsuarioUpdateDto;
import br.com.pawsoncloud.email.Email;
import br.com.pawsoncloud.entidades.TokenEmail;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.repositorios.UsuarioRepositorio;
import br.com.pawsoncloud.servicos.EmailServico;
import br.com.pawsoncloud.servicos.TokenEmailServico;
import br.com.pawsoncloud.servicos.UsuarioRegistroServico;
import br.com.pawsoncloud.servicos.conversor.DadosUsuario;
import br.com.pawsoncloud.servicos.excecoes.DataBaseException;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;

/**
 * Classe que implementa a interface <b>UsuarioRegistroServico</b>.
 * 
 * @author Edielson Assis
 */
@Service
@AllArgsConstructor
public class UsuarioRegistroServicoImpl implements UsuarioRegistroServico {

    private final UsuarioRepositorio repositorio;
    private final TokenEmailServico tokenEmailServico;
    private final EmailServico emailServico;

    /**
     * Verifica se o email ou cpf informado ja está cadastrado. 
     * Se sim, é retornado uma mensagem informando que o email ou cpf já está cadastrado. 
     * Se não, é enviado um token para o email informado.
     * 
     * @param usuarioDto usuário que será criado.
     * @return Usuário recém-criado.
     */
    @Override
    public Usuario create(UsuarioDto usuarioDto) {
        Usuario usuario = DadosUsuario.getUsuario(usuarioDto);
        existsByEmail(usuario);
        existsByCpf(usuario);
        enviarToken(usuario);
        return repositorio.save(usuario);
    }

    /**
     * Verifica, com base no cpf, se o usuário existe no banco de dados.
     * Se sim, os dados são retornados. Se não, é lançado uma exceção.
     * 
     * @exception ObjectNotFoundException é lançada caso o usuário não seja encotrado no banco de dados.
     * @return Usuário
     */
    @Override
    public Usuario findByCpf() {
        Optional<Usuario> usuario = repositorio.findByCpf(UsuarioLogado.getUsuario().getCpf());
        return usuario.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
    }

    /**
     * Verifica, com base no cpf, se o usuário existe no banco de dados. 
     * Se sim, os dados podem ser atualizados. Se não, é lançado uma exceção.
     * 
     * @param usuarioUpdateDto usuário que será atualizado.
     * @exception ObjectNotFoundException é lançada caso o usuário não seja encotrado no banco de dados.
     * @return Usuário recém-atualizado.
     */
    @Override
    public Usuario update(UsuarioUpdateDto usuarioUpdateDto) {
        Usuario usuario = findByCpf();
        DadosUsuario.getUsuarioAtualizado(usuario, usuarioUpdateDto);
        return repositorio.save(usuario);
    }

    /**
     * Verifica, com base no cpf, se o usuário existe no banco de dados.
     * Se sim, o usuário pode ser deletado. Se não, é lançado uma exceção.
     * 
     * @exception ObjectNotFoundException é lançada caso o usuário não seja encotrado no banco de dados.
     */
    @Override
    public void delete() {
        Usuario usuario = findByCpf();
        try {
            repositorio.deleteById(usuario.getId());
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    /**
     * Através do método findByToken, da classe {@link TokenEmailServico}, é verificado se o token exixste no banco de dadods.
     * Se sim, ele é atribuido à um objeto do tipo {@link TokenEmail}, se não, é lançado uma exceção.
     * Por meio do método confirmarToken, é verificado se o email já foi confirmado, se o token está expirado.
     * Caso a resposta seja sim, será lançado uma exceção. Se não, o token é validado e o usuário ativado.
     * 
     * @param token token de validação.
     * @exception IllegalStateException é lançada caso o token não seja encontrado.
     * @return uma mensagem de confirmação, caso o token seja válido.
     */
    @Transactional
    @Override
    public String confirmarToken(String token) {
        TokenEmail tokenEmail = tokenEmailServico.findByToken(token).orElseThrow(() -> new IllegalStateException("Token não encontrado"));

        if (tokenEmail.getConfirmadoAs() != null) {
            throw new IllegalStateException("Email já confirmado");
        }

        LocalDateTime expiradoAs = tokenEmail.getExpiradoAs();

        if (expiradoAs.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expirado");
        }

        tokenEmailServico.setConfirmadoAs(token);
        ativarUsuario(tokenEmail.getUsuario().getEmail());

        return "Email validado com sucesso";
    }

    /**
     * Verifica se o email do usuário já está cadastrado.
     * 
     * @param usuario utilizado para pegar o email do usuário.
     * @exception ValidationException é lançada caso o email já esteja cadastrado.
     */
    private synchronized void existsByEmail(Usuario usuario) {
        boolean existeEmail = repositorio.existsByEmail(usuario.getEmail());        
        if (existeEmail) {          
            throw new ValidationException("Email já cadastrado");
        } 
    }

    /**
     * Verifica se o cpf do usuário já está cadastrado.
     * 
     * @param usuario utilizado para pegar o cpf do usuário.
     * @exception ValidationException é lançada caso o cpf já esteja cadastrado.
     */
    private synchronized void existsByCpf(Usuario usuario) {
        boolean existeCpf = repositorio.existsByCpf(usuario.getCpf());
        if (existeCpf) {
            throw new ValidationException("CPF já cadastrado");
        }
    }

    /**
     * Chama internamente os métodos gerarToken, construirEmail e enviar. Por fim, envia a mensagem de validação para o email cadastrado.
     * 
     * @param usuario utilizado para pegar o nome e email do usuário.
     * @return token
     */
    private String enviarToken(Usuario usuario) {
        String token = gerarToken(usuario);

        String[] nome = usuario.getNome().split(" ");

        String link = "http://localhost:8080/api/v1/usuario/confirmar?token=" + token;
        emailServico.enviar(usuario.getEmail(), Email.construirEmail(nome[0], link));
        return token;
    }

    /**
     * Constroi o token de validação e o salva no banco de dados.
     * 
     * @param usuario pessoa para qual o token será construido.
     * @return token
     */
    private String gerarToken(Usuario usuario) {
        String token = BCrypt.hashpw(UUID.randomUUID().toString(), BCrypt.gensalt());

        TokenEmail tokenEmail = new TokenEmail(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), usuario);
        tokenEmailServico.createToken(tokenEmail);
        return token;
    }

    /**
     * Ativa o usuário, se o token tiver sido validado.
     * 
     * @param email email do usuário que será ativado.
     * @return O número de registro afetado.
     */
    private int ativarUsuario(String email) {
        return repositorio.ativarUsuario(email);
    }
}