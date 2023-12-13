package com.proz.projetointegrador.controladores;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.proz.projetointegrador.dto.EnderecoDto;
import com.proz.projetointegrador.dto.EnderecoRespDto;
import com.proz.projetointegrador.entidades.Endereco;
import com.proz.projetointegrador.servicos.EnderecoServico;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/enderecos")
public class EnderecoControle {
    
    private EnderecoServico servico;

    @Transactional
    @PostMapping
    public ResponseEntity<Endereco> create(@Valid @RequestBody EnderecoDto enderecoDto) {
        Endereco endereco = servico.create(enderecoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}").buildAndExpand(endereco.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EnderecoRespDto> findById(@PathVariable Long id) {
        Endereco endereco = servico.findById(id);
        return ResponseEntity.ok().body(new EnderecoRespDto(endereco));
    }
}