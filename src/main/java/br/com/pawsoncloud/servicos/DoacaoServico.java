package br.com.pawsoncloud.servicos;

import java.util.List;

import br.com.pawsoncloud.dtos.DoacaoDto;
import br.com.pawsoncloud.dtos.DoacaoUpdateDto;
import br.com.pawsoncloud.entidades.Doacao;

/**
 * A interface {@link DoacaoServico} possui a assinatura dos metodos que devem ser implementados.
 * 
 * @author Edielson Assis
 */
public interface DoacaoServico {
    
    /**
     * Cria uma doação e salva no banco de dados.
     * @param doacaoDto doação criada.
     * @return doação recém-criada.
     */
    Doacao create(DoacaoDto doacaoDto);

    /**
     * Retorna todas as adoções realizadas pelo usuário com base no seu cpf.
     * @return uma lista com todas as doações realizadas pelo usuário.
     */
    List<Doacao> findByCpf();

    /**
     * Atualiza uma doação de acordo com o id informado.
     * @param id parâmetro para atualização.
     * @param doacaoDto doação que será atualizada. 
     * @return doação recém-atualizada.
     */
    Doacao update(Long id, DoacaoUpdateDto doacaoDto);

    /**
     * Deleta uma doação de acordo com id informado.
     * @param id parâmetro para cancelamento de doação.
     */
    void delete(Long id);
}