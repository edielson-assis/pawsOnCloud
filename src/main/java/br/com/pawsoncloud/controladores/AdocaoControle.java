package br.com.pawsoncloud.controladores;

import java.net.URI;
import java.nio.file.AccessDeniedException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.pawsoncloud.dto.AdocaoDto;
import br.com.pawsoncloud.dto.AdocaoUpdateDto;
import br.com.pawsoncloud.entidades.Adocao;
import br.com.pawsoncloud.servicos.AdocaoServico;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/adocoes")
public class AdocaoControle {
    
    private AdocaoServico servico;

    @Transactional
    @PostMapping
    public ResponseEntity<Adocao> create(@Valid @RequestBody AdocaoDto adocaoDto) throws AccessDeniedException {
        Adocao adocao = servico.create(adocaoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}").buildAndExpand(adocao.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Transactional
    @PutMapping(value = "/{id}") 
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody AdocaoUpdateDto adocaoUpdateDto) {
        servico.update(id, adocaoUpdateDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servico.delete(id);
        return ResponseEntity.noContent().build();
    }
}