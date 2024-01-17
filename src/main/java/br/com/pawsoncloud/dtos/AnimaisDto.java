package br.com.pawsoncloud.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AnimaisDto(

    @NotBlank(message = "{campo.obrigatorio}")
    String nome,

    @NotBlank(message = "{campo.obrigatorio}")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "{campo.letras}")
    String porte,

    @NotNull(message = "{campo.obrigatorio}")
    Integer idade,

    @NotBlank(message = "{campo.obrigatorio}")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "{campo.letras}")
    String especie,
    String pelagem,

    @NotNull(message = "{campo.obrigatorio}")
    Double peso,

    @NotBlank(message = "{campo.obrigatorio}")
    String imgUrl) {    
}