package com.proz.projetointegrador.servicos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.proz.projetointegrador.dto.UsuarioDto;
import com.proz.projetointegrador.dto.UsuarioRespDto;
import com.proz.projetointegrador.dto.UsuarioUpdateDto;
import com.proz.projetointegrador.entidades.Usuario;

public interface UsuarioRegistroServico {
    
    Usuario create(UsuarioDto usuarioDto);

    Page<UsuarioRespDto> findAll(Pageable pageable);

    Usuario findById(Long id);

    Usuario update(Long id, UsuarioUpdateDto usuarioDto);

    void delete(Long id);
}