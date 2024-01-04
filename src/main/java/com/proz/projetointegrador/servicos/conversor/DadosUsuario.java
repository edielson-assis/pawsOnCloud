package com.proz.projetointegrador.servicos.conversor;

import com.proz.projetointegrador.dto.UsuarioDto;
import com.proz.projetointegrador.dto.UsuarioUpdateDto;
import com.proz.projetointegrador.entidades.NivelAcesso;
import com.proz.projetointegrador.entidades.Usuario;

public class DadosUsuario {
    
    private static Usuario fromDto(UsuarioDto usuarioDto) {
        return new Usuario(null, usuarioDto.nome(),
        usuarioDto.email(),
        usuarioDto.senha(),
        usuarioDto.dataNascimento(),
        usuarioDto.cpf(),
        usuarioDto.telefone(),
        DadosEndereco.getEndereco(usuarioDto.enderecoDto()),
        NivelAcesso.getInstance());
    }

    public static Usuario getUsuario(UsuarioDto usuarioDto) {
        return fromDto(usuarioDto);
    }

    private static void updateData(Usuario usuario, UsuarioUpdateDto usuarioUpdateDto) {
        usuario.setNome(usuarioUpdateDto.nome());
        usuario.setSenha(usuarioUpdateDto.senha());
        usuario.setTelefone(usuarioUpdateDto.telefone());
        usuario.setEndereco(DadosEndereco.getEnderecoAtualizado(usuario.getEndereco(), usuarioUpdateDto.enderecoUpdateDto()));     
    }

    public static void getUsuarioAtualizado(Usuario usuario, UsuarioUpdateDto usuarioUpdateDto) {
        updateData(usuario, usuarioUpdateDto);
    }
}