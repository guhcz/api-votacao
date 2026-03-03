package com.gustavosouza.votacao.services;

import com.gustavosouza.votacao.dto.VotoCadastroDto;
import com.gustavosouza.votacao.dto.VotoExibicaoDto;
import com.gustavosouza.votacao.mapstruct.VotosMapper;
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

    @Mock
    private VotosMapper votosMapper;

    @InjectMocks
    private VotosService votosService;


    @Test
    @DisplayName("Should save a vote successfully")
    void dadoParametrosValidos_quandoCriarVoto_deveCriarComSucesso() {

        Long idPauta = 1L;

        VotoCadastroDto votoCadastroDto = new VotoCadastroDto(
                "Assunto teste",
                true,
                LocalDate.of(2026, 2, 20)
        );

        PautaModel pauta = new PautaModel();
        UsuarioModel usuario = new UsuarioModel();

        Authentication auth = org.mockito.Mockito.mock(Authentication.class);
        when(auth.getName()).thenReturn("teste@teste.com");

        when(pautaRepository.findById(idPauta)).thenReturn(Optional.of(pauta));
        when(usuarioRepository.findByEmail("teste@teste.com")).thenReturn(Optional.of(usuario));

        when(votosMapper.votosModel(any(VotoCadastroDto.class))).thenAnswer(inv -> {
            VotoCadastroDto dto = inv.getArgument(0);
            VotosModel voto = new VotosModel();
            voto.setAssuntoVotado(dto.assuntoVotado());
            voto.setDataVoto(dto.dataVoto());
            voto.setVoto(dto.voto());
            return voto;
        });

        when(votoRepository.save(any(VotosModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        VotoExibicaoDto votoExibicaoDto = new VotoExibicaoDto(
                1L,
                "Assunto de teste",
                true,
                LocalDate.of(2026, 2, 20),
                1L,
                1L
        );

        when(votosMapper.votoExibicaoDto(any(VotosModel.class))).thenReturn(votoExibicaoDto);

        VotoExibicaoDto retorno = votosService.cadastrarVoto(votoCadastroDto, idPauta, auth);

        assertNotNull(retorno);

        verify(pautaRepository).findById(idPauta);
        verify(usuarioRepository).findByEmail("teste@teste.com");

        ArgumentCaptor<VotosModel> captor = ArgumentCaptor.forClass(VotosModel.class);
        verify(votoRepository).save(captor.capture());

        VotosModel salvo = captor.getValue();

        assertNotNull(salvo.getPautaModel());
        assertNotNull(salvo.getUsuarioModel());
        assertEquals(pauta, salvo.getPautaModel());
        assertEquals(usuario, salvo.getUsuarioModel());

        assertEquals("Assunto teste", salvo.getAssuntoVotado());
        assertEquals(LocalDate.of(2026, 2, 20), salvo.getDataVoto());
        assertTrue(salvo.getVoto());
    }


    @Test
    @DisplayName("Should delete a vote successfully")
    void dadoParametrosValidos_quandoEncontrarPauta_deveDeletarComSucesso() {
        Long idVoto = 1L;

        VotosModel voto = new VotosModel();
        when(votoRepository.findById(idVoto)).thenReturn(Optional.of(voto));

        votosService.deletarVoto(idVoto);

        verify(votoRepository, times(1)).findById(idVoto);
        verify(votoRepository, times(1)).delete(voto);
        verifyNoMoreInteractions(votoRepository);
    }
}