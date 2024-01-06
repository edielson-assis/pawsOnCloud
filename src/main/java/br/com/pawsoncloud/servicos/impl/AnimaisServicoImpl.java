package br.com.pawsoncloud.servicos.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.pawsoncloud.dto.AnimaisRespDto;
import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.repositorios.AnimaisRepositorio;
import br.com.pawsoncloud.servicos.AnimaisServico;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AnimaisServicoImpl implements AnimaisServico {

    private AnimaisRepositorio repositorio;

    @Override
    public Page<AnimaisRespDto> findAll(Pageable pageable) {
        Page<Animais> animais = repositorio.findAll(pageable);
        return animais.map(AnimaisRespDto::new);
    }

    @Override
    public Animais findById(Long id) {
        Optional<Animais> pet = repositorio.findById(id);
        return pet.orElseThrow(() -> new ObjectNotFoundException("Pet não encontrado. Id inválido: " + id));
    }
}