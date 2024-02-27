package br.com.pawsoncloud.testesunitarios.repositorio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.pawsoncloud.entidades.TokenEmail;
import br.com.pawsoncloud.repositorios.TokenEmailRepositorio;
import br.com.pawsoncloud.testcontainers.AbstractIntegrationTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TokenEmailRepositorioTest extends AbstractIntegrationTest {
    
    @Autowired
    private TokenEmailRepositorio repositorio;

    private TokenEmail tokenEmail;

    private static final String token = "f47ac10b-58cc-4372-a567-0e02b2c3d479";

    @BeforeEach
    void setup() {
        tokenEmail = new TokenEmail("f47ac10b-58cc-4372-a567-0e02b2c3d479", LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), null);
    }

    @Test
    @DisplayName("Deve criar um tokenEmail com sucesso")
    void testDeveCriarUmTokenEmailComSucesso() {

        // Given / Arrange & When / Act
        TokenEmail tokenCriado = repositorio.save(tokenEmail);

        // Then / Assert
        assertNotNull(tokenCriado);
        assertTrue(tokenCriado.getId() > 0);
        assertEquals(token, tokenCriado.getToken());
    }

    @Test
    @DisplayName("Deve retornar um objeto do tipo tokenEmail")
    void testDeveRetornarUmObjetoDoTipoTokenEmail() {

        // Given / Arrange 
        repositorio.save(tokenEmail);

        // When / Act
        TokenEmail tokenSalvo = repositorio.findByToken(token).get();

        // Then / Assert
        assertNotNull(tokenSalvo);
    }

    @Test
    @DisplayName("Deve atualizar o atributo confirmadoAs e retornar um numero de registro")
    void testDeveAtualizarOAtributoConfirmadoAsERetornarUmNumeroDeRegistro() {

        // Given / Arrange 
        repositorio.save(tokenEmail);

        // When / Act
        int tokenSalvo = repositorio.updateConfirmadoAs(token, LocalDateTime.now());

        // Then / Assert
        assertEquals(1, tokenSalvo);
    }
}