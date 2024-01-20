package br.com.pawsoncloud.dtos;

import br.com.pawsoncloud.entidades.Adocao;

/**
 * Representa um objeto de transferência de dados (DTO) para informações de adoção.
 * Este DTO é usado para fornecer uma versão simplificada de uma adoção para a resposta da API.
 * 
 * @author Edielson Assis
 */
public record AdocaoRespDto(Long id, Long petId, UsuarioResponseDto adotante, boolean confirmarAdocao) {

    /**
     * Construtor que converte uma instância de Adocao para AdocaoRespDto.
     *
     * @param adocao A instância de Adocao a ser convertida.
     */
    public AdocaoRespDto(Adocao adocao) {
        this(adocao.getId(), adocao.getPet().getId(), new UsuarioResponseDto(adocao.getAdotante()),
                adocao.isConfirmarAdocao());
    }
}