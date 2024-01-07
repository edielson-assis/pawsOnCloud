package br.com.pawsoncloud.servicos.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import br.com.pawsoncloud.dto.AdocaoDto;
import br.com.pawsoncloud.dto.AdocaoUpdateDto;
import br.com.pawsoncloud.entidades.Adocao;
import br.com.pawsoncloud.repositorios.AdocaoRepositorio;
import br.com.pawsoncloud.repositorios.AnimaisRepositorio;
import br.com.pawsoncloud.servicos.AdocaoServico;
import br.com.pawsoncloud.servicos.conversor.DadosAdocao;
import br.com.pawsoncloud.servicos.conversor.UsuarioLogado;
import br.com.pawsoncloud.servicos.excecoes.DataBaseException;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;

import jakarta.persistence.EntityNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdocaoServicoImpl implements AdocaoServico {

    private AdocaoRepositorio repositorio;
    private AnimaisRepositorio animaisRepositorio;

    @Override
    public Adocao create(AdocaoDto adocaoDto) {
        Adocao adocao = DadosAdocao.getAdocao(adocaoDto);
        if (animaisRepositorio.existsById(adocao.getPet().getId())) {
            return repositorio.save(adocao);
        } else {
            throw new ObjectNotFoundException("Pet não encontrado. Id inválido: " + adocaoDto.petId());
        }
    }

    @Override
    public Adocao update(Long id, AdocaoUpdateDto adocaoDto) {
        try {
            Adocao adocao = repositorio.getReferenceById(id);
            if (adocao.getAdotante().equals(UsuarioLogado.getUsuario())) {
                DadosAdocao.getAdocaoAtualizada(adocao, adocaoDto);
                return repositorio.save(adocao);
            } else {
                throw new BadCredentialsException("Não autorizado");
            }
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundException("Adoção não encontrada. Id inválido: " + id);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Adocao adocao = repositorio.getReferenceById(id);
            if (adocao.getAdotante().equals(UsuarioLogado.getUsuario())) {
                repositorio.deleteById(id);
            } else {
                throw new BadCredentialsException("Não autorizado");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Adoção não encontrada. Id inválido: " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }    
}