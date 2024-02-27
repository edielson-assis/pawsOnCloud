package br.com.pawsoncloud.testesunitarios.servicos;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.pawsoncloud.entidades.TokenEmail;
import br.com.pawsoncloud.repositorios.TokenEmailRepositorio;
import br.com.pawsoncloud.servicos.impl.TokenEmailServicoImpl;

@ExtendWith(MockitoExtension.class)
class TokenEmailServicoTest {

    @Mock
    private TokenEmailRepositorio repositorio;

    @Mock
    private TokenEmail token;

    @InjectMocks
    private TokenEmailServicoImpl servico;

    @Test
    @DisplayName("Quando criar um token deve devolver um objeto do tipo token")
    void testQuandoCriarUmTokenDeveDevolverUmObjetoDoTipoToken() {

        // Given / Arrenge
        given(repositorio.save(token)).willReturn(token);

        // When / Act
        servico.createToken(token);

        // Then / Assert
        verify(repositorio, times(1)).save(token);
        verify(repositorio, atLeast(1)).save(any(TokenEmail.class));
    }

    @Test
    @DisplayName("Deve retornar um objeto do tipo TokenEmail com base no token informado")
    void testDeveRetornarUmObjetoDoTipoTokenEmailComBaseNoTokenInformado() {

        // Given / Arrenge
        given(repositorio.findByToken(anyString())).willReturn(Optional.of(token));

        // When / Act
        Optional<TokenEmail> tokenSalvo = servico.findByToken(anyString());

        // Then / Assert
        verify(repositorio, times(1)).findByToken(anyString());
        assertNotNull(tokenSalvo);
    }

    @Test
    @DisplayName("Deve atualizar o atributo confirmadoAs com base no token informado")
    void testDeveAtualizarOAtributoConfirmadoAsComBaseNoTokenInformado() {

        // Given / Arrenge
        given(repositorio.updateConfirmadoAs(anyString(), any())).willReturn(anyInt());

        // When / Act
        Integer confirmadoAs = servico.setConfirmadoAs(anyString());

        // Then / Assert
        verify(repositorio, atLeast(1)).updateConfirmadoAs(anyString(), any());
        assertNotNull(confirmadoAs);
    }
}