package br.com.pawsoncloud.servicos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import br.com.pawsoncloud.servicos.impl.EmailServicoImpl;
import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
class EmailServicoTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailServicoImpl emailServico;

    @Test
    @DisplayName("Deve enviar um email com sucesso")
    void testDeveEnviarUmEmailComSucesso() {

        // Given / Arrange
        String para = "test@example.com";
        String email = "Esse e um email teste";

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When / Act
        emailServico.enviar(para, email);

        // Then / Assert
        verify(mailSender, times(1)).send(any(MimeMessage.class));
        verify(mailSender, times(1)).createMimeMessage();
    }

    @Test()
    @DisplayName("Deve lancar uma IllegalStateException se o endereco de email for vazio")
    void testDeveLancarUmaIllegalStateExceptionSeOEnderecoDeEmailForVazio() {
        
        // Given / Arrange
        String para = "";
        String email = "Esse e um email teste";

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When / Act
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> emailServico.enviar(para, email));

        // Then / Assert
        verify(mailSender, never()).send(any(MimeMessage.class));
        assertEquals("Falha ao enviar o email", exception.getMessage());
    }

    @Test()
    @DisplayName("Deve lancar uma IllegalArgumentException se o conteudo ou endereco de email forem nulos")
    void testDeveLancarUmaIllegalArgumentExceptionSeOConteudoOuEnderecoDeEmailForemNulos() {
        
        // Given / Arrange
        String para = null;
        String email = null;

        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // When / Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> emailServico.enviar(para, email));

        // Then / Assert
        verify(mailSender, never()).send(any(MimeMessage.class));
        assertEquals("O conteúdo ou endereço de email não pode ser nulo", exception.getMessage());
    }
}