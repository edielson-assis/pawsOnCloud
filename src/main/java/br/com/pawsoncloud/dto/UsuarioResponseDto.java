package br.com.pawsoncloud.dto;

import br.com.pawsoncloud.entidades.Usuario;

public record UsuarioResponseDto(String nome, EnderecoRespDto endereco) {
    
    public UsuarioResponseDto(Usuario usuario) {
        this(usuario.getNome(), new EnderecoRespDto(usuario.getEndereco()));
    }
}