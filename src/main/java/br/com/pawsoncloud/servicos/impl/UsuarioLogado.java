package br.com.pawsoncloud.servicos.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.pawsoncloud.entidades.Usuario;

public class UsuarioLogado {
    
    private static Usuario usuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();
    }

    public static Usuario getUsuario() {
        return usuarioAutenticado();
    }
}