package br.com.pawsoncloud.dtos;

import br.com.pawsoncloud.entidades.Endereco;

/**
 * Representa um objeto de transferência de dados (DTO) para informações completas de endereço.
 * Este DTO é usado para fornecer uma versão simplificada de um endereço para a resposta da API.
 * Retorna todos os dados do endereço.
 * 
 * @author Edielson Assis
 */
public record EnderecoFullRespDto(String Logradouro, String complemento, String cidade, String estado) {

    /**
     * Construtor que converte uma instância de Endereco para EnderecoFullRespDto.
     *
     * @param endereco A instância de Endereco a ser convertida.
     */
    public EnderecoFullRespDto(Endereco endereco) {
        this(endereco.getLogradouro(), endereco.getComplemento(), endereco.getCidade(), endereco.getEstado());
    }
}