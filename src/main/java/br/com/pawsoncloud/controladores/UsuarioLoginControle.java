package br.com.pawsoncloud.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pawsoncloud.dto.UsuarioAutenticado;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.seguranca.TokenJWT;
import br.com.pawsoncloud.seguranca.TokenService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/login")
public class UsuarioLoginControle {
    
    private AuthenticationManager manager;
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenJWT> login(@RequestBody @Valid UsuarioAutenticado usuario) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(usuario.email(), usuario.senha());
        var authentication = manager.authenticate(authenticationToken);
        
        var tokenJWT = tokenService.generateToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenJWT(tokenJWT));
    }
}