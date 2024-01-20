package br.com.pawsoncloud.servicos;

import br.com.pawsoncloud.dtos.UsuarioDto;
import br.com.pawsoncloud.dtos.UsuarioUpdateDto;
import br.com.pawsoncloud.entidades.Usuario;

/**
 * A interface {@link UsuarioRegistroServico} possui a assinatura dos metodos que devem ser implementados.
 * 
 * @author Edielson Assis
 */
public interface UsuarioRegistroServico {
    
    /**
     * Cria um usuário e salva no banco de dados.
     * 
     * @param usuarioDto usuário que será criado
     * @return Usuario
     */
    Usuario create(UsuarioDto usuarioDto);

    /**
     * Recebe um token como parâmetro e valida o email do usuário cadastrado.
     * 
     * @param token token de validação.
     * @return mensagem de confirmação
     */
    String confirmarToken(String token);

    /**
     * Retorna um usuário que está no banco de dados de acordo com o seu cpf.
     * 
     * @return Usuario
     */
    Usuario findByCpf();

    /**
     * Atualiza o usuário de acordo com o seu cpf.
     * 
     * @param usuarioDto usuário que será atualizado.
     * @return - usuário recém-atualizado.
     */
    Usuario update(UsuarioUpdateDto usuarioDto);

    /**
     * Deleta um usuário de acordo com o seu cpf.
     */
    void delete();
}