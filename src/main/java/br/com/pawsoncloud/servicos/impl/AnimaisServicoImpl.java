package br.com.pawsoncloud.servicos.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.pawsoncloud.dto.AnimaisResponseDto;
import br.com.pawsoncloud.entidades.Adocao;
import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.entidades.Doacao;
import br.com.pawsoncloud.entidades.enums.StatusAdocao;
import br.com.pawsoncloud.repositorios.AdocaoRepositorio;
import br.com.pawsoncloud.repositorios.AnimaisRepositorio;
import br.com.pawsoncloud.repositorios.DoacaoRepositorio;
import br.com.pawsoncloud.servicos.AnimaisServico;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AnimaisServicoImpl implements AnimaisServico {

    private AnimaisRepositorio animaisRepositorio;
    private DoacaoRepositorio doacaoRepositorio;
    private AdocaoRepositorio adocaoRepositorio;

    @Override
    public Page<AnimaisResponseDto> findAll(Pageable pageable) {
        Page<Animais> animais = animaisRepositorio.findAllByAdotadoFalse(pageable);
        animalAdotado();
        return animais.map(AnimaisResponseDto::new);
    }

    @Override
    public Animais findById(Long id) {
        Optional<Animais> pet = animaisRepositorio.findByAdotadoFalse(id);
        return pet.orElseThrow(() -> new ObjectNotFoundException("Pet não encontrado. Id inválido: " + id));
    }

    private void animalAdotado() {
        List<Adocao> adocoesConfirmadas = adocaoRepositorio.findByConfirmarAdocaoTrue();
        List<Doacao> doacoesConfirmadas = doacaoRepositorio.findByConfirmarDoacaoTrue();

        Map<Long, Adocao> adocoesMap = adocoesConfirmadas.stream()
                .collect(Collectors.toMap(adocao -> adocao.getPet().getId(), Function.identity()));

        Map<Long, Doacao> doacoesMap = doacoesConfirmadas.stream()
                .collect(Collectors.toMap(doacao -> doacao.getPet().getId(), Function.identity()));

        for (Long animalId : adocoesMap.keySet()) {
            if (doacoesMap.containsKey(animalId)) {
                Animais pet = adocoesMap.get(animalId).getPet();

                pet.setAdotado(true);
                pet.setStatus(StatusAdocao.ADOTADO);
                animaisRepositorio.save(pet);

                doacoesMap.get(animalId).getDataDoacao();
                doacaoRepositorio.save(doacoesMap.get(animalId));

                adocoesMap.get(animalId).getDataAdocao();
                adocaoRepositorio.save(adocoesMap.get(animalId));
            }
        }
    }
}