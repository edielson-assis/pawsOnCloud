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

/**
 * Controller responsável por manipular operações relacionadas as doações do usuário.
 * 
 * @author Edielson Assis
 */
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/doacoes")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Doações")
public class DoacaoControle {
    
    private final DoacaoServico servico;

    /**
     * Realiza uma doação.
     * 
     * @param doacaoDto DTO com os dados da nova doação.
     * @return Um json com os dados da doação e o código http 201
     */
    @Transactional
    @PostMapping
    public ResponseEntity<DoacaoRespDto> create(@Valid @RequestBody DoacaoDto doacaoDto) {
        Doacao doacao = servico.create(doacaoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}").buildAndExpand(doacao.getId()).toUri();
        return ResponseEntity.created(uri).body(new DoacaoRespDto(doacao));
    }

    /**
     * Retorna todas as doações com base no cpf do usuário.
     * 
     * @return Um json com todas as doações e o código http 200
     */
    @GetMapping
    public ResponseEntity<List<DoacaoRespDto>> findByCpf() {
        var doacoes = servico.findByCpf();
        var doacoesDto = doacoes.stream().map(DoacaoRespDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(doacoesDto);
    }

    /**
     * Atualiza os dados da doação.
     * 
     * @param id ID da doação a ser atualizada.
     * @param doacaoUpdateDto DTO contendo os dados da doação.
     * @return Código http 204
     */
    @Transactional
    @PatchMapping(value = "/{id}") 
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody DoacaoUpdateDto doacaoUpdateDto) {
        servico.update(id, doacaoUpdateDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cancela uma doação.
     * 
     * @param id ID da doação a ser deletada.
     * @return Código http 204
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servico.delete(id);
        return ResponseEntity.noContent().build();
    }
}