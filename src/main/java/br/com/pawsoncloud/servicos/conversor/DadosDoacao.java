package br.com.pawsoncloud.servicos.conversor;

import java.time.LocalDate;

import br.com.pawsoncloud.dtos.DoacaoDto;
import br.com.pawsoncloud.dtos.DoacaoUpdateDto;
import br.com.pawsoncloud.entidades.Doacao;
import br.com.pawsoncloud.servicos.impl.UsuarioLogado;

/**
 * Classe responsável por realizar a conversão dos DTOs de doação.
 * 
 * @author Edielson Assis
 */
public class DadosDoacao {
    
    /** 
     * Cria uma doação e a mapeia com os dados do dto.
     * 
     * @param doacaoDto contém os dados da doação que será criada.
     * @return Doacao
     */
    public static Doacao getDoacao(DoacaoDto doacaoDto) {
        return new Doacao(null, null, LocalDate.now(), DadosAnimais.getAnimais(doacaoDto.pet()), UsuarioLogado.getUsuario(), false);
    }

    /**
     * Atualiza os dados do objeto.
     * 
     * @param doacao objeto que será atualizado.
     * @param doacaoDto contém os dados de atualização da doação.
     */
    public static void getDoacaoAtualizada(Doacao doacao, DoacaoUpdateDto doacaoDto) {
        doacao.setId(doacaoDto.id());
        doacao.setConfirmarDoacao(doacaoDto.confirmarDoacao());
    }
}