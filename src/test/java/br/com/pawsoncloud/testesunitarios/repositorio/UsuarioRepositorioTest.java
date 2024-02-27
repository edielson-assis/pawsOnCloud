package br.com.pawsoncloud.testesunitarios.repositorio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.pawsoncloud.entidades.Endereco;
import br.com.pawsoncloud.entidades.NivelAcesso;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.repositorios.UsuarioRepositorio;
import br.com.pawsoncloud.testcontainers.AbstractIntegrationTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuarioRepositorioTest extends AbstractIntegrationTest {
    
    @Autowired
    private UsuarioRepositorio repositorio;

    private Usuario usuario;

    private static final String EMAIL = "edielson@email.com";
    private static final String CPF = "411.727.360-49";

    @BeforeEach
    void setup() {
        Endereco endereco = new Endereco(null, "Rua dos sonhos, 1000", null, "Salvador", "BA");
        usuario = new Usuario(null, "Edielson", "edielson@email.com", "123456", LocalDate.of(1987, 04, 23), "411.727.360-49", "(71) 98888-7777", endereco, NivelAcesso.getInstance(), false);
    }

    @Test
    @DisplayName("Deve salvar um usuario e retornar um objeto do tipo usuario")
    void testDeveSalvarUmUsuarioERetornarUmObjetoDoTipoUsuario() {

        // Given / Arrange & When / Act
        Usuario usuarioSalvo = repositorio.save(usuario);

        // Then / Assert
        assertNotNull(usuarioSalvo);
        assertTrue(usuarioSalvo.getId() > 0);
        assertEquals("Edielson", usuarioSalvo.getNome());
        assertEquals("Salvador", usuarioSalvo.getEndereco().getCidade());
    }

    @Test
    @DisplayName("Deve retornar um usuario pelo CPF")
    void testDeveRetornarUmUsuarioPeloCpf() {

        // Given / Arrange
        repositorio.save(usuario);

        // When / Act
        Usuario usuarioSalvo = repositorio.findByCpf(CPF).get();

        // Then / Assert
        assertNotNull(usuarioSalvo);
        assertTrue(usuarioSalvo.getId() > 0);
        assertEquals(EMAIL, usuarioSalvo.getEmail());
        assertEquals("BA", usuarioSalvo.getEndereco().getEstado());
    }

    @Test
    @DisplayName("Deve atualizar um usuario e retornar um usuario atualizado")
    void testDeveAtualizarUmUsuarioERetornarUmUsuarioAtualizado() {

        // Given / Arrange
        repositorio.save(usuario);

        // When / Act
        Usuario usuarioSalvo = repositorio.findByCpf(CPF).get();
        usuarioSalvo.setNome("Carlos");
        usuarioSalvo.setTelefone("(71) 95555-4444");

        Usuario usuarioAtualizado = repositorio.save(usuarioSalvo);

        // Then / Assert
        assertNotNull(usuarioAtualizado);
        assertEquals("Carlos", usuarioAtualizado.getNome());
        assertEquals("(71) 95555-4444", usuarioAtualizado.getTelefone());
    }

    @Test
    @DisplayName("Deve deletar um usuario")
    void testDeveDeletarUmUsuario() {

        // Given / Arrange
        repositorio.save(usuario);

        // When / Act
        repositorio.deleteById(usuario.getId());

        Optional<Usuario> usuarioDeletado = repositorio.findByCpf(CPF);

        // Then / Assert
        assertEquals(Optional.empty(), usuarioDeletado);
        assertTrue(usuarioDeletado.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar um objeto do tipo UserDetails pelo email")
    void testDeveRetornarUmObjetoDoTipoUserDetailsPeloEmail() {

        // Given / Arrange
        repositorio.save(usuario);

        // When / Act
        UserDetails userDetails =  repositorio.findByEmail(EMAIL);

        // Then / Assert
        assertNotNull(userDetails);
        assertEquals(EMAIL, userDetails.getUsername());
        assertFalse(userDetails.isEnabled());
    }

    @Test
    @DisplayName("Deve atualizar o atributo ativo pelo email e retornar um numero de registro")
    void testDeveAtualizarOAtributoAtivoPeloEmailERetornarUmNumeroDeRegistro() {

        // Given / Arrange
        repositorio.save(usuario);

        // When / Act
        int usuarioSalvo = repositorio.ativarUsuario(EMAIL);

        // Then / Assert
        assertEquals(1, usuarioSalvo);
    }

    @Test
    @DisplayName("Deve retornar true se o email existir no banco de dados")
    void testDeveRetornarTrueSeOEmailExistirNoBancoDeDados() {

        // Given / Arrange
        repositorio.save(usuario);

        // When / Act
        boolean usuarioSalvo = repositorio.existsByEmail(EMAIL);

        // Then / Assert
        assertTrue(usuarioSalvo);
    }

    @Test
    @DisplayName("Deve retornar true se o CPF existir no banco de dados")
    void testDeveRetornarTrueSeOCpfExistirNoBancoDeDados() {

        // Given / Arrange
        repositorio.save(usuario);

        // When / Act
        boolean usuarioSalvo = repositorio.existsByCpf(CPF);

        // Then / Assert
        assertTrue(usuarioSalvo);
    }
}