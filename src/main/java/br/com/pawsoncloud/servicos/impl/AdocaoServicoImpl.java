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

/**
 * Classe que implementa a interface <b>AdocaoServico</b>.
 * 
 * @author Edielson Assis
 */
@Service
@AllArgsConstructor
public class AdocaoServicoImpl implements AdocaoServico {

    private final AdocaoRepositorio repositorio;
    private final AnimaisRepositorio animaisRepositorio;

    /**
     * Método responsável por realizar uma adoção. Antes da adoção ser realizada, é realizado algumas verificações.
     * 1 - O id do pet informado é válido? Se não, é lançado uma exceção.
     * 2 - O pet está disponivel? Se não, é lançado uma exceção.
     * 3 - Os dados do adotante são os mesmos do doador? Se sim, é lançado uma exceção.
     * 4 - O adotante possui alguma adoção pendente? Se sim, é lançado uma exceção.
     * 
     * @param adocaoDto contém os dados do adotante e do animal que será adotado.
     * @return Adocao
     * @exception ObjectNotFoundException é lançada caso o pet não seja encontrado.
     * @exception DataBaseException é lançada caso o adotante seja o doador ou caso o usuário possua alguma adoçao pendente.
     */
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

    /**
     * Lista com todas as adoções realizadas pelo usuário. 
     * É utilizado o cpf do usuário para carregar a lista.
     * Caso a lista esteja vazia, é lançado uma exceção.
     * 
     * @return lista de adoções
     * @exception ObjectNotFoundException é lançada caso nenhuma adoção seja encontrada.
     */
    @Override
    public List<Adocao> findByCpf() {
        var adocoes = repositorio.findByUsuarioCpf(UsuarioLogado.getUsuario().getCpf());
        if (adocoes.isEmpty()) {
            throw new ObjectNotFoundException("Você não possui nenhuma adoção");
        }
        return adocoes;
    }

    /**
     * Atualiza os dados da adoção com base no id informado. Caso a adoção não seja encontrada, é lançãdo uma exceção.
     * Antes de atualizar, verifica se o usuário logado possui os mesmos dados do doador. Se não, outra exceção é lançada.
     * Por fim, verifica se a adoção já foi finalizada. Se sim, uma exceção é lançada, negando a operação.
     * 
     * @param adocaoDto adoção que será atualizada.
     * @exception EntityNotFoundException capturada caso o usuário não seja encotrado.
     * @exception BadCredentialsException é lançada caso as credenciais do usuário sejam inválidas.
     * @exception DataBaseException é lançada caso a adoção tenha sido finalizada.
     */
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

    /**
     * Cancela uma adoção com base no id informado. Caso a adoção não seja encontrada, é lançãdo uma exceção.
     * Antes de cancelar, verifica se o usuário logado possui os mesmos dados do doador. Se não, outra exceção é lançada.
     * Por fim, verifica se a adoção já foi finalizada. Se sim, uma exceção é lançada, negando a operação.
     * 
     * @param id d da adoção que será cancelada.
     * @exception EntityNotFoundException capturada caso o usuário não seja encotrado.
     * @exception BadCredentialsException é lançada caso as credenciais do usuário sejam inválidas.
     * @exception DataBaseException é lançada caso a adoção tenha sido finalizada.
     */
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
    // Pega o pet no banco de dados pelo id informado. Se o id for inválido, uma exceção será lançada.
    private Animais getPetOrThrow(Long id) {
        return animaisRepositorio.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pet não encontrado. Id inválido: " + id));
    }
    // Verifica se o pet está disponível.
    private boolean isDisponivel(Animais pet) {
        return pet.getStatus().equals(StatusAdocao.DISPONIVEL);
    }
    // Verifica se o adotante é o doador.
    private boolean isDoador(Animais pet) {
        return pet.getUsuario().equals(UsuarioLogado.getUsuario());
    }
    // Atualiza o status do animal para em PROCESSO_ADOCAO.
    private void setStatusProcessoAdocao(Animais pet) {
        pet.setStatus(StatusAdocao.PROCESSO_ADOCAO);
        animaisRepositorio.save(pet);
    }
    // Atualiza o status do animal para DISPONIVEL.
    private void setStatusDisponivel(Animais pet) {
        pet.setStatus(StatusAdocao.DISPONIVEL);
        animaisRepositorio.save(pet);
    }
    // Pega a referência da adoção no banco de dados pelo id informado
    private Adocao getAdocaoReferenceOrThrow(Long id) {
        try {
            return repositorio.getReferenceById(id);
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundException("Adoção não encontrada. Id inválido: " + id);
        }
    }
    // Verifica se o adotante possui alguma adoção pendente.
    private boolean hasAdocaoPendente(Usuario usuario) {
        List<Adocao> adocoes = repositorio.findByUsuarioCpf(usuario.getCpf());
        return adocoes.stream().anyMatch(pet -> pet.getPet().getStatus().equals(StatusAdocao.PROCESSO_ADOCAO));
    }
}