package com.proz.projetointegrador.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proz.projetointegrador.dto.UsuarioAutenticado;
import com.proz.projetointegrador.entidades.Usuario;
import com.proz.projetointegrador.seguranca.TokenJWT;
import com.proz.projetointegrador.seguranca.TokenService;

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