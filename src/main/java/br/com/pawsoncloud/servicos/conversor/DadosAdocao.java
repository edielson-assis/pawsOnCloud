package br.com.pawsoncloud.servicos.conversor;

import java.time.LocalDate;

import br.com.pawsoncloud.dto.AdocaoDto;
import br.com.pawsoncloud.dto.AdocaoUpdateDto;
import br.com.pawsoncloud.entidades.Adocao;
import br.com.pawsoncloud.entidades.Animais;

public class DadosAdocao {
    
    private static Adocao fromDto(AdocaoDto adocaoDto) {
        return new Adocao(null, LocalDate.now(), pet(adocaoDto), UsuarioLogado.getUsuario(), adocaoDto.confirmarAdocao());
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