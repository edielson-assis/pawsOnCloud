package com.proz.projetointegrador.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proz.projetointegrador.entidades.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    
}
