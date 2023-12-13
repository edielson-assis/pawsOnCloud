package com.proz.projetointegrador.servicos;

import com.proz.projetointegrador.dto.UsuarioDto;
import com.proz.projetointegrador.entidades.Usuario;

public interface UsuarioRegistroServico {
    
    Usuario create(UsuarioDto usuarioDto);

    void delete(Long id);
}