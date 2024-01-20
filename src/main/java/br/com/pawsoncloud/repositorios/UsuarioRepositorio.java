package br.com.pawsoncloud.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.pawsoncloud.entidades.Usuario;
import jakarta.transaction.Transactional;

/**
 * Interface de repositório para a entidade Usuario.
 * 
 * @author Edielson Assis
 */
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    
    /**
     * Verifica se um usuário com o e-mail fornecido já existe.
     *
     * @param email O e-mail a ser verificado.
     * @return true se o usuário com o e-mail existe, false caso contrário.
     */
    boolean existsByEmail(String email);

    /**
     * Verifica se um usuário com o CPF fornecido já existe.
     *
     * @param cpf O CPF a ser verificado.
     * @return true se o usuário com o CPF existe, false caso contrário.
     */
    boolean existsByCpf(String cpf);

    /**
     * Encontra detalhes do usuário pelo e-mail.
     *
     * @param email O e-mail do usuário.
     * @return Detalhes do usuário.
     */
    UserDetails findByEmail(String email);

    /**
     * Encontra um usuário pelo CPF.
     *
     * @param cpf O CPF do usuário.
     * @return Um Optional contendo o usuário, se encontrado.
     */
    Optional<Usuario> findByCpf(String cpf);

    /**
     * Ativa um usuário com o e-mail fornecido.
     *
     * @param email O e-mail do usuário a ser ativado.
     * @return O número de registros afetados pela operação.
     */
    @Transactional
    @Modifying
    @Query("UPDATE Usuario u SET u.ativo = TRUE WHERE u.email = ?1")
    int ativarUsuario(String email);
}