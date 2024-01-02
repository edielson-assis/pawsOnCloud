package com.proz.projetointegrador.servicos.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.proz.projetointegrador.dto.EnderecoDto;
import com.proz.projetointegrador.dto.EnderecoUpdateDto;
import com.proz.projetointegrador.entidades.Endereco;
import com.proz.projetointegrador.repositorios.EnderecoRepositorio;
import com.proz.projetointegrador.servicos.EnderecoServico;
import com.proz.projetointegrador.servicos.conversor.DadosEndereco;
import com.proz.projetointegrador.servicos.excecoes.ObjectNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EnderecoServicoImpl implements EnderecoServico {

    private EnderecoRepositorio repositorio;

    @Override
    public Endereco create(EnderecoDto enderecoDto) {
        Endereco endereco = DadosEndereco.getEndereco(enderecoDto);
        return repositorio.save(endereco);
    }

    @Override
    public Endereco findById(Long id) {
        Optional<Endereco> endereco = repositorio.findById(id);
        return endereco.orElseThrow(() -> new ObjectNotFoundException("Endereço não encotrado. Id invalido: " + id));
    }

    @Override
    public Endereco update(Long id, EnderecoUpdateDto enderecoUpdateDto) {
        Endereco endereco = findById(id);
        DadosEndereco.getEnderecoAtualizado(endereco, enderecoUpdateDto);
        return repositorio.save(endereco);
    }
}