package br.com.pawsoncloud.testesunitarios.controladores;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.pawsoncloud.PawsOnCloudApplication;
import br.com.pawsoncloud.controladores.AnimaisControle;
import br.com.pawsoncloud.dtos.AnimaisResponseDto;
import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.entidades.Endereco;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.entidades.enums.StatusAdocao;
import br.com.pawsoncloud.repositorios.UsuarioRepositorio;
import br.com.pawsoncloud.seguranca.SecurityConfiguration;
import br.com.pawsoncloud.seguranca.TokenService;
import br.com.pawsoncloud.servicos.AnimaisServico;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;

@ContextConfiguration(classes={PawsOnCloudApplication.class, SecurityConfiguration.class, TokenService.class})
@WebMvcTest(AnimaisControle.class)
class AnimaisControleTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimaisServico servico;

    @MockBean
    private UsuarioRepositorio usuarioRepositorio;

    private static final String ANIMAIS_URI = "/api/v1/animais";

    @Test
    @WithMockUser
    @DisplayName("Deve retornar uma lista paginada de animais e status code 200")
    void testDeveRetornarUmaListaPaginadaDeAnimais() throws JsonProcessingException, Exception {
        
        // Given / Arrange
        AnimaisResponseDto pet = new AnimaisResponseDto(1L, "Bella", "medio", 3, "show-show", "marron", 20.0, "", StatusAdocao.DISPONIVEL, null);
        AnimaisResponseDto pet1 = new AnimaisResponseDto(2L, "Thor", "grande", 3, "pitbull", "preto", 25.0, "", StatusAdocao.DISPONIVEL, null);

        Page<AnimaisResponseDto> animais = new PageImpl<>(List.of(pet, pet1), PageRequest.of(0, 10), 2);
        given(servico.findAll(any(Pageable.class))).willReturn(animais);

        // When / Act
        ResultActions response = mockMvc.perform(get(ANIMAIS_URI));

        // Then / Assert
        response.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.content.size()", is(animais.getContent().size())))
                .andExpect(jsonPath("$.content[0].id", is(animais.getContent().get(0).id().intValue())))
                .andExpect(jsonPath("$.content[0].nome", is(animais.getContent().get(0).nome())))
                .andExpect(jsonPath("$.content[1].id", is(animais.getContent().get(1).id().intValue())))
                .andExpect(jsonPath("$.content[1].nome", is(animais.getContent().get(1).nome())));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar um animal pelo ID e status code 200")
    void testDeveRetornarUmAnimalPeloId() throws JsonProcessingException, Exception {
        
        // Given / Arrange
        Usuario usuario = new Usuario(null, null, null, null, null, null, null, new Endereco(), null, false);
        Animais pet = new Animais(1L, "Bella", "medio", 3, "show-show", "marron", 20.0, "", StatusAdocao.DISPONIVEL, usuario, false);

        given(servico.findById(anyLong())).willReturn(pet);

        // When / Act
        ResultActions response = mockMvc.perform(get(ANIMAIS_URI.concat("/{id}"), 1L));

        // Then / Assert
        response.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.nome", is(pet.getNome())))
                .andExpect(jsonPath("$.porte", is(pet.getPorte())))
                .andExpect(jsonPath("$.idade", is(pet.getIdade())))
                .andExpect(jsonPath("$.especie", is(pet.getEspecie())))
                .andExpect(jsonPath("$.pelagem", is(pet.getPelagem())))
                .andExpect(jsonPath("$.peso", is(pet.getPeso())))
                .andExpect(jsonPath("$.status", is(pet.getStatus().toString())));
    }

    @Test
    @WithMockUser
    @DisplayName("JUnit test - deve retornar status code 404 se o ID for invalido")
    void deveRetornarStatusCode404SeOIdForInvalido() throws JsonProcessingException, Exception {

        // Given / Arrenge
        given(servico.findById(anyLong())).willThrow(ObjectNotFoundException.class);

        // When / Act
        ResultActions resposta = mockMvc.perform(get(ANIMAIS_URI.concat("/{id}"), 1L));

        // Then / Assert
        resposta.andExpect(status().isNotFound()).andDo(print());
    }
}