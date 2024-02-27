package br.com.pawsoncloud.testesunitarios.repositorio;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.pawsoncloud.entidades.Adocao;
import br.com.pawsoncloud.entidades.Endereco;
import br.com.pawsoncloud.entidades.NivelAcesso;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.repositorios.AdocaoRepositorio;
import br.com.pawsoncloud.repositorios.UsuarioRepositorio;
import br.com.pawsoncloud.testcontainers.AbstractIntegrationTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdocaoRepositorioTest extends AbstractIntegrationTest {
    
    @Autowired
    private AdocaoRepositorio repositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    private Adocao adocao;
    private Adocao adocao2;

    private static final String CPF = "411.727.360-49";

    @BeforeEach
    void setup() {
        adocao = new Adocao(null, LocalDate.now(), null, getUsuario("Edielson", "edielson@email.com", "411.727.360-49"), false);
        adocao2 = new Adocao(null, LocalDate.now(), null, getUsuario("Carlos", "carlos@email.com", "782.476.740-09"), false);
    }

    @Test
    @DisplayName("Deve criar uma adocao com sucesso")
    void testDeveCriarUmaAdocaoComSucesso() {

        // Given / Arrange & When / Act
        Adocao adocaoSalva = repositorio.save(adocao);

        // Then / Assert
        assertNotNull(adocaoSalva);
        assertTrue(adocaoSalva.getId() > 0);
        assertFalse(adocaoSalva.isConfirmarAdocao());
    }

    @Test
    @DisplayName("Deve retornar uma lista de doacoes com base no CPF do usuario")
    void testDeveRetornarUmaListaDeDoacoesComBaseNoCpfDoUsuario() {

        // Given / Arrange 
        repositorio.saveAll(List.of(adocao, adocao2));

        // When / Act
        List<Adocao> doacoes = repositorio.findByUsuarioCpf(CPF);

        // Then / Assert
        assertNotNull(doacoes);
        assertEquals(1, doacoes.size());
    }

    @Test
    @DisplayName("Deve retornar uma lista com todas as doacoes confirmadas")
    void testDeveRetornarUmaListaComTodasAsDoacoesConfirmadas() {

        // Given / Arrange 
        adocao2.setConfirmarAdocao(true);
        repositorio.saveAll(List.of(adocao, adocao2));

        // When / Act
        List<Adocao> doacoes = repositorio.findByConfirmarAdocaoTrue();

        // Then / Assert
        assertNotNull(doacoes);
        assertEquals(1, doacoes.size());
        assertTrue(doacoes.stream().anyMatch(x -> x.isConfirmarAdocao()));
    }
    
    @Test
    @DisplayName("Deve atualizar uma adocao e retornar uma adocao atualizada")
    void testDeveAtualizarUmaAdocaoERetornarUmaAdocaoAtualizada() {

        // Given / Arrange 
        Adocao adocaoSalva = repositorio.save(adocao);

        // When / Act
        adocaoSalva.setConfirmarAdocao(true);
        Adocao adocaoAtualizada = repositorio.save(adocaoSalva);

        // Then / Assert
        assertNotNull(adocaoAtualizada);
        assertTrue(adocaoAtualizada.isConfirmarAdocao());
    }
    
    @Test
    @DisplayName("Deve deletar uma adocao")
    void testDeveDeletarUmaAdocao() {

        // Given / Arrange 
        repositorio.saveAll(Arrays.asList(adocao, adocao2));

        // When / Act
        repositorio.deleteById(adocao.getId());

        List<Adocao> adocaoDeletada = repositorio.findByUsuarioCpf(CPF);

        // Then / Assert
        assertEquals(List.of(), adocaoDeletada);
        assertTrue(adocaoDeletada.isEmpty());
    }

    private Usuario getUsuario(String nome, String email, String cpf) {
        Endereco endereco = new Endereco(null, "Rua dos sonhos, 1000", null, "Salvador", "BA");
        Usuario usuario = new Usuario(null, nome, email, "123456", LocalDate.of(1987, 04, 23), cpf, "(71) 98888-7777", endereco, NivelAcesso.getInstance(), false);
        usuarioRepositorio.save(usuario);
        return usuario;
    }
}