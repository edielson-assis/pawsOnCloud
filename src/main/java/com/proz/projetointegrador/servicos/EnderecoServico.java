package com.proz.projetointegrador.servicos;

import com.proz.projetointegrador.dto.EnderecoDto;
import com.proz.projetointegrador.entidades.Endereco;

public interface EnderecoServico {
    
    Endereco create(EnderecoDto enderecoDto);

    Endereco findById(Long id);
}