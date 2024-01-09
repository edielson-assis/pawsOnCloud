package br.com.pawsoncloud.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pawsoncloud.entidades.Doacao;

public interface DoacaoRepositorio extends JpaRepository<Doacao, Long> {

    List<Doacao> findByConfirmarDoacaoTrue();
}