package br.com.pawsoncloud.servicos.conversor;

import br.com.pawsoncloud.dtos.AnimaisDto;
import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.entidades.enums.StatusAdocao;
import br.com.pawsoncloud.servicos.impl.UsuarioLogado;

/**
 * Classe responsável por realizar a conversão dos DTOs de animais.
 * 
 * @author Edielson Assis
 */
public class DadosAnimais {
    
    /** 
     * Cria um animal e o mapeia com os dados do dto.
     * 
     * @param animaisDto animal que será criado.
     * @return Animais
     */
    public static Animais getAnimais(AnimaisDto animaisDto) {
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
}