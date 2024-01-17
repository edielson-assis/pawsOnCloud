package br.com.pawsoncloud.servicos;

import java.util.List;

import br.com.pawsoncloud.dtos.DoacaoDto;
import br.com.pawsoncloud.dtos.DoacaoUpdateDto;
import br.com.pawsoncloud.entidades.Doacao;

public interface DoacaoServico {
    
    Doacao create(DoacaoDto doacaoDto);

    List<Doacao> findByCpf();

    Doacao update(Long id, DoacaoUpdateDto doacaoDto);

    void delete(Long id);
}