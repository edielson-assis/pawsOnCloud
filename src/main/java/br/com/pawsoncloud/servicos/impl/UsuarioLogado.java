package br.com.pawsoncloud.servicos.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.pawsoncloud.entidades.Usuario;

/**
 * Classe responsável por pegar os dados do usuário logado.
 * 
 * @author Edielson Assis
 */
public class UsuarioLogado {
    
    private static Usuario usuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();
    }

    
    /** 
     * Método estático que retornar um usuário autenticado.
     * @return Usuario
     */
    public static Usuario getUsuario() {
        return usuarioAutenticado();
    }
}