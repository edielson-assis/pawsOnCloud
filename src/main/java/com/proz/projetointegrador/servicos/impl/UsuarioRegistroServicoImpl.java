package com.proz.projetointegrador.servicos.impl;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.proz.projetointegrador.dto.UsuarioDto;
import com.proz.projetointegrador.dto.UsuarioRespDto;
import com.proz.projetointegrador.dto.UsuarioUpdateDto;
import com.proz.projetointegrador.entidades.Usuario;
import com.proz.projetointegrador.repositorios.UsuarioRepositorio;
import com.proz.projetointegrador.servicos.EnderecoServico;
import com.proz.projetointegrador.servicos.UsuarioRegistroServico;
import com.proz.projetointegrador.servicos.excecoes.DataBaseException;
import com.proz.projetointegrador.servicos.excecoes.ObjectNotFoundException;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioRegistroServicoImpl implements UsuarioRegistroServico {

    private UsuarioRepositorio repositorio;
    private EnderecoServico enderecoServico;

    @Override
    public Usuario create(UsuarioDto usuarioDto) {
        Usuario usuario = new Usuario(usuarioDto);
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

    @Override
    public Page<UsuarioRespDto> findAll(Pageable pageable) {
        Page<Usuario> usuarios = repositorio.findAll(pageable);
        return usuarios.map(UsuarioRespDto::new);
    }

    @Override
    public Usuario findById(Long id) {
        Optional<Usuario> usuario = repositorio.findById(id);
        return usuario.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado. Id inválido: " + id));
    }

    @Override
    public Usuario update(Long id, UsuarioUpdateDto usuarioUpdateDto) {
        Usuario usuario = updateData(id, usuarioUpdateDto);
        return repositorio.save(usuario);
    }

    private Usuario updateData(Long id, UsuarioUpdateDto usuarioUpdateDto) {
        Usuario usuario = findById(id); 
        usuario.setNome(usuarioUpdateDto.nome());
        usuario.setEmail(usuarioUpdateDto.email());
        usuario.setSenha(usuarioUpdateDto.senha());
        usuario.setTelefone(usuarioUpdateDto.telefone());
        usuario.setEndereco(enderecoServico.update(id, usuarioUpdateDto.enderecoUpdateDto()));     
        return usuario;
    }
}