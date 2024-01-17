package br.com.pawsoncloud.controladores;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.pawsoncloud.dtos.DoacaoDto;
import br.com.pawsoncloud.dtos.DoacaoRespDto;
import br.com.pawsoncloud.dtos.DoacaoUpdateDto;
import br.com.pawsoncloud.entidades.Doacao;
import br.com.pawsoncloud.servicos.DoacaoServico;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/doacoes")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Doações")
public class DoacaoControle {
    
    private final DoacaoServico servico;

    @Transactional
    @PostMapping
    public ResponseEntity<DoacaoRespDto> create(@Valid @RequestBody DoacaoDto doacaoDto) {
        Doacao doacao = servico.create(doacaoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}").buildAndExpand(doacao.getId()).toUri();
        return ResponseEntity.created(uri).body(new DoacaoRespDto(doacao));
    }

    @GetMapping
    public ResponseEntity<List<DoacaoRespDto>> findByCpf() {
        var doacoes = servico.findByCpf();
        var doacoesDto = doacoes.stream().map(DoacaoRespDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(doacoesDto);
    }

    @Transactional
    @PatchMapping(value = "/{id}") 
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody DoacaoUpdateDto doacaoUpdateDto) {
        servico.update(id, doacaoUpdateDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servico.delete(id);
        return ResponseEntity.noContent().build();
    }
}