package br.com.pawsoncloud.testesunitarios.servicos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.entidades.enums.StatusAdocao;
import br.com.pawsoncloud.repositorios.AdocaoRepositorio;
import br.com.pawsoncloud.repositorios.AnimaisRepositorio;
import br.com.pawsoncloud.repositorios.DoacaoRepositorio;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;
import br.com.pawsoncloud.servicos.impl.AnimaisServicoImpl;

@ExtendWith(MockitoExtension.class)
class AnimaisServicoTest {

    @Mock
    private AnimaisRepositorio animaisRepositorio;

    @Mock
    private DoacaoRepositorio doacaoRepositorio;

    @Mock
    private AdocaoRepositorio adocaoRepositorio;

    @InjectMocks
    private AnimaisServicoImpl servico;

    @Mock
    private Usuario usuario;

    private Animais pet;
    private Animais pet1;

    @BeforeEach
    void setup() {
        pet = new Animais(1L, "Bella", "medio", 3, "show-show", "marron", 20.0, "", StatusAdocao.DISPONIVEL, usuario, false);
        pet1 = new Animais(2L, "Thor", "grande", 3, "pitbull", "preto", 25.0, "", StatusAdocao.DISPONIVEL, usuario, false);
    }

    @Test
    @DisplayName("Deve retornar uma animal especifico de acordo com o ID informado")
    void testDeveRetornarUmAnimalEspecificoDeAcordoComOIdInformado() {

        // Given / Arrange
        given(animaisRepositorio.findByAdotadoFalse(anyLong())).willReturn(Optional.of(pet));

        // When / Act
        Animais petSalvo = servico.findById(1L);

        // Then / Assert
        assertNotNull(petSalvo);
        assertNotNull(petSalvo.getUsuario());
        assertEquals("Bella", petSalvo.getNome());
    }

    @Test
    @DisplayName("Deve retornar uma ObjectNotFoundException, se o ID for invalido")
    void testDeveRetornarUmaObjectNotFoundExceptionSeOIdForInvalido() {

        // Given / Arrange
        given(animaisRepositorio.findByAdotadoFalse(anyLong())).willReturn(Optional.empty());
        var mensagemEsperada = "Pet não encontrado. Id inválido: " + pet.getId();

        // When / Act
        ObjectNotFoundException excecao = assertThrows(ObjectNotFoundException.class, () -> servico.findById(pet.getId()));

        // Then / Assert
        assertEquals(mensagemEsperada, excecao.getMessage());
    }

    @Test
    @DisplayName("Deve retornar uma list de animais")
    void testDeveRetornarUmaListaDeAnimais() {

        // Given / Arrange
        Pageable pageable = mock(Pageable.class);
        given(animaisRepositorio.findAllByAdotadoFalse(pageable)).willReturn(new PageImpl<>(List.of(pet, pet1)));

        // When / Act
        Page<Animais> animais = animaisRepositorio.findAllByAdotadoFalse(pageable);

        // Then / Assert
        verify(animaisRepositorio).findAllByAdotadoFalse(any(Pageable.class));
        
        assertNotNull(animais);
        assertEquals(2, animais.getSize());
        assertTrue(animais.stream().anyMatch(x -> x.getEspecie().equals("pitbull")));
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia, se nenhum animal for encontrado")
    void testDeveRetornarListaVaziaSeNenhumAnimalForEncontrado() {

        // Given / Arrange
        Pageable pageable = mock(Pageable.class);
        given(animaisRepositorio.findAllByAdotadoFalse(pageable)).willReturn(new PageImpl<>(List.of()));

        // When / Act
        Page<Animais> animais = animaisRepositorio.findAllByAdotadoFalse(pageable);

        // Then / Assert
        verify(animaisRepositorio).findAllByAdotadoFalse(any(Pageable.class));
        
        assertTrue(animais.isEmpty());
		assertEquals(0, animais.getSize());
    }
}