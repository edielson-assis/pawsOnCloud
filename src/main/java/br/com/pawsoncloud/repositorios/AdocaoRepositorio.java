package br.com.pawsoncloud.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pawsoncloud.entidades.Adocao;

public interface AdocaoRepositorio extends JpaRepository<Adocao, Long> {

    List<Adocao> findByConfirmarAdocaoTrue();
}