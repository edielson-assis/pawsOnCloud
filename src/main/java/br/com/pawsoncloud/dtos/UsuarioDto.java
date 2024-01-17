package br.com.pawsoncloud.dtos;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioDto(

        @NotBlank(message = "{nome.obrigatorio}") 
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "{campo.letras}")
        @Size(min = 3, message = "'${validatedValue}' precisa ter, pelo menos, {min} caracteres.")
        String nome,

        @NotBlank(message = "{email.obrigatorio}") 
        @Email(message = "{email.invalido}") 
        String email,

        @NotBlank(message = "{senha.obrigatorio}")
        String senha,

        @NotNull(message = "{data.obrigatorio}")
        @Past(message = "Data '${validatedValue}' é inválida!")
        LocalDate dataNascimento,

        @NotBlank(message = "{cpf.obrigatorio}")
        @CPF(message = "'${validatedValue}' é inválido!")
        String cpf,

        @NotBlank(message = "{telefone.obrigatorio}")
        @Pattern(regexp = "^(\\+\\d{1,2}\\s?)?(\\(\\d{2}\\)\\s?)?(\\d{4,5}-?\\d{4})$", message = "{campo.numeros}")
        String telefone,

        @Valid
        EnderecoDto endereco) {
}