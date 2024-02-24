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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;

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

import br.com.pawsoncloud.dtos.AnimaisDto;
import br.com.pawsoncloud.dtos.DoacaoDto;
import br.com.pawsoncloud.dtos.DoacaoUpdateDto;
import br.com.pawsoncloud.entidades.Animais;
import br.com.pawsoncloud.entidades.Doacao;
import br.com.pawsoncloud.entidades.NivelAcesso;
import br.com.pawsoncloud.entidades.Usuario;
import br.com.pawsoncloud.entidades.enums.StatusAdocao;
import br.com.pawsoncloud.repositorios.DoacaoRepositorio;
import br.com.pawsoncloud.servicos.excecoes.DataBaseException;
import br.com.pawsoncloud.servicos.excecoes.ObjectNotFoundException;
import br.com.pawsoncloud.servicos.impl.DoacaoServicoImpl;

@ExtendWith(MockitoExtension.class)
class DoacaoServicoTest {
    
    @Mock
    private DoacaoRepositorio repositorio;

    @Mock 
    private Doacao doacao;
    
    @Mock   
    private AnimaisDto animaisDto;
    
    @InjectMocks
    private DoacaoServicoImpl servico;

    private Animais pet;
    private Usuario usuario;
    private DoacaoDto doacaoDto;

    @BeforeEach
    void setup() {
        usuario = new Usuario(1L, null, null, null, null, "782.476.740-09", null, null, NivelAcesso.getInstance(), true);
        pet = new Animais(1L, null, null, 3, null, null, 20.0, null, StatusAdocao.DISPONIVEL, getUsuarioAutenticado(usuario), false);
        doacao = new Doacao(null, null, LocalDate.now(), pet, getUsuarioAutenticado(usuario), false);
        doacaoDto = new DoacaoDto(animaisDto);
    }

    @Test
    @DisplayName("Deve Criar uma doacao com sucesso")
    void testDeveCriarUmaDoacaoComSucesso() {

        // Given / Arrange
        given(repositorio.save(any(Doacao.class))).willReturn(doacao);

        // Then / Act
        Doacao doacaoSalva = servico.create(doacaoDto);

        // Then / Assert
        verify(repositorio, times(1)).save(any(Doacao.class));
        assertNotNull(doacaoSalva);
        assertEquals(LocalDate.now(), doacaoSalva.getDataCadastramento());
    }

    @Test
    @DisplayName("Deve retornar uma doacao com sucesso")
    void testDeveRetornarUmaDoacaoComSucesso() {

        // Given / Arrange
        given(repositorio.findByUsuarioCpf(anyString())).willReturn(List.of(doacao));

        // Then / Act
        List<Doacao> doacoes = servico.findByCpf();

        // Then / Assert
        verify(repositorio, atLeast(1)).findByUsuarioCpf(anyString());
        assertNotNull(doacoes);
        assertEquals(1, doacoes.size());
    }

    @Test
    @DisplayName("Deve lancar uma ObjectNotFoundException se nenhuma doacao for encontrada")
    void testDeveLancarUmaObjectNotFoundExceptionSeNenhumaDoacaoForEncontrada() {

        // Given / Arrange
        given(repositorio.findByUsuarioCpf(anyString())).willReturn(List.of());

        // Then / Act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> servico.findByCpf());

        // Then / Assert
        verify(repositorio, times(1)).findByUsuarioCpf(anyString());
        assertEquals("Você não possui nenhuma doação", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar uma doacao com sucesso")
    void testDeveAtualizarUmaDoacaoComSucesso() {

        // Given / Arrange
        given(repositorio.getReferenceById(anyLong())).willReturn(doacao);
        given(repositorio.save(any(Doacao.class))).willReturn(doacao);

        DoacaoUpdateDto doacaoUpdateDto = new DoacaoUpdateDto(1l, true);

        // Then / Act
        servico.update(1l, doacaoUpdateDto);

        // Then / Assert
        verify(repositorio, atLeastOnce()).getReferenceById(anyLong());
        verify(repositorio, atLeast(1)).save(any(Doacao.class));
        assertTrue(doacao.isConfirmarDoacao());
    }

    @Test
    @DisplayName("Deve lancar uma ObjectNotFoundException se o ID da doacao for invalido")
    void testDeveLancarUmaObjectNotFoundExceptionSeOIdDaDoacaoForInvalido() {
        
        // Given / Arrange
        given(repositorio.getReferenceById(anyLong())).willThrow(new ObjectNotFoundException("Doação não encontrada. Id inválido: " + doacao.getId()));

        // Then / Act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> servico.update(anyLong(), new DoacaoUpdateDto(null, false)));

        // Then / Assert
        verify(repositorio, times(1)).getReferenceById(anyLong());
        verify(repositorio, never()).save(any(Doacao.class));
        assertEquals("Doação não encontrada. Id inválido: " + doacao.getId(), exception.getMessage());
    }

    @Test
    @DisplayName("Deve lancar uma BadCredentialsException se os dados do doador forem diferentes")
    void testDeveLancarUmaBadCredentialsExceptionSeOsDadosDoDoadorForemDiferentes() {
        
        // Given / Arrange
        getUsuarioAutenticado(new Usuario(2l, null, null, null, null, "411.727.360-49", null, null, NivelAcesso.getInstance(), true));
        given(repositorio.getReferenceById(anyLong())).willReturn(doacao);

        // Then / Act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> servico.update(anyLong(), new DoacaoUpdateDto(null, false)));

        // Then / Assert
        verify(repositorio, times(1)).getReferenceById(anyLong());
        verify(repositorio, never()).save(any(Doacao.class));
        assertEquals("Não autorizado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lancar uma DataBaseException se a doacao ja tiver sido finalizada")
    void testDeveLancarUmaDataBaseExceptionSeADoacaoJaTiverSidoFinalizada() {
        
        // Given / Arrange
        pet.setAdotado(true);
        given(repositorio.getReferenceById(anyLong())).willReturn(doacao);

        // Then / Act
        DataBaseException exception = assertThrows(DataBaseException.class, () -> servico.update(anyLong(), new DoacaoUpdateDto(null, false)));

        // Then / Assert
        verify(repositorio, times(1)).getReferenceById(anyLong());
        verify(repositorio, never()).save(any(Doacao.class));
        assertEquals("Doação já finalizada", exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar uma doacao com sucesso")
    void testDeveDeletaarUmaDoacaoComSucesso() {

        // Given / Arrange
        given(repositorio.getReferenceById(anyLong())).willReturn(doacao);
        willDoNothing().given(repositorio).deleteById(anyLong());

        // Then / Act
        servico.delete(1L); 

        // Then / Assert
        verify(repositorio, atLeastOnce()).getReferenceById(anyLong());
        verify(repositorio, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve lancar uma BadCredentialsException se os dados do adotante nao forem iguais")
    void testDeveLancarUmaBadCredentialsExceptionSeOsDadosDoAdotanteNaoForemIguais() {
        
        // Given / Arrange
        getUsuarioAutenticado(new Usuario(2l, null, null, null, null, "411.727.360-49", null, null, NivelAcesso.getInstance(), true));
        given(repositorio.getReferenceById(anyLong())).willReturn(doacao);

        // Then / Act
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> servico.delete(1L));

        // Then / Assert
        verify(repositorio, times(1)).getReferenceById(anyLong());
        verify(repositorio, never()).deleteById(anyLong());
        assertEquals("Não autorizado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lancar uma DataBaseException se a doacao ja tiver sido concluida")
    void testDeveLancarUmaDataBaseExceptionSeADoacaoJaTiverSidoConcluida() {
        
        // Given / Arrange
        pet.setAdotado(true);
        given(repositorio.getReferenceById(anyLong())).willReturn(doacao);

        // Then / Act
        DataBaseException exception = assertThrows(DataBaseException.class, () -> servico.delete(1L));

        // Then / Assert
        verify(repositorio, times(1)).getReferenceById(anyLong());
        verify(repositorio, never()).deleteById(anyLong());
        assertEquals("Doação já finalizada", exception.getMessage());
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