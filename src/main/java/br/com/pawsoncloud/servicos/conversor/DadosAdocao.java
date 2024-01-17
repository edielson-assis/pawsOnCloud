package br.com.pawsoncloud.servicos.conversor;

import br.com.pawsoncloud.dtos.AdocaoDto;
import br.com.pawsoncloud.dtos.AdocaoUpdateDto;
import br.com.pawsoncloud.entidades.Adocao;
import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.servicos.impl.UsuarioLogado;

public class DadosAdocao {
    
    private static Adocao fromDto(AdocaoDto adocaoDto) {
        return new Adocao(null, null, pet(adocaoDto), UsuarioLogado.getUsuario(), false);
    }

    public static Adocao getAdocao(AdocaoDto adocaoDto) {
        return fromDto(adocaoDto);
    }

    private static void updateData(Adocao adocao, AdocaoUpdateDto adocaoDto) {
        adocao.setId(adocaoDto.id());
        adocao.setConfirmarAdocao(adocaoDto.confirmarAdocao());
    }

    public static void getAdocaoAtualizada(Adocao adocao, AdocaoUpdateDto adocaoDto) {
        updateData(adocao, adocaoDto);
    }

    private static Animais pet(AdocaoDto adocaoDto) {
        Animais pet = new Animais();
        pet.setId(adocaoDto.petId());
        return pet;
    }
}