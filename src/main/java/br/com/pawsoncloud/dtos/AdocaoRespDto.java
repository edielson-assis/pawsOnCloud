package br.com.pawsoncloud.dtos;

import br.com.pawsoncloud.entidades.Adocao;

public record AdocaoRespDto(Long id, Long petId, UsuarioResponseDto adotante, boolean confirmarAdocao) {

    public AdocaoRespDto(Adocao adocao) {
        this(adocao.getId(), adocao.getPet().getId(), new UsuarioResponseDto(adocao.getAdotante()),
                adocao.isConfirmarAdocao());
    }
}