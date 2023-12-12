package com.proz.projetointegrador.dto;

import jakarta.validation.constraints.NotBlank;

public record EnderecoDto(

        @NotBlank(message = "{campo.obrigatorio}") 
        String logradouro,
        String complemento,

        @NotBlank(message = "{campo.obrigatorio}")
        String cidade,

        @NotBlank(message = "{campo.obrigatorio}")
        String estado) {

}