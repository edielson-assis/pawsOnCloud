package br.com.pawsoncloud.servicos;

import java.util.Optional;

import br.com.pawsoncloud.entidades.TokenEmail;

public interface TokenEmailServico {
    
    void createToken(TokenEmail token);

    Optional<TokenEmail> findByToken(String token);

    int setConfirmadoAs(String token);
}