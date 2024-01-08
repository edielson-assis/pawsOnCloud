package br.com.pawsoncloud.servicos.impl;

import java.nio.file.AccessDeniedException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import br.com.pawsoncloud.dto.AdocaoDto;
import br.com.pawsoncloud.dto.AdocaoUpdateDto;
import br.com.pawsoncloud.entidades.Adocao;
import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.entidades.enums.StatusAdocao;
import br.com.pawsoncloud.repositorios.AdocaoRepositorio;
import br.com.pawsoncloud.repositorios.AnimaisRepositorio;
import br.com.pawsoncloud.servicos.AdocaoServico;
import br.com.pawsoncloud.servicos.conversor.DadosAdocao;
import br.com.pawsoncloud.servicos.conversor.UsuarioLogado;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;

import jakarta.persistence.EntityNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdocaoServicoImpl implements AdocaoServico {

    private AdocaoRepositorio repositorio;
    private AnimaisRepositorio animaisRepositorio;

    @Override
    public Adocao create(AdocaoDto adocaoDto) throws AccessDeniedException {
        Animais pet = getPetOrThrow(adocaoDto.petId());

        if (validarAdocao(adocaoDto)) {
            atualizarStatus(pet);
            Adocao adocao = DadosAdocao.getAdocao(adocaoDto);
            return repositorio.save(adocao);
        } else {
            throw new AccessDeniedException("Pet indisponível");
        }
    }

    @Override
    public Adocao update(Long id, AdocaoUpdateDto adocaoDto) {
        try {
            Adocao adocao = getAdocaoReferencia(id);
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
            Adocao adocao = getAdocaoReferencia(id);
            if (adocao.getAdotante().equals(UsuarioLogado.getUsuario())) {
                repositorio.deleteById(id);
            } else {
                throw new BadCredentialsException("Não autorizado");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Adoção não encontrada. Id inválido: " + id);
        }
    }

    private Animais getPetOrThrow(Long petId) {
        return animaisRepositorio.findById(petId)
                .orElseThrow(() -> new ObjectNotFoundException("Pet não encontrado. Id inválido: " + petId));
    }

    private boolean validarAdocao(AdocaoDto adocaoDto) {
        Animais pet = getPetReferenceOrThrow(adocaoDto.petId());
        return pet.getStatus().equals(StatusAdocao.DISPONIVEL);
    }
    
    private void atualizarStatus(Animais pet) {
        pet.setStatus(StatusAdocao.PROCESSO_ADOCAO);
        animaisRepositorio.save(pet);
    }
    
    private Animais getPetReferenceOrThrow(Long petId) {
        try {
            return animaisRepositorio.getReferenceById(petId);
        } catch (EntityNotFoundException ex) {
            throw new ObjectNotFoundException("Pet não encontrado. Id inválido: " + petId);
        }
    }    

    private Adocao getAdocaoReferencia(Long id) {
        return repositorio.getReferenceById(id);
    }
}