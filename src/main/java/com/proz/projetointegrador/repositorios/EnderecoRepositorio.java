package com.proz.projetointegrador.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proz.projetointegrador.entidades.Endereco;

public interface EnderecoRepositorio extends JpaRepository<Endereco, Long> {}