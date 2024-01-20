package br.com.pawsoncloud.servicos.conversor;

import org.springframework.security.crypto.bcrypt.BCrypt;

import br.com.pawsoncloud.dtos.UsuarioDto;
import br.com.pawsoncloud.dtos.UsuarioUpdateDto;
import br.com.pawsoncloud.entidades.NivelAcesso;
import br.com.pawsoncloud.entidades.Usuario;

/**
 * Classe responsável por realizar a conversão dos DTOs de usuário.
 * 
 * @author Edielson Assis
 */
public class DadosUsuario {
    
    /** 
     * Cria um usuário e o mapeia com os dados do dto.
     * 
     * @param usuarioDto contém os dados do usuário que será criado.
     * @return Usuario
     */
    public static Usuario getUsuario(UsuarioDto usuarioDto) {
        return new Usuario(null, usuarioDto.nome(),
        usuarioDto.email(),
        BCrypt.hashpw(usuarioDto.senha(), BCrypt.gensalt()),
        usuarioDto.dataNascimento(),
        usuarioDto.cpf(),
        usuarioDto.telefone(),
        DadosEndereco.getEndereco(usuarioDto.endereco()),
        NivelAcesso.getInstance(),
        false);
    }

    /**
     * Atualiza os dados do objeto.
     * 
     * @param usuario objeto que será atualizado.
     * @param usuarioUpdateDto contém os dados de atualização do usuário.
     */
    public static void getUsuarioAtualizado(Usuario usuario, UsuarioUpdateDto usuarioUpdateDto) {
        usuario.setNome(usuarioUpdateDto.nome());
        usuario.setSenha(BCrypt.hashpw(usuarioUpdateDto.senha(), BCrypt.gensalt()));
        usuario.setTelefone(usuarioUpdateDto.telefone());
        usuario.setEndereco(DadosEndereco.getEnderecoAtualizado(usuario.getEndereco(), usuarioUpdateDto.endereco()));     
    }
}