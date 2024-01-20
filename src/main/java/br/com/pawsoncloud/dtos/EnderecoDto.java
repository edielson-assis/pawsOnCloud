package br.com.pawsoncloud.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Representa um objeto de transferência de dados (DTO) para informações de endereço.
 * Este DTO é usado para validar e transportar dados de endereço para a API.
 * 
 * @author Edielson Assis
 */
public record EnderecoDto(

        @NotBlank(message = "{campo.obrigatorio}") 
        String logradouro,
        String complemento,

        @NotBlank(message = "{campo.obrigatorio}")
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "{campo.letras}")
        String cidade,

        @NotBlank(message = "{campo.obrigatorio}")
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "{campo.letras}")
        @Size(max = 2, message = "'${validatedValue}' precisa ter, no máximo, {max} caracteres.")
        String estado) {
}