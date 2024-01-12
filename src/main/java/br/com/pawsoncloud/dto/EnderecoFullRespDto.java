package br.com.pawsoncloud.dto;

import br.com.pawsoncloud.entidades.Endereco;

public record EnderecoFullRespDto(String Logradouro, String complemento, String cidade, String estado) {

    public EnderecoFullRespDto(Endereco endereco) {
        this(endereco.getLogradouro(), endereco.getComplemento(), endereco.getCidade(), endereco.getEstado());
    }
}