package com.gustavosouza.votacao.services;

import com.gustavosouza.votacao.dto.PautaCadastroDto;
import com.gustavosouza.votacao.dto.PautaExibicaoDto;
import com.gustavosouza.votacao.exception.NoAgendaFoundException;
import com.gustavosouza.votacao.model.PautaModel;
import com.gustavosouza.votacao.model.UsuarioModel;
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

    @InjectMocks
    private PautaService pautaService;


    @Test
    @DisplayName("Should save a new agenda successfully")
    void criarPautaCase1() {

        PautaCadastroDto pautaCadastroDto = new PautaCadastroDto(
                "Eleicoes de chefe",
                100,
                LocalDate.of(2026, 02, 20),
                LocalDate.of(2026, 02, 28)
        );

        when(pautaRepository.save(any(PautaModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ArgumentCaptor<PautaModel> captor = ArgumentCaptor.forClass(PautaModel.class);

        PautaExibicaoDto retorno = pautaService.criarPauta(pautaCadastroDto);
        assertNotNull(retorno);

        verify(pautaRepository, times(1)).save(captor.capture());

        PautaModel salvo = captor.getValue();
        assertNotNull(salvo);

    }

    @Test
    @DisplayName("Should thorw exception when agenda is invalid")
    void criarPautaCase2() {
        PautaCadastroDto pautaCadastroDto = new PautaCadastroDto(
                "",
                100,
                LocalDate.of(2026, 02, 20),
                LocalDate.of(2026, 02, 28)
        );

        when(pautaRepository.save(any(PautaModel.class)))
                .thenThrow(new NoAgendaFoundException());

        assertThrows(NoAgendaFoundException.class, () -> pautaService.criarPauta(pautaCadastroDto));

        verify(pautaRepository, times(1)).save(any(PautaModel.class));
    }

    @Test
    @DisplayName("Should delete the agend by id sucessfully")
    void deletarPautaCase1() {
        Long idPauta = 1L;

        pautaService.excluirPauta(idPauta);

        verify(pautaRepository, times(1)).deleteById(idPauta);
        verifyNoMoreInteractions(pautaRepository);

    }


    @Test
    @DisplayName("Should delete the agend by topic sucessfully")
    void deletarPautaCase2() {
        String assunto = "assunto teste";

        pautaService.excluirPautaPeloAssunto(assunto);

        verify(pautaRepository, times(1)).deleteByAssunto(assunto);
        verifyNoMoreInteractions(pautaRepository);

    }
}