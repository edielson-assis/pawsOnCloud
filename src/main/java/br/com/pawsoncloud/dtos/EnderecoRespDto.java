package br.com.pawsoncloud.dtos;

import br.com.pawsoncloud.entidades.Endereco;

/**
 * Representa um objeto de transferência de dados (DTO) para informações completas de endereço.
 * Este DTO é usado para fornecer uma versão simplificada de um endereço para a resposta da API.
 * Retorna apenas a cidade e o estado.
 * 
 * @author Edielson Assis
 */
public record EnderecoRespDto(String cidade, String estado) {
    
    /**
     * Construtor que converte uma instância de Endereco para EnderecoRespDto.
     *
     * @param endereco A instância de Endereco a ser convertida.
     */
    public EnderecoRespDto(Endereco endereco) {
        this(endereco.getCidade(), endereco.getEstado());
    }
}