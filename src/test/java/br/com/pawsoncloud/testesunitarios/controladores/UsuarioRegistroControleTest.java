package br.com.pawsoncloud.testesunitarios.controladores;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

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
import br.com.pawsoncloud.controladores.UsuarioRegistroControle;
import br.com.pawsoncloud.dtos.EnderecoDto;
import br.com.pawsoncloud.dtos.UsuarioDto;
import br.com.pawsoncloud.dtos.UsuarioUpdateDto;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.repositorios.UsuarioRepositorio;
import br.com.pawsoncloud.seguranca.SecurityConfiguration;
import br.com.pawsoncloud.seguranca.TokenService;
import br.com.pawsoncloud.servicos.UsuarioRegistroServico;
import br.com.pawsoncloud.servicos.conversor.DadosUsuario;

@ContextConfiguration(classes={PawsOnCloudApplication.class, SecurityConfiguration.class, TokenService.class})
@WebMvcTest(UsuarioRegistroControle.class)
class UsuarioRegistroControleTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; 

    @MockBean
    private UsuarioRegistroServico servico;

    @MockBean
    private UsuarioRepositorio repositorio; 

    private UsuarioDto usuarioDto;
    private EnderecoDto enderecoDto;

    private static final String USUARIO_URI = "/api/v1/usuario";

    @BeforeEach
    void setup() {
        enderecoDto = new EnderecoDto("Rua dos sonhos, 1000", null, "Salvador", "BA");
        usuarioDto = new UsuarioDto("Edielson", "edielson@email.com", "123456", LocalDate.of(1987, 04, 23), "411.727.360-49", "(71) 98888-7777", enderecoDto);
    }

    @Test
    @DisplayName("JUnit test - deve criar um usuario, retornar status code 201 e um objeto do tipo UsuarioResponseDto")
    void deveCriarUmUsuarioERetornarUmObjetoDoTipoUsuarioResponseDto() throws JsonProcessingException, Exception {

        // Given / Arrenge
        given(servico.create(any(UsuarioDto.class))).willAnswer((usuario) -> DadosUsuario.getUsuario(usuario.getArgument(0)));

        // When / Act
        ResultActions resposta = mockMvc.perform(post(USUARIO_URI.concat("/cadastro")).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDto)));

        // Then / Assert
        resposta.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is(usuarioDto.nome())))
                .andExpect(jsonPath("$.endereco.cidade", is(usuarioDto.endereco().cidade())))
                .andExpect(jsonPath("$.endereco.estado", is(usuarioDto.endereco().estado())));
    }

    @Test
    @WithMockUser
    @DisplayName("JUnit test - deve retornar status code 200 e um objeto do tipo UsuarioFullRespDto")
    void deveRetornarUmObjetoDoTipoUsuarioFullRespDto() throws JsonProcessingException, Exception {

        // Given / Arrenge
        given(servico.findByCpf()).willReturn(DadosUsuario.getUsuario(usuarioDto));

        // When / Act
        ResultActions resposta = mockMvc.perform(get(USUARIO_URI));

        // Then / Assert
        resposta.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.nome", is(usuarioDto.nome())))
                .andExpect(jsonPath("$.email", is(usuarioDto.email())))
                .andExpect(jsonPath("$.dataNascimento", is(usuarioDto.dataNascimento().toString())))
                .andExpect(jsonPath("$.cpf", is(usuarioDto.cpf())))
                .andExpect(jsonPath("$.telefone", is(usuarioDto.telefone())))
                .andExpect(jsonPath("$.endereco.logradouro", is(usuarioDto.endereco().logradouro())))
                .andExpect(jsonPath("$.endereco.cidade", is(usuarioDto.endereco().cidade())))
                .andExpect(jsonPath("$.endereco.estado", is(usuarioDto.endereco().estado())));
    }

    @Test
    @WithMockUser
    @DisplayName("JUnit test - deve atualizar um usuario e retornar status code 200")
    void deveAtualizarUmUsuarioERetornarStatusCode200() throws JsonProcessingException, Exception {
        
        // Given / Arrange
        Usuario usuarioAtualizado = DadosUsuario.getUsuario(usuarioDto);
        given(servico.update(any(UsuarioUpdateDto.class))).willReturn(usuarioAtualizado);

        UsuarioUpdateDto usuarioUpdateDto = new UsuarioUpdateDto("Maria", "654321", "(71) 93333-4444", enderecoDto);

        // When / Act
        ResultActions resposta = mockMvc.perform(put(USUARIO_URI).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioUpdateDto)));

        // Then / Assert
        resposta.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.nome", is(usuarioAtualizado.getNome())))
                .andExpect(jsonPath("$.telefone", is(usuarioAtualizado.getTelefone())))
                .andExpect(jsonPath("$.endereco.logradouro", is(usuarioAtualizado.getEndereco().getLogradouro())))
                .andExpect(jsonPath("$.endereco.cidade", is(usuarioAtualizado.getEndereco().getCidade())))
                .andExpect(jsonPath("$.endereco.estado", is(usuarioAtualizado.getEndereco().getEstado())));
    }

    @Test
    @WithMockUser
    @DisplayName("JUnit test - deve deletar um usuario e retornar status code 204")
    void deveDeletarUmUsuarioERetornarStatusCode204() throws JsonProcessingException, Exception {

        // Given / Arrenge
        willDoNothing().given(servico).delete();

        // When / Act
        ResultActions resposta = mockMvc.perform(delete(USUARIO_URI).contentType(MediaType.APPLICATION_JSON));

        // Then / Assert
        resposta.andExpect(status().isNoContent()).andDo(print());
    }

    @Test
    @DisplayName("JUnit test - deve validar o token de validacao do email")
    void deveValidarOTokenDeValidacaoDoEmail() throws Exception {
        
        // Given / Arrenge
        String token = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        given(servico.confirmarToken(anyString())).willReturn("Email validado com sucesso");

        // When / Act
        ResultActions resposta = mockMvc.perform(get(USUARIO_URI.concat("/confirmar")).param("token", token));

        // Then / Assert
        resposta.andExpect(status().isOk()).andDo(print())
                .andExpect(content().string("Email validado com sucesso"));
    }
}