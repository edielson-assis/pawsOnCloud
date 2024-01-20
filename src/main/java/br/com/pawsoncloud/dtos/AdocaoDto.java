package br.com.pawsoncloud.dtos;

import jakarta.validation.constraints.NotNull;

/**
 * Representa um objeto de transferência de dados (DTO) para informações de adoção.
 * Este DTO é usado para validar e transportar dados de adoção para a API.
 * 
 * @author Edielson Assis
 */
public record AdocaoDto(@NotNull Long petId) {}