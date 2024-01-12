package br.com.pawsoncloud.dto;

import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.entidades.enums.StatusAdocao;

public record AnimaisRespDto(Long id, String nome, String porte, Integer idade, String especie, String pelagem,
        Double peso, String imgUrl, StatusAdocao status, UsuarioRespDto proprietario) {

    public AnimaisRespDto(Animais animais) {
        this(animais.getId(), animais.getNome(), animais.getPorte(), animais.getIdade(), animais.getEspecie(),
                animais.getPelagem(), animais.getPeso(), animais.getImgUrl(), animais.getStatus(),
                new UsuarioRespDto(animais.getUsuario()));
    }
}