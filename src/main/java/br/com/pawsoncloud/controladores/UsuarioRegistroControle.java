package br.com.pawsoncloud.controladores;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.pawsoncloud.dto.UsuarioDto;
import br.com.pawsoncloud.dto.UsuarioFullRespDto;
import br.com.pawsoncloud.dto.UsuarioUpdateDto;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.servicos.UsuarioRegistroServico;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/usuario")
@SecurityRequirement(name = "bearer-key")
public class UsuarioRegistroControle {
    
    private UsuarioRegistroServico servico;
    
    @Transactional
    @PostMapping(path = "/cadastro")
    public ResponseEntity<Usuario> create(@Valid @RequestBody UsuarioDto usuarioDto) {
        Usuario usuario = servico.create(usuarioDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<UsuarioFullRespDto> findByCpf() {
        Usuario usuario = servico.findByCpf();
        return ResponseEntity.ok().body(new UsuarioFullRespDto(usuario));
    }

    @Transactional
    @PutMapping 
    public ResponseEntity<Void> update(@Valid @RequestBody UsuarioUpdateDto usuarioDto) {
        servico.update(usuarioDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete() {
        servico.delete();
        return ResponseEntity.noContent().build();
    }
}