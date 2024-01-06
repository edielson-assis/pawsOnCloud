package br.com.pawsoncloud.dto;

import br.com.pawsoncloud.entidades.Endereco;

public record EnderecoRespDto(Long id, String cidade, String estado) {
    
    public EnderecoRespDto(Endereco endereco) {
        this(endereco.getId(), endereco.getCidade(), endereco.getEstado());
    }
}