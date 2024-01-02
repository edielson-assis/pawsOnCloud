package com.proz.projetointegrador.servicos.impl;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.proz.projetointegrador.dto.AnimaisDto;
import com.proz.projetointegrador.dto.AnimaisRespDto;
import com.proz.projetointegrador.dto.AnimaisUpdateDto;
import com.proz.projetointegrador.entidades.Animais;
import com.proz.projetointegrador.repositorios.AnimaisRepositorio;
import com.proz.projetointegrador.servicos.AnimaisServico;
import com.proz.projetointegrador.servicos.conversor.DadosAnimais;
import com.proz.projetointegrador.servicos.excecoes.DataBaseException;
import com.proz.projetointegrador.servicos.excecoes.ObjectNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AnimaisServicoImpl implements AnimaisServico {

    private AnimaisRepositorio repositorio;

    @Override
    public Animais create(AnimaisDto animaisDto) {
        Animais pet = DadosAnimais.getAnimais(animaisDto);
        return repositorio.save(pet);
    }

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

    @Override
    public Animais update(Long id, AnimaisUpdateDto petDto) {
        Animais pet = findById(id);
        DadosAnimais.getPetAtualizado(pet, petDto);
        return repositorio.save(pet);
    }

    @Override
    public void delete(Long id) {
        try {
            repositorio.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }  
}