package br.com.pawsoncloud.repositorios;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.pawsoncloud.entidades.TokenEmail;
import jakarta.transaction.Transactional;

/**
 * Interface de repositório para a entidade TokenEmail.
 * 
 * @author Edielson Assis
 */
public interface TokenEmailRepositorio extends JpaRepository<TokenEmail, Long> {

    /**
     * Encontra um TokenEmail pelo token.
     *
     * @param token O token a ser pesquisado.
     * @return Um Optional contendo o TokenEmail, se encontrado.
     */
    Optional<TokenEmail> findByToken(String token);

    /**
     * Atualiza a data de confirmação de um TokenEmail pelo token.
     *
     * @param token O token do TokenEmail a ser atualizado.
     * @param confirmadoAs A nova data de confirmação.
     * @return O número de registros afetados pela operação.
     */
    @Transactional
    @Modifying
    @Query("UPDATE TokenEmail t SET t.confirmadoAs = ?2 WHERE t.token = ?1")
    int updateConfirmadoAs(String token, LocalDateTime confirmadoAs);

    /**
     * Deleta o TokenEmail associado ao usuário, se o campo confirmadoAs estiver nulo.
     *
     * @param usuarioId O ID do usuário associado ao token.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM TokenEmail t WHERE t.usuario.id = :usuarioId AND t.confirmadoAs IS NULL")
    void deletarTokenNaoConfirmadosPorUsuario(@Param("usuarioId") Long usuarioId);
}