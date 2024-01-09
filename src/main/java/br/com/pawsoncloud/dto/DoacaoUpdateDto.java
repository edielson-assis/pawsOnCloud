package br.com.pawsoncloud.dto;

import jakarta.validation.constraints.NotNull;

public record DoacaoUpdateDto(@NotNull Long id, boolean confirmarDoacao) {}