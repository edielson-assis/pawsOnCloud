package br.com.pawsoncloud.servicos.conversor;

import java.time.LocalDate;

import br.com.pawsoncloud.dtos.DoacaoDto;
import br.com.pawsoncloud.dtos.DoacaoUpdateDto;
import br.com.pawsoncloud.entidades.Doacao;
import br.com.pawsoncloud.servicos.impl.UsuarioLogado;

public class DadosDoacao {
    
    private static Doacao fromDto(DoacaoDto doacaoDto) {
        return new Doacao(null, null, LocalDate.now(), DadosAnimais.getAnimais(doacaoDto.pet()), UsuarioLogado.getUsuario(), false);
    }

    public static Doacao getDoacao(DoacaoDto doacaoDto) {
        return fromDto(doacaoDto);
    }

    private static void updateData(Doacao doacao, DoacaoUpdateDto doacaoDto) {
        doacao.setId(doacaoDto.id());
        doacao.setConfirmarDoacao(doacaoDto.confirmarDoacao());
    }

    public static void getDoacaoAtualizada(Doacao doacao, DoacaoUpdateDto doacaoDto) {
        updateData(doacao, doacaoDto);
    }
}