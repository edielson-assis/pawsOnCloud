package br.com.pawsoncloud.dtos;

import java.time.LocalDate;

import br.com.pawsoncloud.entidades.Usuario;

public record UsuarioFullRespDto(String nome, String email, LocalDate dataNascimento, String cpf, String telefone,
        EnderecoFullRespDto endereco) {

    public UsuarioFullRespDto(Usuario usuario) {
        this(usuario.getNome(), usuario.getEmail(), usuario.getDataNascimento(), usuario.getCpf(),
                usuario.getTelefone(), new EnderecoFullRespDto(usuario.getEndereco()));
    }
}