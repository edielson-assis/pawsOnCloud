package com.proz.projetointegrador.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnderecoUpdateDto(

        @NotNull(message = "{campo.obrigatorio}")
        Long id,

        @NotBlank(message = "{campo.obrigatorio}") 
        String logradouro,
        String complemento,

        @NotBlank(message = "{campo.obrigatorio}")
        String cidade,

        @NotBlank(message = "{campo.obrigatorio}")
        String estado) {
}