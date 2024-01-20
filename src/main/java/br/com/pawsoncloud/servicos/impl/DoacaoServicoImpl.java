package br.com.pawsoncloud.servicos.impl;

import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import br.com.pawsoncloud.dtos.DoacaoDto;
import br.com.pawsoncloud.dtos.DoacaoUpdateDto;
import br.com.pawsoncloud.entidades.Doacao;
import br.com.pawsoncloud.repositorios.DoacaoRepositorio;
import br.com.pawsoncloud.servicos.DoacaoServico;
import br.com.pawsoncloud.servicos.conversor.DadosDoacao;
import br.com.pawsoncloud.servicos.excecoes.DataBaseException;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

/**
 * Classe que implementa a interface <b>DoacaoServico</b>.
 * 
 * @author Edielson Assis
 */
@Service
@AllArgsConstructor
public class DoacaoServicoImpl implements DoacaoServico {

    private final DoacaoRepositorio repositorio;

    /**
     * Realizar uma doação. 
     * @param doacaoDto contém os dados do proprietário e do animal que será doado.
     * @return Doacao
     */
    @Override
    public Doacao create(DoacaoDto doacaoDto) {
        Doacao doacao = DadosDoacao.getDoacao(doacaoDto);
        return repositorio.save(doacao);
    }

    /**
     * Lista com todas as doações realizadas pelo usuário. 
     * É utilizado o cpf do usuário para carregar a lista.
     * Caso a lista esteja vazia, é lançado uma exceção.
     * 
     * @return lista de doações
     * @exception ObjectNotFoundException é lançada caso nenhuma doação seja encontrada.
     */
    @Override
    public List<Doacao> findByCpf() {
        var doacoes = repositorio.findByUsuarioCpf(UsuarioLogado.getUsuario().getCpf());
        if (doacoes.isEmpty()) {
            throw new ObjectNotFoundException("Você não possui nenhuma doação");
        }
        return doacoes; 
    }

    /**
     * Atualiza os dados da doação com base no id informado. Caso a doação não seja encontrada, é lançãdo uma exceção.
     * Antes de atualizar, verifica se o usuário logado possui os mesmos dados do doador. Se não, outra exceção é lançada.
     * Por fim, verifica se a doação já foi finalizada. Se sim, uma exceção é lançada, negando a operação.
     * 
     * @param doacaoDto doação que será atualizada.
     * @exception ObjectNotFoundException é lançada caso o usuário não seja encotrado.
     * @exception BadCredentialsException é lançada caso as credenciais do usuário sejam inválidas.
     * @exception DataBaseException é lançada caso a doação tenha sido finalizada.
     */
    @Override
    public Doacao update(Long id, DoacaoUpdateDto doacaoDto) {
        Doacao doacao = getDoacaoReferencia(id);
        if (!doacao.getDoador().equals(UsuarioLogado.getUsuario())) {
            throw new BadCredentialsException("Não autorizado");
        } else if (doacao.getPet().isAdotado()) {
            throw new DataBaseException("Doação já finalizada");
        } else {
            DadosDoacao.getDoacaoAtualizada(doacao, doacaoDto);
            return repositorio.save(doacao);
        }            
    }

    /**
     * Cancela uma doação com base no id informado. Caso a doação não seja encontrada, é lançãdo uma exceção.
     * Antes de cancelar, verifica se o usuário logado possui os mesmos dados do doador. Se não, outra exceção é lançada.
     * Por fim, verifica se a doação já foi finalizada. Se sim, uma exceção é lançada, negando a operação.
     * 
     * @param id id da doação que será cancelada.
     * @exception EntityNotFoundException capturada caso o usuário não seja encotrado.
     * @exception BadCredentialsException é lançada caso as credenciais do usuário sejam inválidas.
     * @exception DataBaseException é lançada caso a doação tenha sido finalizada.
     */
    @Override
    public void delete(Long id) {
        Doacao doacao = getDoacaoReferencia(id);
        if (!doacao.getDoador().equals(UsuarioLogado.getUsuario())) {
            throw new BadCredentialsException("Não autorizado");
        } else if (doacao.getPet().isAdotado()) {
            throw new DataBaseException("Doação já finalizada");
        } else {
            repositorio.deleteById(id);
        }
    }    
    
    /**
     * Pega a referência da doação no banco de dados pelo id informado
     * 
     * @param id utilizado para pegar a doação no banco de dados.
     * @return Doacao
     */
    private Doacao getDoacaoReferencia(Long id) {
        try {
            return repositorio.getReferenceById(id);
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundException("Doação não encontrada. Id inválido: " + id);
        }
    }
}