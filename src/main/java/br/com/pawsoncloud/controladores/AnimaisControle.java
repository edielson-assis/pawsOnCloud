package br.com.pawsoncloud.controladores;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pawsoncloud.dtos.AnimaisRespDto;
import br.com.pawsoncloud.dtos.AnimaisResponseDto;
import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.servicos.AnimaisServico;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/animais")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Animais")
public class AnimaisControle {
    
    private final AnimaisServico servico;

    @GetMapping
    public ResponseEntity<Page<AnimaisResponseDto>> findAll(@PageableDefault(size = 10, sort = {"id"}) Pageable pageable) {
        var page = servico.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AnimaisRespDto> findById(@PathVariable Long id) {
        Animais pet = servico.findById(id);
        return ResponseEntity.ok().body(new AnimaisRespDto(pet));
    }
}