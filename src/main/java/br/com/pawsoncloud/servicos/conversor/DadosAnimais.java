package br.com.pawsoncloud.servicos.conversor;

import br.com.pawsoncloud.dto.AnimaisDto;
import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.entidades.enums.StatusAdocao;
import br.com.pawsoncloud.servicos.impl.UsuarioLogado;

public class DadosAnimais {
    
    private static Animais fromDto(AnimaisDto animaisDto) {
        return new Animais(null, animaisDto.nome(),
        animaisDto.porte(),
        animaisDto.idade(),
        animaisDto.especie(),
        animaisDto.pelagem(),
        animaisDto.peso(),
        animaisDto.imgUrl(),
        StatusAdocao.DISPONIVEL,
        UsuarioLogado.getUsuario(),
        false);
    }

    public static Animais getAnimais(AnimaisDto animaisDto) {
        return fromDto(animaisDto);
    }
}