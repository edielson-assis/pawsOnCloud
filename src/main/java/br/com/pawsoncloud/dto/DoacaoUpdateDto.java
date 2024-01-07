package br.com.pawsoncloud.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DoacaoUpdateDto(

    @NotNull
    Long id,

    @Valid
    AnimaisUpdateDto pet,
    
    boolean confirmarDoacao) {
    
}