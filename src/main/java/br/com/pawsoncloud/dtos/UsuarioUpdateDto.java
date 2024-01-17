package br.com.pawsoncloud.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateDto(

    @NotBlank(message = "{nome.obrigatorio}") 
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "{campo.letras}")
    @Size(min = 3, message = "'${validatedValue}' precisa ter, pelo menos, {min} caracteres.")
    String nome,

    @NotBlank(message = "{senha.obrigatorio}")
    String senha,

    @NotBlank(message = "{telefone.obrigatorio}")
    @Pattern(regexp = "^(\\+\\d{1,2}\\s?)?(\\(\\d{2}\\)\\s?)?(\\d{4,5}-?\\d{4})$", message = "{campo.numeros}")
    String telefone,

    @Valid
    EnderecoDto endereco) {
}