package br.com.pawsoncloud.testesunitarios.repositorio;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.pawsoncloud.entidades.Doacao;
import br.com.pawsoncloud.entidades.Endereco;
import br.com.pawsoncloud.entidades.NivelAcesso;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.repositorios.DoacaoRepositorio;
import br.com.pawsoncloud.repositorios.UsuarioRepositorio;
import br.com.pawsoncloud.testcontainers.AbstractIntegrationTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DoacaoRepositorioTest extends AbstractIntegrationTest {

    @Autowired
    private DoacaoRepositorio repositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    private Doacao doacao;
    private Doacao doacao2;

    private static final String CPF = "411.727.360-49";

    @BeforeEach
    void setup() {
        doacao = new Doacao(null, null, LocalDate.now(), null, getUsuario("Edielson", "edielson@email.com", "411.727.360-49"), false);
        doacao2 = new Doacao(null, null, LocalDate.now(), null, getUsuario("Carlos", "carlos@email.com", "782.476.740-09"), false);
    }

    @Test
    @DisplayName("Deve criar uma doacao com sucesso")
    void testDeveCriarUmaDoacaoComSucesso() {

        // Given / Arrange & When / Act
        Doacao doacaoSalva = repositorio.save(doacao);

        // Then / Assert
        assertNotNull(doacaoSalva);
        assertTrue(doacaoSalva.getId() > 0);
        assertEquals(LocalDate.now(), doacaoSalva.getDataCadastramento());
        assertFalse(doacaoSalva.isConfirmarDoacao());
    }

    @Test
    @DisplayName("Deve retornar uma lista de doacoes com base no CPF do usuario")
    void testDeveRetornarUmaListaDeDoacoesComBaseNoCpfDoUsuario() {

        // Given / Arrange 
        repositorio.saveAll(List.of(doacao, doacao2));

        // When / Act
        List<Doacao> doacoes = repositorio.findByUsuarioCpf(CPF);

        // Then / Assert
        assertNotNull(doacoes);
        assertEquals(1, doacoes.size());
    }

    @Test
    @DisplayName("Deve retornar uma lista com todas as doacoes confirmadas")
    void testDeveRetornarUmaListaComTodasAsDoacoesConfirmadas() {

        // Given / Arrange 
        doacao2.setConfirmarDoacao(true);
        repositorio.saveAll(List.of(doacao, doacao2));

        // When / Act
        List<Doacao> doacoes = repositorio.findByConfirmarDoacaoTrue();

        // Then / Assert
        assertNotNull(doacoes);
        assertEquals(1, doacoes.size());
        assertTrue(doacoes.stream().anyMatch(x -> x.isConfirmarDoacao()));
    }
    
    @Test
    @DisplayName("Deve atualizar uma doacao e retornar uma doacao atualizada")
    void testDeveAtualizarUmaDoacaoERetornarUmaDoacaoAtualizada() {

        // Given / Arrange 
        Doacao doacaoSalva = repositorio.save(doacao);

        // When / Act
        doacaoSalva.setConfirmarDoacao(true);
        Doacao doacaoAtualizada = repositorio.save(doacaoSalva);

        // Then / Assert
        assertNotNull(doacaoAtualizada);
        assertTrue(doacaoAtualizada.isConfirmarDoacao());
    }
    
    @Test
    @DisplayName("Deve deletar uma doacao")
    void testDeveDeletarUmaDoacao() {

        // Given / Arrange 
        repositorio.saveAll(Arrays.asList(doacao, doacao2));

        // When / Act
        repositorio.deleteById(doacao.getId());

        List<Doacao> doacaoDeletada = repositorio.findByUsuarioCpf(CPF);

        // Then / Assert
        assertEquals(Collections.emptyList(), doacaoDeletada);
        assertTrue(doacaoDeletada.isEmpty());
    }

    private Usuario getUsuario(String nome, String email, String cpf) {
        Endereco endereco = new Endereco(null, "Rua dos sonhos, 1000", null, "Salvador", "BA");
        Usuario usuario = new Usuario(null, nome, email, "123456", LocalDate.of(1987, 04, 23), cpf, "(71) 98888-7777", endereco, NivelAcesso.getInstance(), false);
        usuarioRepositorio.save(usuario);
        return usuario;
    }
}