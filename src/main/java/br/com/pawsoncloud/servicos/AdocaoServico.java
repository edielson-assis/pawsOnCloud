package br.com.pawsoncloud.servicos;

import java.util.List;

import br.com.pawsoncloud.dtos.AdocaoDto;
import br.com.pawsoncloud.dtos.AdocaoUpdateDto;
import br.com.pawsoncloud.entidades.Adocao;

/**
 * A interface {@link AdocaoServico} possui a assinatura dos metodos que devem ser implementados.
 * 
 * @author Edielson Assis
 */
public interface AdocaoServico {
    
    /**
   * Cria uma adoção e salva no banco de dados.
   * @param adocaoDto adoção criada.
   * @return adoção recém-criada.
   */
    Adocao create(AdocaoDto adocaoDto);

    /**
   * Retorna todas as adoções realizadas pelo usuário com base no seu cpf.
   * @return uma lista com todas as doações realizadas pelo usuário.
   */
    List<Adocao> findByCpf();

    /**
   * Atualiza uma adoção de acordo com o id informado.
   * @param id parâmetro para atualização.
   * @param adocaoDto adoção que será atualizada.
   * @return adoção recém-atualizada.
   */
    Adocao update(Long id, AdocaoUpdateDto adocaoDto);

    /**
     * Deleta uma adoção de acordo com o id informado.
     * @param id parâmetro para cancelamento de adoção.
     */
    void delete(Long id);
}