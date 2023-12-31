package com.proz.projetointegrador.dto;

import jakarta.validation.Valid;
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
    @Pattern(regexp = "^\\d+$", message = "{campo.numeros}")
    Integer idade,

    @NotBlank(message = "{campo.obrigatorio}")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "{campo.letras}")
    String especie,
    String pelagem,

    @NotNull(message = "{campo.obrigatorio}")
    @Pattern(regexp = "^\\d+(\\.\\d+)?$", message = "{campo.numeros}")
    Double peso,

    @NotBlank(message = "{campo.obrigatorio}")
    String imgUrl,

    @Valid
    UsuarioDto usuarioDto) {
    
}