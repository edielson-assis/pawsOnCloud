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

import br.com.pawsoncloud.dtos.AdocaoDto;
import br.com.pawsoncloud.dtos.AdocaoRespDto;
import br.com.pawsoncloud.dtos.AdocaoUpdateDto;
import br.com.pawsoncloud.entidades.Adocao;
import br.com.pawsoncloud.servicos.AdocaoServico;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/adocoes")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Adoções")
public class AdocaoControle {
    
    private final AdocaoServico servico;

    @Transactional
    @PostMapping
    public ResponseEntity<AdocaoRespDto> create(@Valid @RequestBody AdocaoDto adocaoDto) {
        Adocao adocao = servico.create(adocaoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}").buildAndExpand(adocao.getId()).toUri();
        return ResponseEntity.created(uri).body(new AdocaoRespDto(adocao));
    }

    @GetMapping
    public ResponseEntity<List<AdocaoRespDto>> findByCpf() {
        var adocoes = servico.findByCpf();
        var adocoesDto = adocoes.stream().map(AdocaoRespDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(adocoesDto);
    }

    @Transactional
    @PatchMapping(value = "/{id}") 
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