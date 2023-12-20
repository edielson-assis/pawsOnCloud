package com.proz.projetointegrador.servicos.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.proz.projetointegrador.dto.EnderecoDto;
import com.proz.projetointegrador.dto.EnderecoUpdateDto;
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
        Endereco endereco = new Endereco(enderecoDto);
        return repositorio.save(endereco);
    }

    @Override
    public Endereco findById(Long id) {
        Optional<Endereco> endereco = repositorio.findById(id);
        return endereco.orElseThrow(() -> new ObjectNotFoundException("Endereço não encotrado. Id invalido: " + id));
    }

    @Override
    public Endereco update(Long id, EnderecoUpdateDto enderecoUpdateDto) {
        Endereco endereco = updateData(id, enderecoUpdateDto);
        return repositorio.save(endereco);
    }

    private Endereco updateData(Long id, EnderecoUpdateDto enderecoUpdateDto) {
        Endereco endereo = findById(id);
        endereo.setLogradouro(enderecoUpdateDto.logradouro());
        if (enderecoUpdateDto.complemento() != null) {
            endereo.setComplemento(enderecoUpdateDto.complemento());
        }
        endereo.setCidade(enderecoUpdateDto.cidade());
        endereo.setEstado(enderecoUpdateDto.estado());
        return endereo;
    }
}