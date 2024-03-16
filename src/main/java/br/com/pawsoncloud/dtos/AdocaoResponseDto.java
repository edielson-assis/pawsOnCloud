package br.com.pawsoncloud.dtos;

import br.com.pawsoncloud.entidades.Adocao;

/**
 * Representa um objeto de transferência de dados (DTO) para informações de adoção.
 * Este DTO é usado para fornecer uma versão simplificada de uma adoção para a resposta da API.
 * 
 * @author Edielson Assis
 */
public record AdocaoResponseDto(Long id, AnimaisResponseDto pet, UsuarioResponseDto adotante, boolean confirmarAdocao) {

    /**
     * Construtor que converte uma instância de Adocao para AdocaoResponseDto.
     *
     * @param adocao A instância de Adocao a ser convertida.
     */
    public AdocaoResponseDto(Adocao adocao) {
        this(adocao.getId(), new AnimaisResponseDto(adocao.getPet()), new UsuarioResponseDto(adocao.getAdotante()),
                adocao.isConfirmarAdocao());
    }
}