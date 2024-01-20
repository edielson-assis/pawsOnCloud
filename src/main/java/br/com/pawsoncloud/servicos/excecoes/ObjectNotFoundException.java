package br.com.pawsoncloud.servicos.excecoes;

/**
 * Uma exceção personalizada que indica que um objeto específico não foi encontrado.
 * Esta exceção estende {@link RuntimeException} para permitir o tratamento flexível
 * (não exigindo declarações de throws ou try-catch).
 * 
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/RuntimeException.html">RuntimeException</a>
 * @author Edielson Assis
 */
public class ObjectNotFoundException extends RuntimeException {
    
    /**
     * Construtor que aceita uma mensagem que descreve a exceção.
     *
     * @param msg A mensagem explicativa associada a esta exceção.
     */
    public ObjectNotFoundException(String msg) {
        super(msg);
    }
}