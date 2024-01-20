package br.com.pawsoncloud.dtos;

import java.time.LocalDate;

import br.com.pawsoncloud.entidades.Doacao;

/**
 * Representa um objeto de transferência de dados (DTO) para informações de doação.
 * Este DTO é usado para fornecer uma versão simplificada de uma doação para a resposta da API.
 * 
 * @author Edielson Assis
 */
public record DoacaoRespDto(Long id, LocalDate dataCadastramento, AnimaisResponseDto pet,
        boolean confirmarDoacao) {

    /**
     * Construtor que converte uma instância de Doacao para DoacaoRespDto.
     *
     * @param doacao A instância de Doacao a ser convertida.
     */
    public DoacaoRespDto(Doacao doacao) {
        this(doacao.getId(), doacao.getDataCadastramento(), new AnimaisResponseDto(doacao.getPet()),
                doacao.isConfirmarDoacao());
    }
}