package com.proz.projetointegrador.servicos.conversor;

import com.proz.projetointegrador.dto.EnderecoDto;
import com.proz.projetointegrador.dto.EnderecoUpdateDto;
import com.proz.projetointegrador.entidades.Endereco;

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

    private static Endereco updateData(Endereco endereo, EnderecoUpdateDto enderecoUpdateDto) {
        endereo.setLogradouro(enderecoUpdateDto.logradouro());
        if (!enderecoUpdateDto.complemento().isBlank()) {
            endereo.setComplemento(enderecoUpdateDto.complemento());
        }
        endereo.setCidade(enderecoUpdateDto.cidade());
        endereo.setEstado(enderecoUpdateDto.estado());
        return endereo;
    }

    public static Endereco getEnderecoAtualizado(Endereco endereo, EnderecoUpdateDto enderecoUpdateDto) {
        return updateData(endereo, enderecoUpdateDto);
    }
}