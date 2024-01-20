package br.com.pawsoncloud.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Record que representa o dto de autenticação do usuário. É utilizado nas requisições do tipo request.
 * 
 * @author Edielson Assis
 */
public record UsuarioAutenticado(

    @NotBlank(message = "{email.obrigatorio}") 
    @Email(message = "{email.invalido}") 
    String email, 

    @NotBlank(message = "{senha.obrigatorio}") 
    String senha) {
}