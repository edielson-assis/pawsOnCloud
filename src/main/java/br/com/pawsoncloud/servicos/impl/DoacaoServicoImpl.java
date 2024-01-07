package br.com.pawsoncloud.servicos.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import br.com.pawsoncloud.dto.DoacaoDto;
import br.com.pawsoncloud.dto.DoacaoUpdateDto;
import br.com.pawsoncloud.entidades.Doacao;
import br.com.pawsoncloud.repositorios.DoacaoRepositorio;
import br.com.pawsoncloud.servicos.DoacaoServico;
import br.com.pawsoncloud.servicos.conversor.DadosDoacao;
import br.com.pawsoncloud.servicos.conversor.UsuarioLogado;
import br.com.pawsoncloud.servicos.excecoes.DataBaseException;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DoacaoServicoImpl implements DoacaoServico {

    private DoacaoRepositorio repositorio;

    @Override
    public Doacao create(DoacaoDto doacaoDto) {
        Doacao doacao = DadosDoacao.getDoacao(doacaoDto);
        return repositorio.save(doacao);
    }

    @Override
    public Doacao update(Long id, DoacaoUpdateDto doacaoDto) {
        try {
            Doacao doacao = repositorio.getReferenceById(id);
            if (doacao.getDoador().equals(UsuarioLogado.getUsuario())) {
                DadosDoacao.getDoacaoAtualizada(doacao, doacaoDto);
                return repositorio.save(doacao);
            } else {
                throw new BadCredentialsException("Não autorizado");
            }
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundException("Doação não encontrada. Id inválido: " + id);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Doacao doacao = repositorio.getReferenceById(id);
            if (doacao.getDoador().equals(UsuarioLogado.getUsuario())) {
                repositorio.deleteById(id);
            } else {
                throw new BadCredentialsException("Não autorizado");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Doação não encontrada. Id inválido: " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }    
}