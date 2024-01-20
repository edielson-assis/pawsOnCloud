package br.com.pawsoncloud.servicos.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.pawsoncloud.entidades.TokenEmail;
import br.com.pawsoncloud.repositorios.TokenEmailRepositorio;
import br.com.pawsoncloud.servicos.TokenEmailServico;
import lombok.AllArgsConstructor;

/**
 * Classe que implementa a interface <b>TokenEmailServico</b>.
 * 
 * @author Edielson Assis
 */
@Service
@AllArgsConstructor
public class TokenEmailServicoImpl implements TokenEmailServico {

    private final TokenEmailRepositorio repositorio;

    /** 
     * Cria um token e salva no banco de dados.
     * 
     * @param token token que será criado.
     */
    @Override
    public void createToken(TokenEmail token) {
        repositorio.save(token);
    }

    /**
     * Pega um token com base no parâmetro informado.
     * 
     * @param token parâmetro utilizado para localizar o token.
     */
    @Override
    public Optional<TokenEmail> findByToken(String token) {
        return repositorio.findByToken(token);
    }

    /**
     * Atualiza a data de confrimação do token.
     * 
     * @param token token que será usado na query para atualizar as informações do usuário.
     * @return número do registro afetado pela atualização no banco de dados.
     */
    @Override
    public int setConfirmadoAs(String token) {
        return repositorio.updateConfirmadoAs(token, LocalDateTime.now());
    }   
}