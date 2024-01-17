package br.com.pawsoncloud.dtos;

import jakarta.validation.Valid;

public record DoacaoDto(@Valid AnimaisDto pet) {}