package br.com.pawsoncloud.servicos;

import java.nio.file.AccessDeniedException;

import br.com.pawsoncloud.dto.AdocaoDto;
import br.com.pawsoncloud.dto.AdocaoUpdateDto;
import br.com.pawsoncloud.entidades.Adocao;

public interface AdocaoServico {
    
    Adocao create(AdocaoDto adocaoDto) throws AccessDeniedException;

    Adocao update(Long id, AdocaoUpdateDto adocaoDto);

    void delete(Long id);
}