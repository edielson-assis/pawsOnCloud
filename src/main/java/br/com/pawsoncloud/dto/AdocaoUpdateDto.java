package br.com.pawsoncloud.dto;

import jakarta.validation.constraints.NotNull;

public record AdocaoUpdateDto(@NotNull Long id, boolean confirmarAdocao) {}