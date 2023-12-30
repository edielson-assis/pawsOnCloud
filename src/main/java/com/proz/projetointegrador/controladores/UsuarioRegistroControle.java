package com.proz.projetointegrador.controladores;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.proz.projetointegrador.dto.UsuarioDto;
import com.proz.projetointegrador.dto.UsuarioRespDto;
import com.proz.projetointegrador.dto.UsuarioUpdateDto;
import com.proz.projetointegrador.entidades.Usuario;
import com.proz.projetointegrador.servicos.UsuarioRegistroServico;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/usuario/cadastro")
public class UsuarioRegistroControle {
    
    private UsuarioRegistroServico servico;
    
    @Transactional
    @PostMapping
    public ResponseEntity<Usuario> create(@Valid @RequestBody UsuarioDto usuarioDto) {
        Usuario usuario = servico.create(usuarioDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servico.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioRespDto>> findAll(@PageableDefault(size = 10, sort = {"id"}) Pageable pageable) {
        var page = servico.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UsuarioRespDto> findById(@PathVariable Long id) {
        Usuario usuario = servico.findById(id);
        return ResponseEntity.ok().body(new UsuarioRespDto(usuario));
    }

    @Transactional
    @PutMapping(value = "/{id}") 
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDto usuarioDto) {
        servico.update(id, usuarioDto);
        return ResponseEntity.noContent().build();
    }
}