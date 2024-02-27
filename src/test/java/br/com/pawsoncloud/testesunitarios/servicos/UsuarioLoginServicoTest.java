package br.com.pawsoncloud.testesunitarios.servicos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.pawsoncloud.repositorios.UsuarioRepositorio;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;
import br.com.pawsoncloud.servicos.impl.UsuarioLoginServicoImpl;

@ExtendWith(MockitoExtension.class)
class UsuarioLoginServicoTest {
    
    @Mock
    private UsuarioRepositorio repositorio;

    @Mock
    private UserDetails userdetails;

    @InjectMocks
    private UsuarioLoginServicoImpl servico;

    @Test
    @DisplayName("Deve retornar um objeto do tipo UserDetails com base no email informado")
    void testDeveRetornarUmObjetoDoTipoUserDetailsComBaseNoEmailInformado() {

        // Given / Arrenge
        given(repositorio.findByEmail(anyString())).willReturn(userdetails);

        // When / Act
        UserDetails usuario = servico.loadUserByUsername(anyString());

        // Then / Assert
        verify(repositorio, times(1)).findByEmail(anyString());
        assertNotNull(usuario);
    }

    @Test
    @DisplayName("Deve retornar uma ObjectNotFoundException se o usuario nao for encontrado")
    void testDeveRetornarUmaObjectNotFoundExceptionSeOUsuarioNaoForEncontrado() {

        // Given / Arrenge
        given(repositorio.findByEmail(anyString())).willThrow(new ObjectNotFoundException("Usuário não encontrado"));

        // When / Act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> servico.loadUserByUsername(anyString()));

        // Then / Assert
        verify(repositorio, times(1)).findByEmail(anyString());
        assertEquals("Usuário não encontrado", exception.getMessage());
    }
}