package br.com.pawsoncloud.controladores.excecao;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Estrutura da clsse que gerará as informações de erro.
 * 
 * @author Edielson Assis
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardError implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}