package br.com.pawsoncloud.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.pawsoncloud.entidades.Doacao;

public interface DoacaoRepositorio extends JpaRepository<Doacao, Long> {

    List<Doacao> findByConfirmarDoacaoTrue();

    @Query("SELECT d FROM Doacao d WHERE d.doador.cpf = :cpf")
    List<Doacao> findByUsuarioCpf(@Param("cpf") String cpf);
}