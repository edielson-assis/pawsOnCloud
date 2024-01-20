package br.com.pawsoncloud.servicos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.pawsoncloud.dtos.AnimaisResponseDto;
import br.com.pawsoncloud.entidades.Animais;

/**
 * A interface {@link AnimaisServico} possui a assinatura dos metodos que devem ser implementados.
 * 
 * @author Edielson Assis
 */
public interface AnimaisServico {

    /**
     * Retorna uma lista paginada de animais com base nas configurações do objeto Pageable.
     * @param pageable objeto que deve ser configurado.
     * @return uma lista com todos animais que estão salvas no DB.
     */
    Page<AnimaisResponseDto> findAll(Pageable pageable);

    /**
     * Retorna um pet específico com base no ID fornecido.
     * @param id parâmetro para localizar o pet.
     * @return Pet
     */
    Animais findById(Long id);
}