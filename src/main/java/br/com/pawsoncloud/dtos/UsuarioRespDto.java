package br.com.pawsoncloud.dtos;

import br.com.pawsoncloud.entidades.Usuario;

/**
 * Representa um objeto de transferência de dados (DTO) para informações resumidas de usuário.
 * Este DTO é usado para fornecer uma versão simplificada das informações do usuário para a resposta da API.
 * Retorna o nome e email do usuário, a cidade e o estado.
 * 
 * @author Edielson Assis
 */
public record UsuarioRespDto(String nome, String email, EnderecoRespDto endereco) {
    
    /**
     * Construtor que converte uma instância de Usuario para UsuarioRespDto.
     *
     * @param usuario A instância de Usuario a ser convertida.
     */
    public UsuarioRespDto(Usuario usuario) {
        this(usuario.getNome(), usuario.getEmail(), new EnderecoRespDto(usuario.getEndereco()));
    }
}