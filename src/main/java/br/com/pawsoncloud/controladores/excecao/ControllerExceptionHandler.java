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

import br.com.pawsoncloud.seguranca.excecoes.ValidationException;
import br.com.pawsoncloud.servicos.excecoes.DataBaseException;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
    
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ObjectNotFoundException e, HttpServletRequest request) {
        String error = "Não encontrado";
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(EntityNotFoundException e, HttpServletRequest request) {
        String error = "Não encontrado";
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validationError(MethodArgumentNotValidException e, HttpServletRequest request) {
        String error = "Erro na validação";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> badRequest(HttpMessageNotReadableException e, HttpServletRequest request) {
        String error = "Solicitação inválida";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<StandardError> businessException(ValidationException e, HttpServletRequest request) {
        String error = "Erro na validação";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardError> badCredentialsError(BadCredentialsException e, HttpServletRequest request) {
        String error = "Credenciais inválidas";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<StandardError> authenticationError(AuthenticationException e, HttpServletRequest request) {
        String error = "Falha na autenticação";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> accessDeniedError(AccessDeniedException e, HttpServletRequest request) {
        String error = "Acesso negado";
        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> databaseError(DataBaseException e, HttpServletRequest request) {
        String error = "Erro no servidor";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> databaseError(Exception e, HttpServletRequest request) {
        String error = "Conflito";
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(errors(status, error, e, request));
    }

    private StandardError errors(HttpStatus status, String error, Exception message, HttpServletRequest request) {
        return new StandardError(Instant.now(), status.value(), error, message.getMessage(), request.getRequestURI());
    }
}