package br.com.pawsoncloud.servicos.impl;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.pawsoncloud.dto.UsuarioDto;
import br.com.pawsoncloud.dto.UsuarioRespDto;
import br.com.pawsoncloud.dto.UsuarioUpdateDto;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.repositorios.UsuarioRepositorio;
import br.com.pawsoncloud.servicos.UsuarioRegistroServico;
import br.com.pawsoncloud.servicos.conversor.DadosUsuario;
import br.com.pawsoncloud.servicos.excecoes.DataBaseException;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;

import jakarta.validation.ValidationException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioRegistroServicoImpl implements UsuarioRegistroServico {

    private UsuarioRepositorio repositorio;

    @Override
    public Usuario create(UsuarioDto usuarioDto) {
        Usuario usuario = DadosUsuario.getUsuario(usuarioDto);
        existsByEmail(usuario);
        existsByCpf(usuario);
        return repositorio.save(usuario);
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
        Usuario usuario = findById(id);
        DadosUsuario.getUsuarioAtualizado(usuario, usuarioUpdateDto);
        return repositorio.save(usuario);
    }

    @Override
    public void delete(Long id) {
        findById(id);
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
}