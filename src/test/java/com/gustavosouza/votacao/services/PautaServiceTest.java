package com.gustavosouza.votacao.services;

import com.gustavosouza.votacao.dto.PautaCadastroDto;
import com.gustavosouza.votacao.dto.PautaExibicaoDto;
import com.gustavosouza.votacao.exception.NoAgendaFoundException;
import com.gustavosouza.votacao.mapstruct.PautaMapper;
import com.gustavosouza.votacao.model.PautaModel;
import com.gustavosouza.votacao.repository.PautaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private PautaMapper pautaMapper;

    @InjectMocks
    private PautaService pautaService;


    @Test
    @DisplayName("Should save a new agenda successfully")
    void dadoParametrosValidos_quandoCriarPauta_deveCriarComSucesso() {

        PautaCadastroDto pautaCadastroDto = new PautaCadastroDto(
                "Eleicoes de chefe",
                100,
                LocalDate.of(2026, 02, 20),
                LocalDate.of(2026, 02, 28)
        );

        PautaModel pautaModel = new PautaModel();
        pautaModel.setAssunto("Eleicoes de chefe");
        pautaModel.setQuantidadeDeVotosNecessarios(100);
        pautaModel.setDataInicio(LocalDate.of(2026, 2, 20));
        pautaModel.setDataEncerramento(LocalDate.of(2026, 2, 28));

        PautaModel salvo = new PautaModel();
        salvo.setIdPauta(1L);
        salvo.setAssunto(pautaModel.getAssunto());
        salvo.setQuantidadeDeVotosNecessarios(pautaModel.getQuantidadeDeVotosNecessarios());
        salvo.setDataInicio(pautaModel.getDataInicio());
        salvo.setDataEncerramento(pautaModel.getDataEncerramento());

        PautaExibicaoDto pautaExibicaoDto = new PautaExibicaoDto(
                "Eleicoes de chefe",
                100,
                LocalDate.of(2026, 2, 20),
                LocalDate.of(2026, 2, 28)
        );

        when(pautaMapper.pautaModel(any(PautaCadastroDto.class))).thenReturn(pautaModel);
        when(pautaRepository.save(any(PautaModel.class))).thenReturn(salvo);
        when(pautaMapper.pautaExibicaoDto(any(PautaModel.class))).thenReturn(pautaExibicaoDto);

        ArgumentCaptor<PautaModel> captor = ArgumentCaptor.forClass(PautaModel.class);

        PautaExibicaoDto retorno = pautaService.criarPauta(pautaCadastroDto);

        assertNotNull(retorno);

        verify(pautaRepository, times(1)).save(captor.capture());

        PautaModel salvar = captor.getValue();
        assertNotNull(salvar);
        assertEquals("Eleicoes de chefe", salvar.getAssunto());
        assertEquals(100, salvar.getQuantidadeDeVotosNecessarios());
    }

    @Test
    @DisplayName("Should thorw exception when agenda is invalid")
    void dadoParametrosInvalidos_quandoCriarPauta_deveLancarUmaExcessao() {

        PautaCadastroDto pautaCadastroDto = new PautaCadastroDto(
                "",
                100,
                LocalDate.of(2026, 2, 20),
                LocalDate.of(2026, 2, 28)
        );

        when(pautaMapper.pautaModel(pautaCadastroDto)).thenThrow(new NoAgendaFoundException());

        var exception = assertThrows(NoAgendaFoundException.class,
                () -> pautaService.criarPauta(pautaCadastroDto));

        assertEquals("Pauta nao encontrada!", exception.getMessage());

        verify(pautaMapper).pautaModel(pautaCadastroDto);
        verifyNoInteractions(pautaRepository);
    }


    @Test
    @DisplayName("Should delete the agend by id sucessfully")
    void dadoParametrosValidos_quandoEncontrarPauta_deveDeletarComSucesso() {
        Long idPauta = 1L;

        pautaService.excluirPauta(idPauta);

        verify(pautaRepository, times(1)).deleteById(idPauta);
        verifyNoMoreInteractions(pautaRepository);

    }


    @Test
    @DisplayName("Should delete the agend by topic sucessfully")
    void dadoAssuntoValido_quandoEncontrarPauta_deveDeletarComSucesso() {
        String assunto = "assunto teste";

        pautaService.excluirPautaPeloAssunto(assunto);

        verify(pautaRepository, times(1)).deleteByAssunto(assunto);
        verifyNoMoreInteractions(pautaRepository);

    }
}