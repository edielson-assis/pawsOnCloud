package br.com.pawsoncloud.servicos.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.pawsoncloud.entidades.TokenEmail;
import br.com.pawsoncloud.repositorios.TokenEmailRepositorio;
import br.com.pawsoncloud.servicos.TokenEmailServico;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TokenEmailServicoImpl implements TokenEmailServico {

    private final TokenEmailRepositorio repositorio;

    @Override
    public void createToken(TokenEmail token) {
        repositorio.save(token);
    }

    @Override
    public Optional<TokenEmail> findByToken(String token) {
        return repositorio.findByToken(token);
    }

    @Override
    public int setConfirmadoAs(String token) {
        return repositorio.updateConfirmadoAs(token, LocalDateTime.now());
    }   
}