package br.com.pawsoncloud.servicos.impl;

import java.nio.file.AccessDeniedException;

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
        if (isDisponivel(pet) && !isDoador(pet)) {
            atualizarStatus(pet);
            Adocao adocao = DadosAdocao.getAdocao(adocaoDto);
            return repositorio.save(adocao);
        } else {
            throw new AccessDeniedException("Pet indisponível");
        }
    }

    @Override
    public Adocao update(Long id, AdocaoUpdateDto adocaoDto) {
        Adocao adocao = getAdocaoReferenceOrThrow(id);
        if (adocao.getAdotante().equals(UsuarioLogado.getUsuario())) {
            DadosAdocao.getAdocaoAtualizada(adocao, adocaoDto);
            return repositorio.save(adocao);
        } else {
            throw new BadCredentialsException("Não autorizado");
        }
    }

    @Override
    public void delete(Long id) {
        Adocao adocao = getAdocaoReferenceOrThrow(id);
        if (adocao.getAdotante().equals(UsuarioLogado.getUsuario())) {
            repositorio.deleteById(id);
        } else {
            throw new BadCredentialsException("Não autorizado");
        }
    }

    private Animais getPetOrThrow(Long id) {
        return animaisRepositorio.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pet não encontrado. Id inválido: " + id));
    }

    private boolean isDisponivel(Animais pet) {
        return pet.getStatus().equals(StatusAdocao.DISPONIVEL);
    }

    private boolean isDoador(Animais pet) {
        return pet.getUsuario().equals(UsuarioLogado.getUsuario());
    }

    private void atualizarStatus(Animais pet) {
        pet.setStatus(StatusAdocao.PROCESSO_ADOCAO);
        animaisRepositorio.save(pet);
    }

    private Adocao getAdocaoReferenceOrThrow(Long id) {
        try {
            return repositorio.getReferenceById(id);
        } catch (EntityNotFoundException ex) {
            throw new ObjectNotFoundException("Adoção não encontrada. Id inválido: " + id);
        }
    }
}