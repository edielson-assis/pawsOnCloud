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

/**
 * Controller responsável por manipular operações relacionadas as adoções do usuário.
 * 
 * @author Edielson Assis
 */
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/adocoes")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Adoções")
public class AdocaoControle {
    
    private final AdocaoServico servico;

    /**
     * Realiza uma adoção.
     * 
     * @param adocaoDto DTO com os dados da nova adoção.
     * @return Um json com os dados da adoção e o código http 201
     */
    @Transactional
    @PostMapping
    public ResponseEntity<AdocaoRespDto> create(@Valid @RequestBody AdocaoDto adocaoDto) {
        Adocao adocao = servico.create(adocaoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}").buildAndExpand(adocao.getId()).toUri();
        return ResponseEntity.created(uri).body(new AdocaoRespDto(adocao));
    }

    /**
     * Pega todas as adoções com base no cpf do usuário.
     * 
     * @return Um json com todas as adoções e o código http 200
     */
    @GetMapping
    public ResponseEntity<List<AdocaoRespDto>> findByCpf() {
        var adocoes = servico.findByCpf();
        var adocoesDto = adocoes.stream().map(AdocaoRespDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(adocoesDto);
    }

    /**
     * Atualiza os dados da adoção.
     * 
     * @param id ID da adoção a ser atualizada.
     * @param adocaoUpdateDto DTO contendo os dados da adoção.
     * @return Código http 204
     */
    @Transactional
    @PatchMapping(value = "/{id}") 
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody AdocaoUpdateDto adocaoUpdateDto) {
        servico.update(id, adocaoUpdateDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cancela uma adoção.
     * 
     * @param id ID da adoção a ser deletada.
     * @return Código http 204
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servico.delete(id);
        return ResponseEntity.noContent().build();
    }
}