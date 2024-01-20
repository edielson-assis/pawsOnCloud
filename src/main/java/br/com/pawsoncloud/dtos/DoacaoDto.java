package br.com.pawsoncloud.dtos;

import jakarta.validation.Valid;

/**
 * Representa um objeto de transferência de dados (DTO) para informações de doação.
 * Este DTO é usado para validar e transportar dados de doação para a API.
 * 
 * @author Edielson Assis
 */
public record DoacaoDto(@Valid AnimaisDto pet) {}