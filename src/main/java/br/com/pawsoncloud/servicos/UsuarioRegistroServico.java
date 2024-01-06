package br.com.pawsoncloud.servicos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.pawsoncloud.dto.UsuarioDto;
import br.com.pawsoncloud.dto.UsuarioRespDto;
import br.com.pawsoncloud.dto.UsuarioUpdateDto;
import br.com.pawsoncloud.entidades.Usuario;

public interface UsuarioRegistroServico {
    
    Usuario create(UsuarioDto usuarioDto);

    Page<UsuarioRespDto> findAll(Pageable pageable);

    Usuario findById(Long id);

    Usuario update(Long id, UsuarioUpdateDto usuarioDto);

    void delete(Long id);
}