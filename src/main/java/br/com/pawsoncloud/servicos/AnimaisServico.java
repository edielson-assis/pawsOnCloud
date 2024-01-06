package br.com.pawsoncloud.servicos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.pawsoncloud.dto.AnimaisRespDto;
import br.com.pawsoncloud.entidades.Animais;

public interface AnimaisServico {

    Page<AnimaisRespDto> findAll(Pageable pageable);

    Animais findById(Long id);
}