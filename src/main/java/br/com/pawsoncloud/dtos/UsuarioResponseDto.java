package br.com.pawsoncloud.dtos;

import br.com.pawsoncloud.entidades.Usuario;

/**
 * Representa um objeto de transferência de dados (DTO) para informações resumidas de usuário.
 * Este DTO é usado para fornecer uma versão simplificada das informações do usuário para a resposta da API.
 * Retorna o nome do usuário, a cidade e o estado.
 * 
 * @author Edielson Assis
 */
public record UsuarioResponseDto(String nome, EnderecoRespDto endereco) {
    
    /**
     * Construtor que converte uma instância de Usuario para UsuarioResponseDto.
     *
     * @param usuario A instância de Usuario a ser convertida.
     */
    public UsuarioResponseDto(Usuario usuario) {
        this(usuario.getNome(), new EnderecoRespDto(usuario.getEndereco()));
    }
}