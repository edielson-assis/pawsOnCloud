package br.com.pawsoncloud.seguranca;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.pawsoncloud.entidades.Usuario;

/**
 * Serviço responsável pela manipulação de tokens JWT (JSON Web Token) relacionados à autenticação.
 * 
 * @author Edielson Assis
 */
@Service
public class TokenService {

    /**
     * Chave secreta usada para assinar e verificar tokens JWT.
     */
    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Gera um token JWT para o usuário fornecido.
     * 
     * @param usuario O usuário para o qual o token será gerado.
     * @return Uma string representando o token JWT gerado.
     * @throws SecurityException Se houver um erro durante a geração do token.
     */
    public String generateToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API PawsOnCloud")
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(exprirationToken())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new SecurityException("Erro ao gerar token jwt", exception);
        }
    }

    /**
     * Obtém os detalhes do token JWT.
     * 
     * @param tokenJWT token que será verificado.
     * @return O token JWT.
     * @throws SecurityException Se o token JWT for inválido ou expirado.
     */
    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("API PawsOnCloud")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new SecurityException("Token JWT inválido ou expirado!");
        }
    }

    /**
     * Calcula o tempo de expiração do token JWT, configurado para 2 horas a partir do momento atual.
     * 
     * @return O instante de expiração do token.
     */
    private Instant exprirationToken() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}