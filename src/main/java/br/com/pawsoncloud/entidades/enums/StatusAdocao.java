package br.com.pawsoncloud.entidades.enums;

/**
 * Enum que representa o status de adoção do pet na tabela animais.
 * 
 * @author Edielson Assis
 */
public enum StatusAdocao {

    /**
     * Retorna o status: DISPONIVEL
     */
    DISPONIVEL(1),

    /**
     * Retorna o status: PROCESSO_ADOCAO
     */
    PROCESSO_ADOCAO(2),

    /**
     * Retorna o status: ADOTADO
     */
    ADOTADO(3);

    private int code;

    private StatusAdocao(int code) {
        this.code = code;
    }

    /**
     * Retorna o código associado ao status.
     * 
     * @return número do código.
     */
    public int getCode() {
        return code;
    }

    /**
     * Retorna o status de adoção de acordo o código informado.
     * @param code código associado ao status.
     * @return StatusAdocao
     * @exception IllegalArgumentException é lançada caso o código for inválido.
     */
    public static StatusAdocao valueOf(int code) {
        for  (StatusAdocao value : StatusAdocao.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("O código do status de adoção é inválido");
    }
}