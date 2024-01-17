package br.com.pawsoncloud.servicos.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import br.com.pawsoncloud.servicos.EmailServico;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailServicoImpl implements EmailServico {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServicoImpl.class);
    private final JavaMailSender mailSender;
  
    @Async
    @Override
    public void enviar(String para, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(email, true);
            helper.setTo(para);
            helper.setSubject("Confirme o seu email");
            helper.setFrom("PawsOnCloud");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("Falha ao enviar o email ", e);
            throw new IllegalStateException("Falha ao enviar o email");
        }
    }
}