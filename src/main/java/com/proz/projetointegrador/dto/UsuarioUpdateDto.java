package com.proz.projetointegrador.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateDto(

    @NotNull(message = "{campo.obrigatorio}")
    Long id,

    @NotBlank(message = "{nome.obrigatorio}") 
    @Size(min = 3, message = "'${validatedValue}' precisa ser ter, pelo menos, {min} caracteres.")
    String nome,

    @NotBlank(message = "{email.obrigatorio}") 
    @Email(message = "{email.invalido}") 
    String email,

    @NotBlank(message = "{password.obrigatorio}")
    String senha,

    @NotBlank(message = "{telefone.obrigatorio}")
    String telefone,

    @Valid
    EnderecoUpdateDto enderecoUpdateDto) {
}