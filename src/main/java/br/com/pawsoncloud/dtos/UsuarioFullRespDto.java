package br.com.pawsoncloud.dtos;

import java.time.LocalDate;

import br.com.pawsoncloud.entidades.Usuario;

/**
 * Representa um objeto de transferência de dados (DTO) para informações completas de usuário.
 * Este DTO é usado para fornecer uma versão simplificada das informações do usuário para a resposta da API.
 * Retorna todos os dados do usuário e do endereço.
 * 
 * @author Edielson Assis
 */
public record UsuarioFullRespDto(String nome, String email, LocalDate dataNascimento, String cpf, String telefone,
        EnderecoFullRespDto endereco) {

    /**
     * Construtor que converte uma instância de Usuario para UsuarioFullRespDto.
     *
     * @param usuario A instância de Usuario a ser convertida.
     */
    public UsuarioFullRespDto(Usuario usuario) {
        this(usuario.getNome(), usuario.getEmail(), usuario.getDataNascimento(), usuario.getCpf(),
                usuario.getTelefone(), new EnderecoFullRespDto(usuario.getEndereco()));
    }
}