package br.com.pawsoncloud.servicos.conversor;

import java.time.LocalDate;

import br.com.pawsoncloud.dto.DoacaoDto;
import br.com.pawsoncloud.dto.DoacaoUpdateDto;
import br.com.pawsoncloud.entidades.Doacao;

public class DadosDoacao {
    
    private static Doacao fromDto(DoacaoDto doacaoDto) {
        return new Doacao(null, LocalDate.now(), DadosAnimais.getAnimais(doacaoDto.pet()), UsuarioLogado.getUsuario());
    }

    public static Doacao getDoacao(DoacaoDto doacaoDto) {
        return fromDto(doacaoDto);
    }

    private static void updateData(Doacao doacao, DoacaoUpdateDto doacaoDto) {
        doacao.setId(doacaoDto.id());
        doacao.setPet(DadosAnimais.getPetAtualizado(doacao.getPet(), doacaoDto.pet()));
    }

    public static void getDoacaoAtualizada(Doacao doacao, DoacaoUpdateDto doacaoDto) {
        updateData(doacao, doacaoDto);
    }
}