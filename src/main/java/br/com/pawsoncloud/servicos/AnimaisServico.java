package br.com.pawsoncloud.servicos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.pawsoncloud.dto.AnimaisDto;
import br.com.pawsoncloud.dto.AnimaisRespDto;
import br.com.pawsoncloud.dto.AnimaisUpdateDto;
import br.com.pawsoncloud.entidades.Animais;

public interface AnimaisServico {
    
    Animais create(AnimaisDto animaisDto);

    Page<AnimaisRespDto> findAll(Pageable pageable);

    Animais findById(Long id);

    Animais update(Long id, AnimaisUpdateDto animaisDto);

    void delete(Long id);
}