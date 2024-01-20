package br.com.pawsoncloud.dtos;

import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.entidades.enums.StatusAdocao;

/**
 * Representa um objeto de transferência de dados (DTO) para informações de animais.
 * Este DTO é usado para fornecer uma versão simplificada de um animal para a resposta da API.
 * Retorna todos os dados do pet mais o nome do proprietário.
 * 
 * @author Edielson Assis
 */
public record AnimaisResponseDto(Long id, String nome, String porte, Integer idade, String especie, String pelagem,
        Double peso, String imgUrl, StatusAdocao status, UsuarioResponseDto proprietario) {

    /**
     * Construtor que converte uma instância de Animais para AnimaisResponseDto.
     *
     * @param animais A instância de Animais a ser convertida.
     */
    public AnimaisResponseDto(Animais animais) {
        this(animais.getId(), animais.getNome(), animais.getPorte(), animais.getIdade(), animais.getEspecie(),
                animais.getPelagem(), animais.getPeso(), animais.getImgUrl(), animais.getStatus(),
                new UsuarioResponseDto(animais.getUsuario()));
    }
}