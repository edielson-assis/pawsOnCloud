package br.com.pawsoncloud.servicos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.pawsoncloud.dto.AnimaisResponseDto;
import br.com.pawsoncloud.entidades.Animais;

public interface AnimaisServico {

    Page<AnimaisResponseDto> findAll(Pageable pageable);

    Animais findById(Long id);
}