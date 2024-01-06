package br.com.pawsoncloud.servicos.excecoes;

public class ObjectNotFoundException extends RuntimeException {
    
    public ObjectNotFoundException(String msg) {
        super(msg);
    }
}