package br.com.pawsoncloud.dtos;

import br.com.pawsoncloud.entidades.Usuario;

public record UsuarioRespDto(String nome, String email, EnderecoRespDto endereco) {
    
    public UsuarioRespDto(Usuario usuario) {
        this(usuario.getNome(), usuario.getEmail(), new EnderecoRespDto(usuario.getEndereco()));
    }
}