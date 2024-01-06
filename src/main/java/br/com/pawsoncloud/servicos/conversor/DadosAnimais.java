package br.com.pawsoncloud.servicos.conversor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.pawsoncloud.dto.AnimaisDto;
import br.com.pawsoncloud.dto.AnimaisUpdateDto;
import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.entidades.Usuario;
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
        usuarioAutenticado());
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

    private static Usuario usuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();
    }
}