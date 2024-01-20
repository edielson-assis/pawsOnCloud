package br.com.pawsoncloud.servicos.conversor;

import br.com.pawsoncloud.dtos.EnderecoDto;
import br.com.pawsoncloud.entidades.Endereco;

/**
 * Classe responsável por realizar a conversão dos DTOs de endereço.
 * 
 * @author Edielson Assis
 */
public class DadosEndereco {
    
    /** 
     * Cria um endereço e o mapeia com os dados do dto.
     * 
     * @param enderecoDto contém os dados do endereço que será criado.
     * @return Endereco
     */
    public static Endereco getEndereco(EnderecoDto enderecoDto) {
        return new Endereco(null, enderecoDto.logradouro(),
        enderecoDto.complemento(),
        enderecoDto.cidade(),
        enderecoDto.estado());
    }

    /**
     * Atualiza os dados do objeto.
     * 
     * @param endereo objeto que será atualizado.
     * @param enderecoDto contém os dados de atualização do endereço.
     * @return Endereco
     */
    public static Endereco getEnderecoAtualizado(Endereco endereo, EnderecoDto enderecoDto) {
        endereo.setLogradouro(enderecoDto.logradouro());
        if (!enderecoDto.complemento().isBlank()) {
            endereo.setComplemento(enderecoDto.complemento());
        }
        endereo.setCidade(enderecoDto.cidade());
        endereo.setEstado(enderecoDto.estado());
        return endereo;
    }
}