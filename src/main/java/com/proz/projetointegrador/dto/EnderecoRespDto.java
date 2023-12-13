package com.proz.projetointegrador.dto;

import com.proz.projetointegrador.entidades.Endereco;

public record EnderecoRespDto(Long id, String cidade, String estado) {
    
    public EnderecoRespDto(Endereco endereco) {
        this(endereco.getId(), endereco.getCidade(), endereco.getEstado());
    }
}