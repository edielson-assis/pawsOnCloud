package com.proz.projetointegrador.controladores;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.proz.projetointegrador.dto.AnimaisDto;
import com.proz.projetointegrador.dto.AnimaisRespDto;
import com.proz.projetointegrador.dto.AnimaisUpdateDto;
import com.proz.projetointegrador.entidades.Animais;
import com.proz.projetointegrador.servicos.AnimaisServico;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/animais")
public class AnimaisControle {
    
    private AnimaisServico servico;
    
    @Transactional
    @PostMapping
    public ResponseEntity<Animais> create(@Valid @RequestBody AnimaisDto petDto) {
        Animais pet = servico.create(petDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}").buildAndExpand(pet.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<Page<AnimaisRespDto>> findAll(@PageableDefault(size = 10, sort = {"id"}) Pageable pageable) {
        var page = servico.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AnimaisRespDto> findById(@PathVariable Long id) {
        Animais pet = servico.findById(id);
        return ResponseEntity.ok().body(new AnimaisRespDto(pet));
    }

    @Transactional
    @PutMapping(value = "/{id}") 
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody AnimaisUpdateDto petDto) {
        servico.update(id, petDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servico.delete(id);
        return ResponseEntity.noContent().build();
    }
}