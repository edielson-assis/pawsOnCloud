package br.com.pawsoncloud.dtos;

import jakarta.validation.constraints.NotNull;

public record DoacaoUpdateDto(@NotNull Long id, boolean confirmarDoacao) {}