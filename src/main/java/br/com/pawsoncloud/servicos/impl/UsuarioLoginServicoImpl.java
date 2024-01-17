package br.com.pawsoncloud.servicos.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.pawsoncloud.repositorios.UsuarioRepositorio;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioLoginServicoImpl implements UserDetailsService {

    private final UsuarioRepositorio repositorio;

    @Override
    public UserDetails loadUserByUsername(String email) {
        try {
            return repositorio.findByEmail(email);   
        } catch (UsernameNotFoundException e) {
            throw new ObjectNotFoundException("Usuário não encontrado");
        }    
    } 
}