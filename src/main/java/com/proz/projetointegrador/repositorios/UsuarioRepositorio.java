package com.proz.projetointegrador.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.proz.projetointegrador.entidades.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    UserDetails findByEmail(String email);
}