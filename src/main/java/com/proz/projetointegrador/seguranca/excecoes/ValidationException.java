package com.proz.projetointegrador.seguranca.excecoes;

public class ValidationException extends RuntimeException {
    
    public ValidationException(String msg) {
        super(msg);
    }
}