package br.com.pawsoncloud.servicos.conversor;

import br.com.pawsoncloud.dto.EnderecoDto;
import br.com.pawsoncloud.entidades.Endereco;

public class DadosEndereco {
    
    private static Endereco fromDto(EnderecoDto enderecoDto) {
        return new Endereco(null, enderecoDto.logradouro(),
        enderecoDto.complemento(),
        enderecoDto.cidade(),
        enderecoDto.estado());
    }

    public static Endereco getEndereco(EnderecoDto enderecoDto) {
        return fromDto(enderecoDto);
    }

    private static Endereco updateData(Endereco endereo, EnderecoDto enderecoDto) {
        endereo.setLogradouro(enderecoDto.logradouro());
        if (!enderecoDto.complemento().isBlank()) {
            endereo.setComplemento(enderecoDto.complemento());
        }
        endereo.setCidade(enderecoDto.cidade());
        endereo.setEstado(enderecoDto.estado());
        return endereo;
    }

    public static Endereco getEnderecoAtualizado(Endereco endereo, EnderecoDto enderecoDto) {
        return updateData(endereo, enderecoDto);
    }
}