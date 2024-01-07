package br.com.pawsoncloud.dto;

import jakarta.validation.Valid;

public record AdocaoDto(@Valid Long petId, boolean confirmarAdocao) {}