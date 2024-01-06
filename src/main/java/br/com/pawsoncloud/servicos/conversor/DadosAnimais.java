package br.com.pawsoncloud.servicos.conversor;

import br.com.pawsoncloud.dto.AnimaisDto;
import br.com.pawsoncloud.dto.AnimaisUpdateDto;
import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.entidades.enums.StatusAdocao;

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
        UsuarioLogado.getUsuario());
    }

    public static Animais getAnimais(AnimaisDto animaisDto) {
        return fromDto(animaisDto);
    }

    private static Animais updateData(Animais pet, AnimaisUpdateDto petDto) {
        pet.setNome(petDto.nome());
        pet.setPorte(petDto.porte());
        pet.setIdade(petDto.idade());
        pet.setEspecie(petDto.especie());
        pet.setPelagem(petDto.pelagem());
        pet.setPeso(petDto.peso());
        pet.setImgUrl(petDto.imgUrl());
        return pet;
    }  

    public static Animais getPetAtualizado(Animais pet, AnimaisUpdateDto petDto) {
        return updateData(pet, petDto);
    }
}