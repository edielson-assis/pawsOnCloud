package com.proz.projetointegrador.entidades.enums;

public enum StatusAdocao {

    DISPONIVEL(1),
    PROCESSO_ADOCAO(2),
    ADOTADO(3);

    private int code;

    private StatusAdocao(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static StatusAdocao valueOf(int code) {
        for  (StatusAdocao value : StatusAdocao.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("O código do status de adoção é inválido");
    }
}