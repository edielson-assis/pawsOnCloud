package br.com.pawsoncloud.repositorios;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.pawsoncloud.entidades.Animais;

/**
 * Interface de repositório para a entidade Animais.
 * 
 * @author Edielson Assis
 */
public interface AnimaisRepositorio extends JpaRepository<Animais, Long> {

    /**
     * Retorna uma página de animais não adotados.
     *
     * @param page Objeto Pageable para paginação.
     * @return Página de animais não adotados.
     */
    Page<Animais> findAllByAdotadoFalse(Pageable page);

    /**
     * Retorna um animal não adotado com base no ID.
     *
     * @param id O ID do animal.
     * @return Um Optional contendo o animal não adotado, se encontrado.
     */
    @Query("""
            SELECT a FROM Animais a
            WHERE a.adotado = false
            AND a.id = :id
        """)
    Optional<Animais> findByAdotadoFalse(Long id);
}