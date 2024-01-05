package com.proz.projetointegrador.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioAutenticado(

    @NotBlank(message = "{email.obrigatorio}") 
    @Email(message = "{email.invalido}") 
    String email, 

    @NotBlank(message = "{senha.obrigatorio}") 
    String senha) {
}