package br.com.pawsoncloud.dto;

import jakarta.validation.Valid;

public record DoacaoDto(@Valid AnimaisDto pet, boolean confirmarDoacao) {}