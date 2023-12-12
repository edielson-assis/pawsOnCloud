package com.proz.projetointegrador.servicos.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.proz.projetointegrador.dto.EnderecoDto;
import com.proz.projetointegrador.entidades.Endereco;
import com.proz.projetointegrador.repositorios.EnderecoRepositorio;
import com.proz.projetointegrador.servicos.EnderecoServico;
import com.proz.projetointegrador.servicos.excecoes.ObjectNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EnderecoServicoImpl implements EnderecoServico {

    private EnderecoRepositorio repositorio;

    @Override
    public Endereco create(EnderecoDto enderecoDto) {
        Endereco endereco = fromDto(enderecoDto);
        return repositorio.save(endereco);
    }

    @Override
    public Endereco findById(Long id) {
        Optional<Endereco> endereco = repositorio.findById(id);
        return endereco.orElseThrow(() -> new ObjectNotFoundException("Endereço não encotrado. Id invalido: " + id));
    }
    
    private Endereco fromDto(EnderecoDto enderecoDto) {
        return new Endereco(null, enderecoDto.logradouro(), enderecoDto.complemento(), enderecoDto.cidade(), enderecoDto.estado()); 
    }
}