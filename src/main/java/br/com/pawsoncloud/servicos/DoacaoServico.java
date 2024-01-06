package br.com.pawsoncloud.servicos;

import br.com.pawsoncloud.dto.DoacaoDto;
import br.com.pawsoncloud.dto.DoacaoUpdateDto;
import br.com.pawsoncloud.entidades.Doacao;

public interface DoacaoServico {
    
    Doacao create(DoacaoDto doacaoDto);

    Doacao update(Long id, DoacaoUpdateDto doacaoDto);

    void delete(Long id);
}