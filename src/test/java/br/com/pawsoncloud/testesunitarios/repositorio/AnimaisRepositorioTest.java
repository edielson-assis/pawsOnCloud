package br.com.pawsoncloud.testesunitarios.repositorio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.entidades.enums.StatusAdocao;
import br.com.pawsoncloud.repositorios.AnimaisRepositorio;
import br.com.pawsoncloud.testcontainers.AbstractIntegrationTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnimaisRepositorioTest extends AbstractIntegrationTest {
    
    @Autowired
    private AnimaisRepositorio repositorio;

    private Animais pet;
    private Animais pet1;

    @BeforeEach
    void setup() {
        pet = new Animais(null, "Bella", "medio", 3, "show-show", "marron", 20.0, "", StatusAdocao.DISPONIVEL, null, false);
        pet1 = new Animais(null, "Thor", "grande", 3, "pitbull", "preto", 25.0, "", StatusAdocao.DISPONIVEL, null, false);
    }
    
    @Test
    @DisplayName("Deve retornar uma lista paginada de animais")
    void testDeveRetornarUmaListaPaginadaDeAnimais() {

        // Given / Arrenge
        repositorio.saveAll(Arrays.asList(pet, pet1));

        // When / Act
        Page<Animais> animais = repositorio.findAll(Pageable.ofSize(2));

        // Then / Assert
        assertNotNull(animais);
        assertEquals(2, animais.getSize());
        assertTrue(animais.stream().anyMatch(x -> x.getPelagem().equals("preto")));
    }

    @Test
    @DisplayName("Deve retornar um animal pelo ID")
    void testDeveRetornarUmAnimalPeloId() {

        // Given / Arrenge
        repositorio.saveAll(Arrays.asList(pet, pet1));

        // When / Act
        Animais petSalvo = repositorio.findById(pet1.getId()).get();

        // Then / Assert
        assertNotNull(petSalvo);
        assertEquals("Thor", petSalvo.getNome());
        assertEquals("grande", petSalvo.getPorte());
        assertEquals("pitbull", petSalvo.getEspecie());
    }
}