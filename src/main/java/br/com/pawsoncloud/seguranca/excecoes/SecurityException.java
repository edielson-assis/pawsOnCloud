package br.com.pawsoncloud.seguranca.excecoes;

public class SecurityException extends RuntimeException {
    
    public SecurityException(String msg) {
        super(msg);
    }
}