package br.com.pawsoncloud.servicos.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.pawsoncloud.dtos.AnimaisResponseDto;
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

/**
 * Classe que implementa a interface <b>DoacaoServico</b>.
 * 
 * @author Edielson Assis
 */
@Service
@AllArgsConstructor
public class AnimaisServicoImpl implements AnimaisServico {

    private final AnimaisRepositorio animaisRepositorio;
    private final DoacaoRepositorio doacaoRepositorio;
    private final AdocaoRepositorio adocaoRepositorio;

    /** 
     * Retorna uma lista paginada de animais com base nas configurações do objeto Pageable.
     * @param pageable objeto que deve ser configurado para determinar a order em que os animais serão exibidos.
     * @return Lista de animais.
     */
    @Override
    public Page<AnimaisResponseDto> findAll(Pageable pageable) {
        animalAdotado();
        Page<Animais> animais = animaisRepositorio.findAllByAdotadoFalse(pageable);
        return animais.map(AnimaisResponseDto::new);
    }

    /**
     * Retorna um pet com base no id fornecido. Caso o id seja inválido, uma exceção é lançada.
     * 
     * @param id id do animail pesquisado.
     * @exception ObjectNotFoundException é lançada caso o pet não seja encontrado.
     */
    @Override
    public Animais findById(Long id) {
        animalAdotado();
        Optional<Animais> pet = animaisRepositorio.findByAdotadoFalse(id);
        return pet.orElseThrow(() -> new ObjectNotFoundException("Pet não encontrado. Id inválido: " + id));
    }
    // Verifica se o adotante e o doador confirmaram a adoção e atualiza o status do animal para adotado.
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