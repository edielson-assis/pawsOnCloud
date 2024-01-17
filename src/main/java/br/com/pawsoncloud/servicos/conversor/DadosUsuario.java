package br.com.pawsoncloud.servicos.conversor;

import org.springframework.security.crypto.bcrypt.BCrypt;

import br.com.pawsoncloud.dtos.UsuarioDto;
import br.com.pawsoncloud.dtos.UsuarioUpdateDto;
import br.com.pawsoncloud.entidades.NivelAcesso;
import br.com.pawsoncloud.entidades.Usuario;

public class DadosUsuario {
    
    private static Usuario fromDto(UsuarioDto usuarioDto) {
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

    public static Usuario getUsuario(UsuarioDto usuarioDto) {
        return fromDto(usuarioDto);
    }

    private static void updateData(Usuario usuario, UsuarioUpdateDto usuarioUpdateDto) {
        usuario.setNome(usuarioUpdateDto.nome());
        usuario.setSenha(BCrypt.hashpw(usuarioUpdateDto.senha(), BCrypt.gensalt()));
        usuario.setTelefone(usuarioUpdateDto.telefone());
        usuario.setEndereco(DadosEndereco.getEnderecoAtualizado(usuario.getEndereco(), usuarioUpdateDto.endereco()));     
    }

    public static void getUsuarioAtualizado(Usuario usuario, UsuarioUpdateDto usuarioUpdateDto) {
        updateData(usuario, usuarioUpdateDto);
    }
}