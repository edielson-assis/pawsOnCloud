package br.com.pawsoncloud.servicos.impl;

import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.mysql.cj.exceptions.DataReadException;

import br.com.pawsoncloud.dtos.AdocaoDto;
import br.com.pawsoncloud.dtos.AdocaoUpdateDto;
import br.com.pawsoncloud.entidades.Adocao;
import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.entidades.enums.StatusAdocao;
import br.com.pawsoncloud.repositorios.AdocaoRepositorio;
import br.com.pawsoncloud.repositorios.AnimaisRepositorio;
import br.com.pawsoncloud.servicos.AdocaoServico;
import br.com.pawsoncloud.servicos.conversor.DadosAdocao;
import br.com.pawsoncloud.servicos.excecoes.DataBaseException;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdocaoServicoImpl implements AdocaoServico {

    private final AdocaoRepositorio repositorio;
    private final AnimaisRepositorio animaisRepositorio;

    @Override
    public Adocao create(AdocaoDto adocaoDto) {
        Animais pet = getPetOrThrow(adocaoDto.petId());
        if (!isDisponivel(pet)) {
            throw new DataBaseException("Pet em pocesso de adoção");
        } else if (isDoador(pet)) {
            throw new DataBaseException("Você não pode adotar o próprio pet");
        } else if (hasAdocaoPendente(UsuarioLogado.getUsuario())) {
            throw new DataReadException("Você possui uma adoção pendente");
        } else {
            setStatusProcessoAdocao(pet);
            Adocao adocao = DadosAdocao.getAdocao(adocaoDto);
            return repositorio.save(adocao);
        }
    }

    @Override
    public List<Adocao> findByCpf() {
        var adocoes = repositorio.findByUsuarioCpf(UsuarioLogado.getUsuario().getCpf());
        if (adocoes.isEmpty()) {
            throw new ObjectNotFoundException("Você não possui nenhuma adoção");
        }
        return adocoes;
    }

    @Override
    public Adocao update(Long id, AdocaoUpdateDto adocaoDto) {
        Adocao adocao = getAdocaoReferenceOrThrow(id);
        if (!adocao.getAdotante().equals(UsuarioLogado.getUsuario())) {
            throw new BadCredentialsException("Não autorizado");
        } else if (adocao.getPet().isAdotado()) {
            throw new DataBaseException("Adoção já finalizada");
        } else {
            DadosAdocao.getAdocaoAtualizada(adocao, adocaoDto);
            return repositorio.save(adocao);
        }
    }

    @Override
    public void delete(Long id) {
        Adocao adocao = getAdocaoReferenceOrThrow(id);
        if (!adocao.getAdotante().equals(UsuarioLogado.getUsuario())) {
            throw new BadCredentialsException("Não autorizado");
        } else if (adocao.getPet().isAdotado()) {
            throw new DataBaseException("Adoção já finalizada");
        } else {
            setStatusDisponivel(adocao.getPet());
            repositorio.deleteById(id);
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

    private void setStatusProcessoAdocao(Animais pet) {
        pet.setStatus(StatusAdocao.PROCESSO_ADOCAO);
        animaisRepositorio.save(pet);
    }

    private void setStatusDisponivel(Animais pet) {
        pet.setStatus(StatusAdocao.DISPONIVEL);
        animaisRepositorio.save(pet);
    }

    private Adocao getAdocaoReferenceOrThrow(Long id) {
        try {
            return repositorio.getReferenceById(id);
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundException("Adoção não encontrada. Id inválido: " + id);
        }
    }

    private boolean hasAdocaoPendente(Usuario usuario) {
        List<Adocao> adocoes = repositorio.findByUsuarioCpf(usuario.getCpf());
        return adocoes.stream().anyMatch(pet -> pet.getPet().getStatus().equals(StatusAdocao.PROCESSO_ADOCAO));
    }
}