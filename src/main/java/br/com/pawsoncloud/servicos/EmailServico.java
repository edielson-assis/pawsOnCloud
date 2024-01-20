package br.com.pawsoncloud.servicos;

/**
 * A interface {@link EmailServico} possui a assinatura do metodo que deve ser implementado.
 * 
 * @author Edielson Assis
 */
public interface EmailServico {
    
    /**
     * Envia uma mensagem de validação de email para o email do usuário.
     * @param para destino para onde o email será enviado.
     * @param email mensagem que estará no corpo do email.
     */
    void enviar(String para, String email);
}