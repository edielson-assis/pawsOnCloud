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

@Service
@AllArgsConstructor
public class DoacaoServicoImpl implements DoacaoServico {

    private final DoacaoRepositorio repositorio;

    @Override
    public Doacao create(DoacaoDto doacaoDto) {
        Doacao doacao = DadosDoacao.getDoacao(doacaoDto);
        return repositorio.save(doacao);
    }

    @Override
    public List<Doacao> findByCpf() {
        var doacoes = repositorio.findByUsuarioCpf(UsuarioLogado.getUsuario().getCpf());
        if (doacoes.isEmpty()) {
            throw new ObjectNotFoundException("Você não possui nenhuma doação");
        }
        return doacoes; 
    }

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

    private Doacao getDoacaoReferencia(Long id) {
        try {
            return repositorio.getReferenceById(id);
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundException("Doação não encontrada. Id inválido: " + id);
        }
    }
}