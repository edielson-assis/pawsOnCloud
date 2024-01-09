package br.com.pawsoncloud.servicos;

import br.com.pawsoncloud.dto.UsuarioDto;
import br.com.pawsoncloud.dto.UsuarioUpdateDto;
import br.com.pawsoncloud.entidades.Usuario;

public interface UsuarioRegistroServico {
    
    Usuario create(UsuarioDto usuarioDto);

    Usuario findByCpf();

    Usuario update(UsuarioUpdateDto usuarioDto);

    void delete();
}