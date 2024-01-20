package br.com.pawsoncloud.servicos.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.pawsoncloud.repositorios.UsuarioRepositorio;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;
import lombok.AllArgsConstructor;

/**
 * Classe que implementa a interface <b>UserDetailsService</b>.
 * 
 * @author Edielson Assis
 */
@Service
@AllArgsConstructor
public class UsuarioLoginServicoImpl implements UserDetailsService {

    private final UsuarioRepositorio repositorio;
    
    /** 
     * O método loadUserByUsername da interface {@link UserDetailsService} é usado para carregar um usuário com base no nome de usuário. 
     * Ele retorna um objeto {@link UserDetails} que contém informações sobre o usuário, como nome de usuário, senha e autorizações. 
     * O método é usado pelo <b>DaoAuthenticationProvider</b> para carregar detalhes sobre o usuário durante a autenticação.
     * 
     * @param email utilizado para verificar os dados de autenticação do usuário.
     * @return UserDetails
     * @exception ObjectNotFoundException é lançada caso o usuário não seja encotrado.
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        try {
            return repositorio.findByEmail(email);   
        } catch (UsernameNotFoundException e) {
            throw new ObjectNotFoundException("Usuário não encontrado");
        }    
    } 
}