package br.com.pawsoncloud.controladores.excecao;

import java.nio.file.AccessDeniedException;
import java.time.Instant;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.pawsoncloud.seguranca.excecoes.ValidationException;
import br.com.pawsoncloud.servicos.excecoes.DataBaseException;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Classe que lida com exceções globais em controllers e fornece respostas padronizadas.
 * 
 * <p>Esta classe utiliza a anotação {@code @ControllerAdvice} para tratar exceções
 * em diferentes controllers.</p>
 * 
 * @author Edielson Assis
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    
    /**
     * Trata a exceção {@code ObjectNotFoundException} e retorna uma resposta padronizada.
     * 
     * @param e A exceção lançada.
     * @param request O objeto de requisição HTTP.
     * @return Uma resposta {@code ResponseEntity} com detalhes do erro.
     */
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ObjectNotFoundException e, HttpServletRequest request) {
        String error = "Não encontrado";
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    /**
     * Trata a exceção {@code EntityNotFoundException} e retorna uma resposta padronizada.
     * 
     * @param e A exceção lançada.
     * @param request O objeto de requisição HTTP.
     * @return Uma resposta {@code ResponseEntity} com detalhes do erro.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(EntityNotFoundException e, HttpServletRequest request) {
        String error = "Não encontrado";
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    /**
     * Trata a exceção {@code MethodArgumentNotValidException} e retorna uma resposta padronizada.
     * 
     * @param e A exceção lançada.
     * @param request O objeto de requisição HTTP.
     * @return Uma resposta {@code ResponseEntity} com detalhes do erro.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validationError(MethodArgumentNotValidException e, HttpServletRequest request) {
        String error = "Erro na validação";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    /**
     * Trata a exceção {@code HttpMessageNotReadableException} e retorna uma resposta padronizada.
     * 
     * @param e A exceção lançada.
     * @param request O objeto de requisição HTTP.
     * @return Uma resposta {@code ResponseEntity} com detalhes do erro.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> badRequest(HttpMessageNotReadableException e, HttpServletRequest request) {
        String error = "Solicitação inválida";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    /**
     * Trata a exceção {@code ValidationException} e retorna uma resposta padronizada.
     * 
     * @param e A exceção lançada.
     * @param request O objeto de requisição HTTP.
     * @return Uma resposta {@code ResponseEntity} com detalhes do erro.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<StandardError> businessException(ValidationException e, HttpServletRequest request) {
        String error = "Erro na validação";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    /**
     * Trata a exceção {@code BadCredentialsException} e retorna uma resposta padronizada.
     * 
     * @param e A exceção lançada.
     * @param request O objeto de requisição HTTP.
     * @return Uma resposta {@code ResponseEntity} com detalhes do erro.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardError> badCredentialsError(BadCredentialsException e, HttpServletRequest request) {
        String error = "Credenciais inválidas";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    /**
     * Trata a exceção {@code AuthenticationException} e retorna uma resposta padronizada.
     * 
     * @param e A exceção lançada.
     * @param request O objeto de requisição HTTP.
     * @return Uma resposta {@code ResponseEntity} com detalhes do erro.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<StandardError> authenticationError(AuthenticationException e, HttpServletRequest request) {
        String error = "Falha na autenticação";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    /**
     * Trata a exceção {@code AccessDeniedException} e retorna uma resposta padronizada.
     * 
     * @param e A exceção lançada.
     * @param request O objeto de requisição HTTP.
     * @return Uma resposta {@code ResponseEntity} com detalhes do erro.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> accessDeniedError(AccessDeniedException e, HttpServletRequest request) {
        String error = "Acesso negado";
        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    /**
     * Trata a exceção {@code JWTVerificationException} e retorna uma resposta padronizada.
     * 
     * @param e A exceção lançada.
     * @param request O objeto de requisição HTTP.
     * @return Uma resposta {@code ResponseEntity} com detalhes do erro.
     */
    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<StandardError> jwtError(SecurityException e, HttpServletRequest request) {
        String error = "Acesso negado";
        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    /**
     * Trata a exceção {@code DataIntegrityViolationException} e retorna uma resposta padronizada.
     * 
     * @param e A exceção lançada.
     * @param request O objeto de requisição HTTP.
     * @return Uma resposta {@code ResponseEntity} com detalhes do erro.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> databaseError(DataBaseException e, HttpServletRequest request) {
        String error = "Erro no servidor";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    /**
     * Trata a exceção {@code Exception} e retorna uma resposta padronizada.
     * 
     * @param e A exceção lançada.
     * @param request O objeto de requisição HTTP.
     * @return Uma resposta {@code ResponseEntity} com detalhes do erro.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> databaseError(Exception e, HttpServletRequest request) {
        String error = "Conflito";
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    /**
     * Constrói um objeto {@code StandardError} com informações do erro.
     * 
     * @param status O código de status HTTP.
     * @param error A descrição do erro.
     * @param message A mensagem de erro específica.
     * @param request O objeto de requisição HTTP.
     * @return Um objeto {@code StandardError} com informações do erro.
     */
    private StandardError errors(HttpStatus status, String error, Exception message, HttpServletRequest request) {
        return new StandardError(Instant.now(), status.value(), error, message.getMessage(), request.getRequestURI());
    }
}