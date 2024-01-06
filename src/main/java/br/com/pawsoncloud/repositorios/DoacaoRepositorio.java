package br.com.pawsoncloud.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pawsoncloud.entidades.Doacao;

public interface DoacaoRepositorio extends JpaRepository<Doacao, Long> {}