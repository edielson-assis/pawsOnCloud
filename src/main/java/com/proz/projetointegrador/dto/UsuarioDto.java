package com.proz.projetointegrador.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import com.proz.projetointegrador.entidades.Endereco;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record UsuarioDto(

        @NotBlank(message = "{nome.obrigatorio}") 
        @Size(min = 3, message = "'${validatedValue}' precisa ser ter, pelo menos, {min} caracteres.")
        String nome,

        @NotBlank(message = "{email.obrigatorio}") 
        @Email(message = "{email.invalido}") 
        String email,

        @NotBlank(message = "{password.obrigatorio}")
        String senha,

        @NotNull(message = "{data.obrigatorio}")
        @Past(message = "Data '${validatedValue}' é inválida!")
        LocalDate dataDeNascimento,

        @NotBlank(message = "{cpf.obrigatorio}")
        @CPF(message = "'${validatedValue}' é inválido!")
        String cpf,

        @NotBlank(message = "{telefone.obrigatorio}")
        String telefone,

        @Valid
        Endereco endereco) {

}