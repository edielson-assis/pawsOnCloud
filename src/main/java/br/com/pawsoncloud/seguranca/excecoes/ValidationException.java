package br.com.pawsoncloud.seguranca.excecoes;

public class ValidationException extends RuntimeException {
    
    public ValidationException(String msg) {
        super(msg);
    }
}