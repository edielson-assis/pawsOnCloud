package br.com.pawsoncloud.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.pawsoncloud.entidades.Usuario;
import jakarta.transaction.Transactional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    UserDetails findByEmail(String email);
    Optional<Usuario> findByCpf(String cpf);

    @Transactional
    @Modifying
    @Query("UPDATE Usuario u SET u.ativo = TRUE WHERE u.email = ?1")
    int ativarUsuario(String email);
}