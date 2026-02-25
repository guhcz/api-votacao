package com.gustavosouza.votacao.services;

import com.gustavosouza.votacao.dto.VotoCadastroDto;
import com.gustavosouza.votacao.model.PautaModel;
import com.gustavosouza.votacao.model.UsuarioModel;
import com.gustavosouza.votacao.model.VotosModel;
import com.gustavosouza.votacao.repository.PautaRepository;
import com.gustavosouza.votacao.repository.UsuarioRepository;
import com.gustavosouza.votacao.repository.VotoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotosServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private VotosService votosService;


    @Test
    @DisplayName("Should save a vote successfully")
    void cadastrarVotoCase1() {

        Long idPauta = 1L;

        VotoCadastroDto votoCadastroDto = new VotoCadastroDto(
                1L,
                "Assunto teste",
                true,
                LocalDate.of(2026, 2, 20)
        );

        PautaModel pauta = new PautaModel();

        UsuarioModel usuario = new UsuarioModel();

        Authentication auth = org.mockito.Mockito.mock(Authentication.class);
        when(auth.getName()).thenReturn("teste@teste.com");

        when(pautaRepository.findById(idPauta))
                .thenReturn(Optional.of(pauta));
        when(usuarioRepository.findByEmail("teste@teste.com"))
                .thenReturn(Optional.of(usuario));

        when(votoRepository.save(any(VotosModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ArgumentCaptor<VotosModel> captor = ArgumentCaptor.forClass(VotosModel.class);

        VotosModel retorno = votosService.cadastrarVoto(votoCadastroDto, idPauta, auth);

        assertNotNull(retorno);

        verify(votoRepository, times(1)).save(captor.capture());
        verify(pautaRepository, times(1)).findById(idPauta);
        verify(usuarioRepository, times(1)).findByEmail("teste@teste.com");

        VotosModel votoSalvo = captor.getValue();
        assertNotNull(votoSalvo);
        assertEquals("Assunto teste", votoSalvo.getAssuntoVotado());
        assertTrue(votoSalvo.getVoto());
        assertEquals(LocalDate.of(2026, 2, 20), votoSalvo.getDataVoto());
        assertNotNull(votoSalvo.getPautaModel());
        assertNotNull(votoSalvo.getUsuarioModel());
    }


    @Test
    @DisplayName("Should save even if DTO is invalid (validation happens in controller)")
    void cadastrarVotoCase2() {

        // Arrange
        Long idPauta = 1L;

        VotoCadastroDto votoCadastroDto = new VotoCadastroDto(
                1L,
                "",
                true,
                LocalDate.of(2026, 2, 20)
        );

        PautaModel pauta = new PautaModel();
        UsuarioModel usuario = new UsuarioModel();

        Authentication auth = org.mockito.Mockito.mock(Authentication.class);
        when(auth.getName()).thenReturn("teste@teste.com");

        when(pautaRepository.findById(idPauta)).thenReturn(Optional.of(pauta));
        when(usuarioRepository.findByEmail("teste@teste.com")).thenReturn(Optional.of(usuario));

        when(votoRepository.save(any(VotosModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        VotosModel retorno = votosService.cadastrarVoto(votoCadastroDto, idPauta, auth);

        assertNotNull(retorno);
        verify(votoRepository, times(1)).save(any(VotosModel.class));
    }

    @Test
    @DisplayName("Should delete a vote successfully")
    void deletarVoto() {
        Long idVoto = 1L;

        VotosModel voto = new VotosModel();
        when(votoRepository.findById(idVoto)).thenReturn(Optional.of(voto));

        votosService.deletarVoto(idVoto);

        verify(votoRepository, times(1)).findById(idVoto);
        verify(votoRepository, times(1)).delete(voto);
        verifyNoMoreInteractions(votoRepository);
    }
}