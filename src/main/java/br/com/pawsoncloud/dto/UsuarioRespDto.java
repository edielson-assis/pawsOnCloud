package br.com.pawsoncloud.dto;

import br.com.pawsoncloud.entidades.Usuario;

public record UsuarioRespDto(Long id, String nome, String email, EnderecoRespDto endereco) {
    
    public UsuarioRespDto(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), new EnderecoRespDto(usuario.getEndereco()));
    }
}