package br.com.pawsoncloud.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pawsoncloud.entidades.Animais;

public interface AnimaisRepositorio extends JpaRepository<Animais, Long> {}