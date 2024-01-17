package br.com.pawsoncloud.dtos;

import br.com.pawsoncloud.entidades.Endereco;

public record EnderecoRespDto(String cidade, String estado) {
    
    public EnderecoRespDto(Endereco endereco) {
        this(endereco.getCidade(), endereco.getEstado());
    }
}