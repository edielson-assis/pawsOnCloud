package br.com.pawsoncloud.testesunitarios.controladores;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pawsoncloud.PawsOnCloudApplication;
import br.com.pawsoncloud.controladores.UsuarioLoginControle;
import br.com.pawsoncloud.dtos.UsuarioAutenticado;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.repositorios.UsuarioRepositorio;
import br.com.pawsoncloud.seguranca.SecurityConfiguration;
import br.com.pawsoncloud.seguranca.TokenService;

@ContextConfiguration(classes={PawsOnCloudApplication.class, SecurityConfiguration.class})
@WebMvcTest(UsuarioLoginControle.class)
class UsuarioLoginControleTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; 

    @MockBean
    private TokenService tokenService;

    @MockBean
    private AuthenticationManager manager;

    @MockBean
    private UsuarioRepositorio repositorio;

    private UsuarioAutenticado usuario;

    private static final String TOKEN_JWT = "jwt_token";
    private static final String LOGIN_URI = "/api/v1/login";

    @BeforeEach
    void setup() {
        usuario = new UsuarioAutenticado("test@example.com", "password");
    }

    @Test
    @DisplayName("^Deve fazer login, retornar status code 200 e devolver um token JWT")
    void testDeveFazerLoginEDevolverUmTokenJwt() throws JsonProcessingException, Exception {
        
        // Given / Arrange    
        given(manager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(new UsernamePasswordAuthenticationToken(new Usuario(), null));
        given(tokenService.generateToken(any(Usuario.class))).willReturn(TOKEN_JWT);
    
        // When / Assert
        ResultActions resposta = mockMvc.perform(post(LOGIN_URI).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)));

        // Then / Assert
        resposta.andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("jwt_token"));
    }

    @Test
    @DisplayName("^Deve retornar status code 401 quando as credenciais forem invalidas")
    void testDeveRetornarStatusCode401QuandoAsCredenciaisForemInvalidas() throws JsonProcessingException, Exception {
        
        // Given / Arrange    
        given(manager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willThrow(BadCredentialsException.class);
    
        // When / Assert
        ResultActions resposta = mockMvc.perform(post(LOGIN_URI).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UsuarioAutenticado("test@example.com", "12345"))));

        // Then / Assert
        resposta.andDo(print()).andExpect(status().isUnauthorized());
    }
}