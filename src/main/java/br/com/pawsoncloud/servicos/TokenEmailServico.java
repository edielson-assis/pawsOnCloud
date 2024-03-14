package br.com.pawsoncloud.servicos;

import java.util.Optional;

import br.com.pawsoncloud.entidades.TokenEmail;

/**
 * A interface {@link TokenEmailServico} possui a assinatura dos metodos que devem ser implementados.
 * 
 * @author Edielson Assis
 */
public interface TokenEmailServico {
    
    /**
     * Cria um token e salva no banco de dados.
     * @param token token que será criado.
     */
    void createToken(TokenEmail token);

    /**
     * Retorna um Optional contendo o token.
     * @param token token de validação.
     * @return token.
     */
    Optional<TokenEmail> findByToken(String token);

    /**
     * Atualiza a data de confirmação do token.
     * @param token token de validaçao.
     * @return número do registro afetado pela atualização no banco de dados.
     */
    int setConfirmadoAs(String token);

    /**
     * Deleta o token associado ao usuário.
     * @param id id do usuário associado ao token.
     */
    void deleteTokenByUsuarioId(Long id);
}