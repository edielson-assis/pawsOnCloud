package br.com.pawsoncloud.servicos.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.pawsoncloud.repositorios.UsuarioRepositorio;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioLoginServicoImpl implements UserDetailsService {

    private UsuarioRepositorio repositorio;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repositorio.findByEmail(email);       
    } 
}