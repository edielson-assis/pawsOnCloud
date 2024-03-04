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

import java.time.LocalDate;
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
import br.com.pawsoncloud.controladores.DoacaoControle;
import br.com.pawsoncloud.dtos.DoacaoDto;
import br.com.pawsoncloud.dtos.DoacaoUpdateDto;
import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.entidades.Doacao;
import br.com.pawsoncloud.entidades.Endereco;
import br.com.pawsoncloud.entidades.NivelAcesso;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.entidades.enums.StatusAdocao;
import br.com.pawsoncloud.repositorios.UsuarioRepositorio;
import br.com.pawsoncloud.seguranca.SecurityConfiguration;
import br.com.pawsoncloud.seguranca.TokenService;
import br.com.pawsoncloud.servicos.DoacaoServico;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;

@ContextConfiguration(classes={PawsOnCloudApplication.class, SecurityConfiguration.class, TokenService.class})
@WebMvcTest(DoacaoControle.class)
class DoacaoControleTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; 

    @MockBean
    private DoacaoServico servico;

    @MockBean
    private UsuarioRepositorio repositorio; 

    private Doacao doacao;
    private Doacao doacao1;

    private static final String DOACAO_URI = "/api/v1/doacoes";

    @BeforeEach
    void setup() {
        Endereco endereco = new Endereco(null, null, null, "Salvador", "BA");
        Usuario usuario = new Usuario(2l, "Edielson", null, null, null, "411.727.360-49", null, endereco, NivelAcesso.getInstance(), true);
        Animais pet = new Animais(null, "Bella", "medio", 3, "show-show", "marron", 20.0, "https://imagem.com", StatusAdocao.DISPONIVEL, usuario, false);
        Animais pet1 = new Animais(null, "Thor", "grande", 3, "vira-lata", "marron", 20.0, "https://imagem.com", StatusAdocao.DISPONIVEL, usuario, false);
        doacao = new Doacao(1L, null, LocalDate.now(), pet, usuario, false);
        doacao1 = new Doacao(2L, null, LocalDate.now(), pet1, usuario, false);
    }

    @Test
    @WithMockUser
    @DisplayName("JUnit test - deve criar uma doacao, retornar status code 201 e um objeto do tipo DoacaoRespDto")
    void deveCriarUmaDoacaoERetornarUmObjetoDoTipoDoacaoRespDto() throws JsonProcessingException, Exception {

        // Given / Arrenge
        given(servico.create(any(DoacaoDto.class))).willReturn(doacao);

        // When / Act
        ResultActions resposta = mockMvc.perform(post(DOACAO_URI).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doacao)));

        // Then / Assert
        resposta.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.dataCadastramento", is(doacao.getDataCadastramento().toString())))
                .andExpect(jsonPath("$.pet.nome", is(doacao.getPet().getNome())))
                .andExpect(jsonPath("$.pet.proprietario.nome", is(doacao.getDoador().getNome())))
                .andExpect(jsonPath("$.pet.proprietario.endereco.cidade", is(doacao.getDoador().getEndereco().getCidade())))
                .andExpect(jsonPath("$.pet.proprietario.endereco.estado", is(doacao.getDoador().getEndereco().getEstado())));
    }

    @Test
    @WithMockUser
    @DisplayName("JUnit test - deve retornar uma lista com todas as doacoes do usuario e status code 200")
    void deveRetornarUmaListaComTodasAsDoacoesDoUsuario() throws JsonProcessingException, Exception {

        // Given / Arrenge
        List<Doacao> doacoes = Arrays.asList(doacao, doacao1);

        given(servico.findByCpf()).willReturn(doacoes);

        // When / Act
        ResultActions resposta = mockMvc.perform(get(DOACAO_URI));

        // Then / Assert
        resposta.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.size()", is(doacoes.size())));
    }

    @Test
    @WithMockUser
    @DisplayName("JUnit test - deve retornar status code 404 se o usuario nao possuir doacoes")
    void deveRetornarStatusCode404SeOUsuarioNaoPossuirDoacoes() throws JsonProcessingException, Exception {

        // Given / Arrenge
        given(servico.findByCpf()).willThrow(ObjectNotFoundException.class);

        // When / Act
        ResultActions resposta = mockMvc.perform(get(DOACAO_URI));

        // Then / Assert
        resposta.andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("JUnit test - deve atualizar uma doacao e retornar status code 204")
    void deveAtualizarUmaDoacaoERetornarStatusCode204() throws JsonProcessingException, Exception {

        // Given / Arrenge
        given(servico.update(anyLong(), any(DoacaoUpdateDto.class))).willReturn(doacao);

        // When / Act
        DoacaoUpdateDto doacaoAtualizada = new DoacaoUpdateDto(1L, true);

        ResultActions resposta = mockMvc.perform(patch(DOACAO_URI.concat("/{id}"), 1L).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doacaoAtualizada)));

        // Then / Assert
        resposta.andExpect(status().isNoContent()).andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("JUnit test - deve deletar uma doacao e retornar status code 204")
    void deveDeletarUmaDoacaoERetornarStatusCode204() throws JsonProcessingException, Exception {

        // Given / Arrenge
        willDoNothing().given(servico).delete(anyLong());

        // When / Act
        ResultActions resposta = mockMvc.perform(delete(DOACAO_URI.concat("/{id}"), 1L).contentType(MediaType.APPLICATION_JSON));

        // Then / Assert
        resposta.andExpect(status().isNoContent()).andDo(print());
    }
}