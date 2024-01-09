package br.com.pawsoncloud.dto;

import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.entidades.enums.StatusAdocao;

public record AnimaisResponseDto(Long id, String nome, String porte, Integer idade, String especie, String pelagem,
        Double peso, String imgUrl, StatusAdocao status, UsuarioResponseDto usuario) {

    public AnimaisResponseDto(Animais animais) {
        this(animais.getId(), animais.getNome(), animais.getPorte(), animais.getIdade(), animais.getEspecie(),
                animais.getPelagem(), animais.getPeso(), animais.getImgUrl(), animais.getStatus(),
                new UsuarioResponseDto(animais.getUsuario()));
    }
}