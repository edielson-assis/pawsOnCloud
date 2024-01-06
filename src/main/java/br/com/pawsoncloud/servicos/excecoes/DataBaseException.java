package br.com.pawsoncloud.servicos.excecoes;

public class DataBaseException extends RuntimeException {
    
    public DataBaseException(String msg) {
        super(msg);
    }
}