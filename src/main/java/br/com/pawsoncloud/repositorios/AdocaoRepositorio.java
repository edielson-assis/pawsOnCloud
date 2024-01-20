package br.com.pawsoncloud.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.pawsoncloud.entidades.Adocao;

/**
 * Interface de repositório para a entidade Adocao.
 * 
 * @author Edielson Assis
 */
public interface AdocaoRepositorio extends JpaRepository<Adocao, Long> {

    /**
     * Retorna uma lista de adoções a serem confirmadas.
     *
     * @return Lista de adoções a serem confirmadas.
     */
    List<Adocao> findByConfirmarAdocaoTrue();

    /**
     * Retorna uma lista de adoções associadas a um usuário através do CPF.
     *
     * @param cpf O CPF do adotante.
     * @return Lista de adoções associadas ao usuário.
     */
    @Query("SELECT a FROM Adocao a WHERE a.adotante.cpf = :cpf")
    List<Adocao> findByUsuarioCpf(@Param("cpf") String cpf);
}