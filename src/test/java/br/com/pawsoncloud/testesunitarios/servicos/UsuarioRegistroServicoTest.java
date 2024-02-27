package br.com.pawsoncloud.testesunitarios.servicos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.pawsoncloud.dtos.EnderecoDto;
import br.com.pawsoncloud.dtos.UsuarioDto;
import br.com.pawsoncloud.dtos.UsuarioUpdateDto;
import br.com.pawsoncloud.entidades.TokenEmail;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.repositorios.UsuarioRepositorio;
import br.com.pawsoncloud.seguranca.excecoes.ValidationException;
import br.com.pawsoncloud.servicos.EmailServico;
import br.com.pawsoncloud.servicos.TokenEmailServico;
import br.com.pawsoncloud.servicos.conversor.DadosUsuario;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;
import br.com.pawsoncloud.servicos.impl.UsuarioRegistroServicoImpl;

@ExtendWith(MockitoExtension.class)
class UsuarioRegistroServicoTest {

    @Mock
    private UsuarioRepositorio repositorio;

    @Mock
    private TokenEmailServico tokenEmailServico;

    @Mock
    private EmailServico emailServico;

    @Mock
    private EnderecoDto endereco;

    @InjectMocks
    private UsuarioRegistroServicoImpl servico;

    private Usuario usuario;
    private UsuarioDto usuarioDto;

    @BeforeEach
    void setup() {
        usuarioDto = new UsuarioDto("Edielson", "edielson@email.com", "123456", LocalDate.of(1987, 04, 23), "411.727.360-49", "(71) 98888-7777", endereco);
        usuario = DadosUsuario.getUsuario(usuarioDto);
        autenticaUsuario(usuario);
    }

    @Test
    @DisplayName("Quando salvar um usuario deve retornar um objeto do tipo usuario")
    void testQuandoSalvarUmUsuarioDeveRetornarUmObjetoDoTipoUsuario() {

        // Given / Arrenge
        given(repositorio.existsByEmail(anyString())).willReturn(false);
        given(repositorio.existsByCpf(anyString())).willReturn(false);
        given(repositorio.save(usuario)).willReturn(usuario);

        // When / Act
        Usuario usuarioSalvo = servico.create(usuarioDto);

        // Then / Assert
        assertNotNull(usuarioSalvo);
        assertEquals("Edielson", usuarioSalvo.getNome());
    }

    @Test
    @DisplayName("Deve retornar uma ValidationException se o email informado ja estiver cadastrado")
    void testDeveRetornarUmaValidationExceptionSeOEmailInformadoJaEstiverCadastrado() {

        // Given / Arrenge
        given(repositorio.existsByEmail(anyString())).willReturn(true);

        // When / Act
        assertThrows(ValidationException.class, () -> servico.create(usuarioDto));

        // Then / Assert
        verify(repositorio, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve retornar uma ValidationException se o CPF informado ja estiver cadastrado")
    void testDeveRetornarUmaValidationExceptionSeOCPFInformadoJaEstiverCadastrado() {

        // Given / Arrenge
        given(repositorio.existsByCpf(anyString())).willReturn(true);

        // When / Act
        ValidationException validationException = assertThrows(ValidationException.class, () -> servico.create(usuarioDto));

        // Then / Assert
        verify(repositorio, times(0)).save(any(Usuario.class));
        assertEquals("CPF já cadastrado", validationException.getMessage());
    }

    @Test
    @DisplayName("Deve retornar um usuario com base no CPF informado")
    void testDeveRetornarUmUsuarioComBaseNoCPFInformado() {

        // Given / Arrenge
        given(repositorio.findByCpf(anyString())).willReturn(Optional.of(usuario));

        // When / Act
        Usuario usuarioSalvo = servico.findByCpf();

        // Then / Assert
        verify(repositorio, times(1)).findByCpf(anyString());
        assertNotNull(usuarioSalvo);
        assertEquals("edielson@email.com", usuarioSalvo.getEmail());
    }

    @Test
    @DisplayName("Deve retornar um ObjectNotFoundException se o usuario nao for encontrado")   
    void testDeveRetornarUmObjectNotFoundExceptionSeOUsuarioNaoForEncontrado() {

        // Given / Arrenge
        given(repositorio.findByCpf(anyString())).willReturn(Optional.empty());

        //  When / Act
        assertThrows(ObjectNotFoundException.class, () -> servico.findByCpf());

        // Then / Assert
        verify(repositorio, atLeast(1)).findByCpf(anyString());
    }

    @Test
    @DisplayName("Deve atualizar os dados de um usuario autenticado")
    void testDeveAtualizarOsDadosDeUmUsuarioAutenticado() {

        // Given / Arrenge
        given(repositorio.findByCpf(anyString())).willReturn(Optional.of(usuario));
        given(repositorio.save(usuario)).willReturn(usuario);

        EnderecoDto enderecoDto = new EnderecoDto("Rua dos sonhos, 1000", "", "Salvador", "Bahia");
        UsuarioUpdateDto usuarioAtualizado = new UsuarioUpdateDto("Carlos", "12345678", "(71) 95555-6666", enderecoDto);

        // When / Act
        servico.update(usuarioAtualizado);

        // Then / Assert
        verify(repositorio, atLeast(1)).findByCpf(anyString());
        assertNotNull(usuario);
        assertEquals("Carlos", usuario.getNome());
        assertNotEquals("123456", usuario.getSenha());
        assertEquals("(71) 95555-6666", usuario.getTelefone());
    }

    @Test
    @DisplayName("Deve deletar um usuario com base no seu CPF")
    void testDeveDeletarUmUsuarioComBaseNoSeuCPF() {

        // Given / Arrenge
        given(repositorio.findByCpf(anyString())).willReturn(Optional.of(usuario));
        willDoNothing().given(repositorio).delete(usuario);

        // When / Act
        servico.delete();

        // Then / Assert
        verify(repositorio, times(1)).findByCpf(anyString());
        verify(repositorio, times(1)).delete(usuario);
    }

    @Test
    @DisplayName("Deve retornar uma mensagem informando que o email foi validado")
    void testDeveRetornarUmaMensagemDeValidacaoDoEmail() {

        // Given / Arrenge
        TokenEmail tokenEmail = new TokenEmail(token(), LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), usuario);
        
        given(tokenEmailServico.findByToken(anyString())).willReturn(Optional.of(tokenEmail));

        // When / Act
        String tokenConfirmacao = servico.confirmarToken(anyString());

        // Then / Assert
        verify(tokenEmailServico, times(1)).findByToken(anyString());
        assertEquals("Email validado com sucesso", tokenConfirmacao);    
    }

    @Test
    @DisplayName("Deve retornar uma IllegalStateException se o email já tiver sido validado")
    void testDeveRetornarUmaIllegalStateExceptionSeOEmailJaEstiverSidoValidado() {
        
        // Given / Arrange
        TokenEmail tokenEmail = new TokenEmail(token(), LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), usuario);
        
        given(tokenEmailServico.findByToken(anyString())).willReturn(Optional.of(tokenEmail)); 
        given(repositorio.ativarUsuario(anyString())).willThrow(new IllegalStateException("Email já confirmado"));

        // When / Act
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> servico.confirmarToken(token()));

        // Then / Assert
        verify(tokenEmailServico, times(1)).findByToken(anyString());
        assertEquals("Email já confirmado", illegalStateException.getMessage());     
    }

    @Test
    @DisplayName("Deve retornar uma IllegalStateException se o token ja estiver expirado")
    void testDeveRetornarUmaIllegalStateExceptionSeOTokenJaEstiverExpirado() {

        // Given / Arrenge
        TokenEmail tokenEmail = new TokenEmail(token(), LocalDateTime.now(), LocalDateTime.now().minusMinutes(15), usuario);
        
        given(tokenEmailServico.findByToken(anyString())).willReturn(Optional.of(tokenEmail));

        // When / Act
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> servico.confirmarToken(token()));

        // Then / Assert
        verify(tokenEmailServico, times(1)).findByToken(anyString());
        assertEquals("Token expirado", illegalStateException.getMessage());    
    }

    private String token() {
        return UUID.randomUUID().toString();
    }

    private void autenticaUsuario(Usuario usuario) {
        var usuarioAutenticado = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usuarioAutenticado);
    }
}