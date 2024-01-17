package br.com.pawsoncloud.servicos;

import java.util.List;

import br.com.pawsoncloud.dtos.AdocaoDto;
import br.com.pawsoncloud.dtos.AdocaoUpdateDto;
import br.com.pawsoncloud.entidades.Adocao;

public interface AdocaoServico {
    
    Adocao create(AdocaoDto adocaoDto);

    List<Adocao> findByCpf();

    Adocao update(Long id, AdocaoUpdateDto adocaoDto);

    void delete(Long id);
}