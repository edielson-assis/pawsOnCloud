package br.com.pawsoncloud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AnimaisUpdateDto(

    @NotNull(message = "{campo.obrigatorio}")
    Long id,

    @NotBlank(message = "{campo.obrigatorio}")
    String nome,

    @NotBlank(message = "{campo.obrigatorio}")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "{campo.letras}")
    String porte,

    @NotNull(message = "{campo.obrigatorio}")
    Integer idade,

    @NotBlank(message = "{campo.obrigatorio}")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "{campo.letras}")
    String especie,
    String pelagem,

    @NotNull(message = "{campo.obrigatorio}")
    Double peso,

    @NotBlank(message = "{campo.obrigatorio}")
    String imgUrl,
    
    boolean adotado) {
}