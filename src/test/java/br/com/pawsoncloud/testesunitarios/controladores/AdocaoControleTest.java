package br.com.pawsoncloud.testesunitarios.controladores;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pawsoncloud.PawsOnCloudApplication;
import br.com.pawsoncloud.controladores.AdocaoControle;
import br.com.pawsoncloud.dtos.AdocaoDto;
import br.com.pawsoncloud.dtos.AdocaoUpdateDto;
import br.com.pawsoncloud.entidades.Adocao;
import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.entidades.Endereco;
import br.com.pawsoncloud.entidades.NivelAcesso;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.entidades.enums.StatusAdocao;
import br.com.pawsoncloud.repositorios.UsuarioRepositorio;
import br.com.pawsoncloud.seguranca.SecurityConfiguration;
import br.com.pawsoncloud.seguranca.TokenService;
import br.com.pawsoncloud.servicos.AdocaoServico;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;

@ContextConfiguration(classes={PawsOnCloudApplication.class, SecurityConfiguration.class, TokenService.class})
@WebMvcTest(AdocaoControle.class)
class AdocaoControleTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; 

    @MockBean
    private AdocaoServico servico;

    @MockBean
    private UsuarioRepositorio repositorio; 

    private Adocao adocao;
    private Adocao adocao1;

    private static final String ADOCAO_URI = "/api/v1/adocoes";

    @BeforeEach
    void setup() {
        Endereco endereco = new Endereco(null, null, null, "Salvador", "BA");
        Usuario doador = new Usuario(1l, "Maria", null, null, null, "782.476.740-09", null, endereco, NivelAcesso.getInstance(), true);
        Usuario adotante = new Usuario(2l, "Edielson", null, null, null, "411.727.360-49", null, endereco, NivelAcesso.getInstance(), true);
        Animais pet = new Animais(1l, "Bella", "medio", 3, "show-show", "marron", 20.0, "https://imagem.com", StatusAdocao.DISPONIVEL, doador, false);
        Animais pet1 = new Animais(2l, "Thor", "grande", 3, "vira-lata", "marron", 20.0, "https://imagem.com", StatusAdocao.ADOTADO, doador, true);
        adocao = new Adocao(1L, null, pet, adotante, false);
        adocao1 = new Adocao(2L, null, pet1, adotante, true);
    }

    @Test
    @WithMockUser
    @DisplayName("JUnit test - deve criar uma adocao, retornar status code 201 e um objeto do tipo AdocaoRespDto")
    void deveCriarUmaAdocaoERetornarUmObjetoDoTipoAdocaoRespDto() throws JsonProcessingException, Exception {

        // Given / Arrenge
        given(servico.create(any(AdocaoDto.class))).willReturn(adocao);

        // When / Act
        ResultActions resposta = mockMvc.perform(post(ADOCAO_URI).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AdocaoDto(1l))));

        // Then / Assert
        resposta.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.petId", is(adocao.getPet().getId().intValue())))
                .andExpect(jsonPath("$.adotante.nome", is(adocao.getAdotante().getNome())))
                .andExpect(jsonPath("$.adotante.endereco.cidade", is(adocao.getAdotante().getEndereco().getCidade())))
                .andExpect(jsonPath("$.adotante.endereco.estado", is(adocao.getAdotante().getEndereco().getEstado())));
    }

    @Test
    @WithMockUser
    @DisplayName("JUnit test - deve retornar uma lista com todas as adocoes do usuario e status code 200")
    void deveRetornarUmaListaComTodasAsAdocoesDoUsuario() throws JsonProcessingException, Exception {

        // Given / Arrenge
        List<Adocao> adocoes = Arrays.asList(adocao, adocao1);

        given(servico.findByCpf()).willReturn(adocoes);

        // When / Act
        ResultActions resposta = mockMvc.perform(get(ADOCAO_URI));

        // Then / Assert
        resposta.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.size()", is(adocoes.size())));
    }

    @Test
    @WithMockUser
    @DisplayName("JUnit test - deve retornar status code 404 se o usuario nao possuir adocoes")
    void deveRetornarStatusCode404SeOUsuarioNaoPossuirAdocoes() throws JsonProcessingException, Exception {

        // Given / Arrenge
        given(servico.findByCpf()).willThrow(ObjectNotFoundException.class);

        // When / Act
        ResultActions resposta = mockMvc.perform(get(ADOCAO_URI));

        // Then / Assert
        resposta.andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("JUnit test - deve atualizar uma adocao e retornar status code 204")
    void deveAtualizarUmaAdocaoERetornarStatusCode204() throws JsonProcessingException, Exception {

        // Given / Arrenge
        given(servico.update(anyLong(), any(AdocaoUpdateDto.class))).willReturn(adocao);

        // When / Act
        AdocaoUpdateDto adocaoAtualizada = new AdocaoUpdateDto(1L, true);

        ResultActions resposta = mockMvc.perform(patch(ADOCAO_URI.concat("/{id}"), 1L).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adocaoAtualizada)));

        // Then / Assert
        resposta.andExpect(status().isNoContent()).andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("JUnit test - deve deletar uma adocao e retornar status code 204")
    void deveDeletarUmaAdocaoERetornarStatusCode204() throws JsonProcessingException, Exception {

        // Given / Arrenge
        willDoNothing().given(servico).delete(anyLong());

        // When / Act
        ResultActions resposta = mockMvc.perform(delete(ADOCAO_URI.concat("/{id}"), 1L).contentType(MediaType.APPLICATION_JSON));

        // Then / Assert
        resposta.andExpect(status().isNoContent()).andDo(print());
    }
}