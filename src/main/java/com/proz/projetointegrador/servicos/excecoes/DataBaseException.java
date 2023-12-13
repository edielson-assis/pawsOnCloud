package com.proz.projetointegrador.servicos.excecoes;

public class DataBaseException extends RuntimeException {
    
    public DataBaseException(String msg) {
        super(msg);
    }
}