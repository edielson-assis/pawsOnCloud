package br.com.pawsoncloud.servicos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.pawsoncloud.dtos.AdocaoDto;
import br.com.pawsoncloud.dtos.AdocaoUpdateDto;
import br.com.pawsoncloud.entidades.Adocao;
import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.entidades.NivelAcesso;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.entidades.enums.StatusAdocao;
import br.com.pawsoncloud.repositorios.AdocaoRepositorio;
import br.com.pawsoncloud.repositorios.AnimaisRepositorio;
import br.com.pawsoncloud.servicos.excecoes.DataBaseException;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;
import br.com.pawsoncloud.servicos.impl.AdocaoServicoImpl;

@ExtendWith(MockitoExtension.class)
class AdocaoServicoTest {

    @Mock
    private AdocaoRepositorio adocaoRepositorio;

    @Mock
    private AnimaisRepositorio animaisRepositorio;

    @Mock
    private AdocaoDto adocaoDto;

    @InjectMocks
    private AdocaoServicoImpl servico;
   
    private Adocao adocao;
    private Usuario edielson;
    private Usuario carlos;
    private Animais pet;

    @BeforeEach
    void setup() {
        edielson = new Usuario(1l, null, null, null, null, "782.476.740-09", null, null, NivelAcesso.getInstance(), true);
        carlos = new Usuario(2l, null, null, null, null, "411.727.360-49", null, null, NivelAcesso.getInstance(), true);
        pet = new Animais(1L, null, null, 3, null, null, 20.0, null, StatusAdocao.DISPONIVEL, getUsuarioAutenticado(edielson), false);
        adocao = new Adocao(null, null, pet, getUsuarioAutenticado(carlos), false);
    }

    @Test
    @DisplayName("Deve criar uma adocao com sucesso")
    void testDeveCriarUmaAdocaoComSucesso() {
        
        // Given / Arrange
        given(animaisRepositorio.findById(anyLong())).willReturn(Optional.of(pet));
        given(adocaoRepositorio.findByUsuarioCpf(anyString())).willReturn(List.of());
        given(adocaoRepositorio.save(adocao)).willReturn(adocao);

        // Then / Act
        Adocao adocaoSalva = servico.create(adocaoDto);

        // Then / Assert
        verify(animaisRepositorio, atLeastOnce()).findById(anyLong());
        verify(adocaoRepositorio, atMostOnce()).findByUsuarioCpf(anyString());
        verify(adocaoRepositorio, times(1)).save(any(Adocao.class));
        assertNotNull(adocaoSalva);
    }

    @Test
    @DisplayName("Deve lancar ObjectNotFoundException se o ID for invalido")
    void testDeveLancarUmaObjectNotFoundExceptionSeOIdForInvalido() {

        // Given / Arrange
        given(animaisRepositorio.findById(anyLong())).willReturn(Optional.empty());

        // Then / Act
        assertThrows(ObjectNotFoundException.class, () -> servico.create(adocaoDto));

        // Then / Assert
        verify(animaisRepositorio, times(1)).findById(anyLong());
        verify(adocaoRepositorio, never()).save(any(Adocao.class));
    }

    @Test
    @DisplayName("Deve lancar DataBaseException se o pet nao estiver disponivel")
    void testDeveLancarUmaDataBaseExceptionSeOPetNaoEstiverDisponivel() {

        // Given / Arrange
        pet.setStatus(StatusAdocao.PROCESSO_ADOCAO);
        given(animaisRepositorio.findById(anyLong())).willReturn(Optional.of(pet));

        // Then / Act
        DataBaseException exception = assertThrows(DataBaseException.class, () -> servico.create(adocaoDto));

        // Then / Assert
        verify(adocaoRepositorio, times(0)).save(any(Adocao.class));
        assertEquals("Pet em pocesso de adoção", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lancar DataBaseException se o doador tentar adotar o proprio pet")
    void testDeveLancarUmaDataBaseExceptionSeODoadorTentarAdotarOProprioPet() {

        // Given / Arrange
        adocao = new Adocao(null, null, pet, getUsuarioAutenticado(edielson), false);
        given(animaisRepositorio.findById(anyLong())).willReturn(Optional.of(pet));

        // Then / Act
        DataBaseException exception = assertThrows(DataBaseException.class, () -> servico.create(adocaoDto));

        // Then / Assert
        verify(adocaoRepositorio, atLeast(0)).save(any(Adocao.class));
        assertEquals("Você não pode adotar o próprio pet", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lancar DataBaseException se o usuario possuir alguma adocao pendente")
    void testDeveLancarUmaDataBaseExceptionSeOUsuarioPossuirAlgumaAdocaoPendente() {

        // Given / Arrange
        Animais pet2 = new Animais();
        pet2.setStatus(StatusAdocao.PROCESSO_ADOCAO);
        Adocao adocaoPendente = new Adocao(null, null, pet2, getUsuarioAutenticado(carlos), false);

        given(animaisRepositorio.findById(anyLong())).willReturn(Optional.of(pet));
        given(adocaoRepositorio.findByUsuarioCpf(anyString())).willReturn(List.of(adocaoPendente));

        // Then / Act
        DataBaseException exception = assertThrows(DataBaseException.class, () -> servico.create(adocaoDto));

        // Then / Assert
        verify(adocaoRepositorio, never()).save(any(Adocao.class));
        verify(adocaoRepositorio, times(1)).findByUsuarioCpf(anyString());
        assertEquals("Você possui uma adoção pendente", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar uma adocao com sucesso")
    void testDeveRetornarUmaAdocaoComSucesso() {
        
        // Given / Arrange
        given(adocaoRepositorio.findByUsuarioCpf(anyString())).willReturn(List.of(adocao));

        // Then / Act
        List<Adocao> adocoes = servico.findByCpf();

        // Then / Assert
        verify(adocaoRepositorio, atLeastOnce()).findByUsuarioCpf(anyString());
        assertEquals(adocoes.get(0), adocao);
        assertEquals(1, adocoes.size());
        assertNotNull(adocoes);
    }

    @Test
    @DisplayName("Deve lancar uma ObjectNotFoundException se o usuario nao possuir nenhuma adocao")
    void testDeveLancarUmaObjectNotFoundExceptionSeOUsuarioNaoPossuirNenhumaAdocao() {
        
        // Given / Arrange
        given(adocaoRepositorio.findByUsuarioCpf(anyString())).willReturn(List.of());

        // Then / Act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> servico.findByCpf());

        // Then / Assert
        verify(adocaoRepositorio, times(1)).findByUsuarioCpf(anyString());
        assertEquals("Você não possui nenhuma adoção", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar uma adocao com sucesso")
    void testDeveAtualizarUmaAdocaoComSucesso() {

        // Given / Arrange
        given(adocaoRepositorio.getReferenceById(anyLong())).willReturn(adocao);
        given(adocaoRepositorio.save(adocao)).willReturn(adocao);

        AdocaoUpdateDto adocaoUpdateDto = new AdocaoUpdateDto(1l, true);

        // Then / Act
        servico.update(1l, adocaoUpdateDto);

        // Then / Assert
        verify(adocaoRepositorio, atLeastOnce()).getReferenceById(anyLong());
        verify(adocaoRepositorio, atLeast(1)).save(any(Adocao.class));
        assertTrue(adocao.isConfirmarAdocao());
    }

    @Test
    @DisplayName("Deve lancar uma ObjectNotFoundException se o ID da adocao for invalido")
    void testDeveLancarUmaObjectNotFoundExceptionSeOIdDaAdocaoForInvalido() {
        
        // Given / Arrange
        given(adocaoRepositorio.getReferenceById(anyLong())).willThrow(new ObjectNotFoundException("Adoção não encontrada. Id inválido: " + adocao.getId()));

        // Then / Act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> servico.update(anyLong(), new AdocaoUpdateDto(null, false)));

        // Then / Assert
        verify(adocaoRepositorio, times(1)).getReferenceById(anyLong());
        verify(adocaoRepositorio, never()).save(any(Adocao.class));
        assertEquals("Adoção não encontrada. Id inválido: " + adocao.getId(), exception.getMessage());
    }

    @Test
    @DisplayName("Deve lancar uma BadCredentialsException se os dados do adotante forem diferentes")
    void testDeveLancarUmaBadCredentialsExceptionSeOsDadosDoAdotanteForemDiferentes() {
        
        // Given / Arrange
        getUsuarioAutenticado(edielson);
        given(adocaoRepositorio.getReferenceById(anyLong())).willReturn(adocao);

        // Then / Act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> servico.update(anyLong(), new AdocaoUpdateDto(null, false)));

        // Then / Assert
        verify(adocaoRepositorio, times(1)).getReferenceById(anyLong());
        verify(adocaoRepositorio, never()).save(any(Adocao.class));
        assertEquals("Não autorizado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lancar uma DataBaseException se a adocao ja tiver sido finalizada")
    void testDeveLancarUmaDataBaseExceptionSeAAdocaoJaTiverSidoFinalizada() {
        
        // Given / Arrange
        pet.setAdotado(true);
        given(adocaoRepositorio.getReferenceById(anyLong())).willReturn(adocao);

        // Then / Act
        DataBaseException exception = assertThrows(DataBaseException.class, () -> servico.update(anyLong(), new AdocaoUpdateDto(null, false)));

        // Then / Assert
        verify(adocaoRepositorio, times(1)).getReferenceById(anyLong());
        verify(adocaoRepositorio, never()).save(any(Adocao.class));
        assertEquals("Adoção já finalizada", exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar uma adocao com sucesso")
    void testDeveDeletaarUmaAdocaoComSucesso() {

        // Given / Arrange
        given(adocaoRepositorio.getReferenceById(anyLong())).willReturn(adocao);
        willDoNothing().given(adocaoRepositorio).deleteById(anyLong());

        // Then / Act
        servico.delete(1L); 

        // Then / Assert
        verify(adocaoRepositorio, atLeastOnce()).getReferenceById(anyLong());
        verify(adocaoRepositorio, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve lancar uma BadCredentialsException se os dados do adotante nao forem iguais")
    void testDeveLancarUmaBadCredentialsExceptionSeOsDadosDoAdotanteNaoForemIguais() {
        
        // Given / Arrange
        getUsuarioAutenticado(edielson);
        given(adocaoRepositorio.getReferenceById(anyLong())).willReturn(adocao);

        // Then / Act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> servico.delete(1L));

        // Then / Assert
        verify(adocaoRepositorio, times(1)).getReferenceById(anyLong());
        verify(adocaoRepositorio, never()).deleteById(anyLong());
        assertEquals("Não autorizado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lancar uma DataBaseException se a adocao ja tiver sido concluida")
    void testDeveLancarUmaDataBaseExceptionSeAAdocaoJaTiverSidoConcluida() {
        
        // Given / Arrange
        pet.setAdotado(true);
        given(adocaoRepositorio.getReferenceById(anyLong())).willReturn(adocao);

        // Then / Act
        DataBaseException exception = assertThrows(DataBaseException.class, () -> servico.delete(1L));

        // Then / Assert
        verify(adocaoRepositorio, times(1)).getReferenceById(anyLong());
        verify(adocaoRepositorio, never()).deleteById(anyLong());
        assertEquals("Adoção já finalizada", exception.getMessage());
    }

    private Usuario getUsuarioAutenticado(Usuario usaurio) {
        autenticaUsuario(usaurio);
        return usaurio;
    }

    private void autenticaUsuario(Usuario usuario) {
        var usuarioAutenticado = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usuarioAutenticado);
    }
}