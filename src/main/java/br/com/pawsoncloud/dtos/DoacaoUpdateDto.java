package br.com.pawsoncloud.dtos;

import jakarta.validation.constraints.NotNull;

/**
 * Representa um objeto de transferência de dados (DTO) para informações de doação.
 * Este DTO é usado para validar e transportar dados de doação para a API.
 * 
 * @author Edielson Assis
 */
public record DoacaoUpdateDto(@NotNull Long id, boolean confirmarDoacao) {}