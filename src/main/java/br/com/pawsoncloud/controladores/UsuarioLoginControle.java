package br.com.pawsoncloud.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pawsoncloud.dtos.UsuarioAutenticado;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.seguranca.TokenJWT;
import br.com.pawsoncloud.seguranca.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * Controller responsável por manipular operações relacionadas ao login do usuário.
 * 
 * @author Edielson Assis
 */
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/login")
@Tag(name = "Login")
public class UsuarioLoginControle {
    
    private final AuthenticationManager manager;
    private final TokenService tokenService;

    /** 
     * Valida os dados de login do usuário e devolve um token JWT, se os dados estiverem corretos.
     * 
     * @param usuario DTO contendo informações do usuário.
     * @return Token JWT.
     */
    @PostMapping
    public ResponseEntity<TokenJWT> login(@RequestBody @Valid UsuarioAutenticado usuario) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(usuario.email(), usuario.senha());
        var authentication = manager.authenticate(authenticationToken);
        
        var tokenJWT = tokenService.generateToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenJWT(tokenJWT));
    }
}