package br.com.pawsoncloud.servicos;

import br.com.pawsoncloud.dtos.UsuarioDto;
import br.com.pawsoncloud.dtos.UsuarioUpdateDto;
import br.com.pawsoncloud.entidades.Usuario;

public interface UsuarioRegistroServico {
    
    Usuario create(UsuarioDto usuarioDto);

    String confirmarToken(String token);

    Usuario findByCpf();

    Usuario update(UsuarioUpdateDto usuarioDto);

    void delete();
}