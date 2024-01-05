package com.proz.projetointegrador.servicos.conversor;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.proz.projetointegrador.dto.UsuarioDto;
import com.proz.projetointegrador.dto.UsuarioUpdateDto;
import com.proz.projetointegrador.entidades.NivelAcesso;
import com.proz.projetointegrador.entidades.Usuario;

public class DadosUsuario {
    
    private static Usuario fromDto(UsuarioDto usuarioDto) {
        return new Usuario(null, usuarioDto.nome(),
        usuarioDto.email(),
        BCrypt.hashpw(usuarioDto.senha(), BCrypt.gensalt()),
        usuarioDto.dataNascimento(),
        usuarioDto.cpf(),
        usuarioDto.telefone(),
        DadosEndereco.getEndereco(usuarioDto.endereco()),
        NivelAcesso.getInstance());
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