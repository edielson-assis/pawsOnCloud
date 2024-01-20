package br.com.pawsoncloud.servicos.conversor;

import br.com.pawsoncloud.dtos.AdocaoDto;
import br.com.pawsoncloud.dtos.AdocaoUpdateDto;
import br.com.pawsoncloud.entidades.Adocao;
import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.servicos.impl.UsuarioLogado;

/**
 * Classe responsável por realizar a conversão dos DTOs de adoção.
 * 
 * @author Edielson Assis
 */
public class DadosAdocao {
    
    /** 
     * Cria uma adoção e a mapeia com os dados do dto.
     * 
     * @param adocaoDto contém os dados da adoção que será criada.
     * @return Adocao
     */
    public static Adocao getAdocao(AdocaoDto adocaoDto) {
        return new Adocao(null, null, pet(adocaoDto), UsuarioLogado.getUsuario(), false);
    }

    /**
     * Atualiza os dados do objeto.
     * 
     * @param adocao objeto que será atualizado.
     * @param adocaoDto contém os dados de atualização da adoção.
     */
    public static void getAdocaoAtualizada(Adocao adocao, AdocaoUpdateDto adocaoDto) {
        adocao.setId(adocaoDto.id());
        adocao.setConfirmarAdocao(adocaoDto.confirmarAdocao());
    }
    // Seta o objeto instânciado com o id passado como parâmetro
    private static Animais pet(AdocaoDto adocaoDto) {
        Animais pet = new Animais();
        pet.setId(adocaoDto.petId());
        return pet;
    }
}