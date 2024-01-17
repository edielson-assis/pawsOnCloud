package br.com.pawsoncloud.dtos;

import java.time.LocalDate;

import br.com.pawsoncloud.entidades.Doacao;

public record DoacaoRespDto(Long id, LocalDate dataCadastramento, AnimaisResponseDto pet,
        boolean confirmarDoacao) {

    public DoacaoRespDto(Doacao doacao) {
        this(doacao.getId(), doacao.getDataCadastramento(), new AnimaisResponseDto(doacao.getPet()),
                doacao.isConfirmarDoacao());
    }
}