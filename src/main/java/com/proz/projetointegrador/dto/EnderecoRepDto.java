package com.proz.projetointegrador.dto;

import com.proz.projetointegrador.entidades.Endereco;

public record EnderecoRepDto(Long id, String logradouro, String complemento, String cidade, String estado) {
    
    public EnderecoRepDto(Endereco endereco) {
        this(endereco.getId(), endereco.getLogradouro(), endereco.getComplemento(), endereco.getCidade(), endereco.getEstado());
    }
}