package br.com.pawsoncloud.controladores;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.pawsoncloud.dtos.UsuarioDto;
import br.com.pawsoncloud.dtos.UsuarioFullRespDto;
import br.com.pawsoncloud.dtos.UsuarioResponseDto;
import br.com.pawsoncloud.dtos.UsuarioUpdateDto;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.servicos.UsuarioRegistroServico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * Controller responsável por manipular operações relacionadas a usuários.
 * 
 * @author Edielson Assis
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "api/v1/usuario")
@Tag(name = "Usuários")
public class UsuarioRegistroControle {
    
    private final UsuarioRegistroServico servico;
    private static final String SECURITY_SCHEME_KEY = "bearer-key";
    
    /**
     * Cadastra um usuário.
     * 
     * @param usuarioDto DTO contendo informações do novo usuário.
     * @return Um json com os dados do usuário e o código http 201
     */
    @Transactional
    @PostMapping(path = "/cadastro")
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioDto usuarioDto) {
        Usuario usuario = servico.create(usuarioDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new UsuarioResponseDto(usuario));
    }

    /**
     * Valida o email do usuário por meio do token
     * 
     * @param token token de validação.
     * @return Mensagem de validação.
     */
    @GetMapping(path = "/confirmar")
    public ResponseEntity<String> confirmarToken(@RequestParam("token") String token) {
        return ResponseEntity.ok(servico.confirmarToken(token));
    }

    /**
     * Retorna todos os dados do usuário com base no cpf.
     * 
     * @return Um json com os dados do usuário e o código http 200
     */
    @Operation(security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)})
    @GetMapping
    public ResponseEntity<UsuarioFullRespDto> findByCpf() {
        Usuario usuario = servico.findByCpf();
        return ResponseEntity.ok().body(new UsuarioFullRespDto(usuario));
    }

    /**
     * Atualiza os dados do usuário.
     * 
     * @param usuarioDto DTO contendo as novas informações do usuário.
     * @return Um json com os dados do usuário e o código http 200
     */
    @Operation(security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)})
    @Transactional
    @PutMapping 
    public ResponseEntity<UsuarioFullRespDto> update(@Valid @RequestBody UsuarioUpdateDto usuarioDto) {
        Usuario usuario = servico.update(usuarioDto);
        return ResponseEntity.ok().body(new UsuarioFullRespDto(usuario));
    }

    /**
     * Apaga a conta do usuário.
     * 
     * @return Código http 204
     */
    @Operation(security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)})
    @DeleteMapping
    public ResponseEntity<Void> delete() {
        servico.delete();
        return ResponseEntity.noContent().build();
    }
}