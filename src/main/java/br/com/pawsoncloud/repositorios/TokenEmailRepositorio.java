package br.com.pawsoncloud.repositorios;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.pawsoncloud.entidades.TokenEmail;
import jakarta.transaction.Transactional;

public interface TokenEmailRepositorio extends JpaRepository<TokenEmail, Long> {

    Optional<TokenEmail> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE TokenEmail t SET t.confirmadoAs = ?2 WHERE t.token = ?1")
    int updateConfirmadoAs(String token, LocalDateTime confirmadoAs);
}