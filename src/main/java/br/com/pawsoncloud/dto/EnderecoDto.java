package br.com.pawsoncloud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoDto(

        @NotBlank(message = "{campo.obrigatorio}") 
        String logradouro,
        String complemento,

        @NotBlank(message = "{campo.obrigatorio}")
        @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "{campo.letras}")
        String cidade,

        @NotBlank(message = "{campo.obrigatorio}")
        @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "{campo.letras}")
        @Size(max = 2, message = "'${validatedValue}' precisa ter, no máximo, {max} caracteres.")
        String estado) {
}