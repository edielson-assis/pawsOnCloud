package com.proz.projetointegrador.servicos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.proz.projetointegrador.dto.AnimaisDto;
import com.proz.projetointegrador.dto.AnimaisRespDto;
import com.proz.projetointegrador.dto.AnimaisUpdateDto;
import com.proz.projetointegrador.entidades.Animais;

public interface AnimaisServico {
    
    Animais create(AnimaisDto animaisDto);

    Page<AnimaisRespDto> findAll(Pageable pageable);

    Animais findById(Long id);

    Animais update(Long id, AnimaisUpdateDto animaisDto);

    void delete(Long id);
}