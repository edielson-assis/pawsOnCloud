package br.com.pawsoncloud.repositorios;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.pawsoncloud.entidades.Animais;

public interface AnimaisRepositorio extends JpaRepository<Animais, Long> {

    Page<Animais> findAllByAdotadoFalse(Pageable page);

    @Query("""
            select a from Animais a
            where
            a.adotado = false
            and
            a.id = :id
        """)
    Optional<Animais> findByAdotadoFalse(Long id);
}