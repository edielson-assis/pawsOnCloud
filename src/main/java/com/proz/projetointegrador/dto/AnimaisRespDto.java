package com.proz.projetointegrador.dto;

import com.proz.projetointegrador.entidades.Animais;
import com.proz.projetointegrador.entidades.enums.StatusAdocao;

public record AnimaisRespDto(Long id, String nome, String porte, Integer idade, String especie, String pelagem,
        Double peso, String imgUrl, StatusAdocao status, UsuarioRespDto usuario) {

    public AnimaisRespDto(Animais animais) {
        this(animais.getId(), animais.getNome(), animais.getPorte(), animais.getIdade(), animais.getEspecie(),
                animais.getPelagem(), animais.getPeso(), animais.getImgUrl(), animais.getStatus(),
                new UsuarioRespDto(animais.getUsuario()));
    }
}