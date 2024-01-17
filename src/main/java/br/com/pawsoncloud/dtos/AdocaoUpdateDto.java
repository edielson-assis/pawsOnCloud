package br.com.pawsoncloud.dtos;

import jakarta.validation.constraints.NotNull;

public record AdocaoUpdateDto(@NotNull Long id, boolean confirmarAdocao) {}