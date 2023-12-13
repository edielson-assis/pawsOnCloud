package com.proz.projetointegrador.servicos.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.proz.projetointegrador.dto.UsuarioDto;
import com.proz.projetointegrador.entidades.NivelAcesso;
import com.proz.projetointegrador.entidades.Usuario;
import com.proz.projetointegrador.repositorios.UsuarioRepositorio;
import com.proz.projetointegrador.servicos.UsuarioRegistroServico;
import com.proz.projetointegrador.servicos.excecoes.DataBaseException;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioRegistroServicoImpl implements UsuarioRegistroServico {

    private UsuarioRepositorio repositorio;

    @Override
    public Usuario create(UsuarioDto usuarioDto) {
        Usuario usuario = fromDto(usuarioDto);
        existsByEmail(usuario);
        existsByCpf(usuario);
        return repositorio.save(usuario);
    }

    @Override
    public void delete(Long id) {
        try {
            repositorio.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }
    
    private Usuario fromDto(UsuarioDto usuarioDto) {
        NivelAcesso nivelAcesso = new NivelAcesso();
        nivelAcesso.setId(1L);
        nivelAcesso.setNome("ROLE_USER");
        return new Usuario(null, usuarioDto.nome(), usuarioDto.email(), usuarioDto.senha(), usuarioDto.dataNascimento(), usuarioDto.cpf(), usuarioDto.telefone(), usuarioDto.endereco(), nivelAcesso);
    }
    
    private synchronized void existsByEmail(Usuario usuario) {
        boolean existeEmail = repositorio.existsByEmail(usuario.getEmail());
        if (existeEmail) {
            throw new ValidationException("Email já cadastrado");
        }
    }

    private synchronized void existsByCpf(Usuario usuario) {
        boolean existeCpf = repositorio.existsByCpf(usuario.getCpf());
        if (existeCpf) {
            throw new ValidationException("CPF já cadastrado");
        }
    }
}