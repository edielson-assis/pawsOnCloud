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

@Service
@AllArgsConstructor
public class UsuarioRegistroServicoImpl implements UsuarioRegistroServico {

    private final UsuarioRepositorio repositorio;
    private final TokenEmailServico tokenEmailServico;
    private final EmailServico emailServico;

    @Override
    public Usuario create(UsuarioDto usuarioDto) {
        Usuario usuario = DadosUsuario.getUsuario(usuarioDto);
        existsByEmail(usuario);
        existsByCpf(usuario);
        enviarToken(usuario);
        return repositorio.save(usuario);
    }

    @Override
    public Usuario findByCpf() {
        Optional<Usuario> usuario = repositorio.findByCpf(UsuarioLogado.getUsuario().getCpf());
        return usuario.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
    }

    @Override
    public Usuario update(UsuarioUpdateDto usuarioUpdateDto) {
        Usuario usuario = findByCpf();
        DadosUsuario.getUsuarioAtualizado(usuario, usuarioUpdateDto);
        return repositorio.save(usuario);
    }

    @Override
    public void delete() {
        Usuario usuario = findByCpf();
        try {
            repositorio.deleteById(usuario.getId());
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

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

    private synchronized void existsByEmail(Usuario usuario) {
        boolean existeEmail = repositorio.existsByEmail(usuario.getEmail());        
        if (existeEmail) {          
            throw new ValidationException("Email já cadastrado");
        } 
    }

    private synchronized void existsByCpf(Usuario usuario) {
        boolean existeCpf = repositorio.existsByCpf(usuario.getCpf());
        if (existeCpf) {
            throw new ValidationException("CPF já cadastrado");
        }
    }

    private String enviarToken(Usuario usuario) {
        String token = gerarToken(usuario);

        String link = "http://localhost:8080/api/v1/usuario/confirmar?token=" + token;
        emailServico.enviar(usuario.getEmail(), Email.construirEmail(usuario.getNome(), link));
        return token;
    }

    private String gerarToken(Usuario usuario) {
        String token = BCrypt.hashpw(UUID.randomUUID().toString(), BCrypt.gensalt());

        TokenEmail tokenEmail = new TokenEmail(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), usuario);
        tokenEmailServico.createToken(tokenEmail);
        return token;
    }

    private int ativarUsuario(String email) {
        return repositorio.ativarUsuario(email);
    }
}