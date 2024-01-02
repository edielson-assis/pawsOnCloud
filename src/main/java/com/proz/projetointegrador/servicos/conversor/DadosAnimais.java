package com.proz.projetointegrador.servicos.conversor;

import com.proz.projetointegrador.dto.AnimaisDto;
import com.proz.projetointegrador.dto.AnimaisUpdateDto;
import com.proz.projetointegrador.entidades.Animais;
import com.proz.projetointegrador.entidades.enums.StatusAdocao;

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
        DadosUsuario.getUsuario(animaisDto.usuarioDto()));
    }

    public static Animais getAnimais(AnimaisDto animaisDto) {
        return fromDto(animaisDto);
    }

    private static void updateData(Animais pet, AnimaisUpdateDto petDto) {
        pet.setNome(petDto.nome());
        pet.setPorte(petDto.porte());
        pet.setIdade(petDto.idade());
        pet.setEspecie(petDto.especie());
        pet.setPelagem(petDto.pelagem());
        pet.setPeso(petDto.peso());
        pet.setImgUrl(petDto.imgUrl());
    }  

    public static void getPetAtualizado(Animais pet, AnimaisUpdateDto petDto) {
        updateData(pet, petDto);
    }
}