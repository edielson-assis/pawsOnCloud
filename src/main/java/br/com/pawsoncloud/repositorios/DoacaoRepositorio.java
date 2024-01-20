package br.com.pawsoncloud.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.pawsoncloud.entidades.Doacao;

/**
 * Interface de repositório para a entidade Doacao.
 * 
 * @author Edielson Assis
 */
public interface DoacaoRepositorio extends JpaRepository<Doacao, Long> {

    /**
     * Retorna uma lista de doações confirmadas.
     *
     * @return Lista de doações confirmadas.
     */
    List<Doacao> findByConfirmarDoacaoTrue();

    /**
     * Retorna uma lista de doações associadas a um usuário pelo CPF.
     *
     * @param cpf O CPF do usuário.
     * @return Lista de doações associadas ao usuário.
     */
    @Query("SELECT d FROM Doacao d WHERE d.doador.cpf = :cpf")
    List<Doacao> findByUsuarioCpf(@Param("cpf") String cpf);
}