package br.com.pawsoncloud.servicos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.servicos.impl.UsuarioLogado;

class UsuarioLogadoTest {

    @Mock
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar um usuario autenticado")
    void testGetUsuarioDeveRetornarUsuarioAutenticado() {
        
        // Given / Arrenge
        autenticaUsuario(usuario);

        // When / Act 
        Usuario usuarioRetornado = UsuarioLogado.getUsuario();

        // Tehn / Assert
        assertEquals(usuario, usuarioRetornado);
        assertNotNull(usuarioRetornado);
    }

    @Test
    @DisplayName("Deve retornar NullPointerException se o usuario nao estiver autenticado")
    void testGetUsuarioDeveRetornarNullPointerExceptionQuandoNaoAutenticado() {

        // Given / Arrenge & When / Act 
        SecurityContextHolder.clearContext();
        
        // Tehn / Assert
        assertThrows(NullPointerException.class, () -> UsuarioLogado.getUsuario());
    }

    private void autenticaUsuario(Usuario usuario) {
        var usuarioAutenticado = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usuarioAutenticado);
    }
}