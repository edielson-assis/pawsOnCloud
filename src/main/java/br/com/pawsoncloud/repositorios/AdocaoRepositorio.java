package br.com.pawsoncloud.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.pawsoncloud.entidades.Adocao;

public interface AdocaoRepositorio extends JpaRepository<Adocao, Long> {

    List<Adocao> findByConfirmarAdocaoTrue();

    @Query("SELECT a FROM Adocao a WHERE a.adotante.cpf = :cpf")
    List<Adocao> findByUsuarioCpf(@Param("cpf") String cpf);
}