package com.gustavosouza.votacao.services;

import com.gustavosouza.votacao.dto.VotoCadastroDto;
import com.gustavosouza.votacao.model.PautaModel;
import com.gustavosouza.votacao.model.VotosModel;
import com.gustavosouza.votacao.repository.VotoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotosServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @InjectMocks
    private VotosService votosService;


    @Test
    @DisplayName("Should save a vote successfully")
    void cadastrarVotoCase1() {

        VotoCadastroDto votoCadastroDto = new VotoCadastroDto(
                "Assunto teste",
                true,
                LocalDate.of(2026, 02, 20)
        );

        when(votoRepository.save(any(VotosModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ArgumentCaptor<VotosModel> captor = ArgumentCaptor.forClass(VotosModel.class);

        VotosModel retorno = votosService.cadastrarVoto(votoCadastroDto);
        assertNotNull(retorno);

        verify(votoRepository, times(1)).save(captor.capture());
    }


    @Test
    @DisplayName("Should throw exception if vote is invalid")
    void cadastrarVotoCase2() {
        VotoCadastroDto votoCadastroDto = new VotoCadastroDto(
                "",
                true,
                LocalDate.of(2026, 02, 20)

        );

        when(votoRepository.save(any(VotosModel.class)))
                .thenThrow(new RuntimeException("Informacoes para cadastro de voto inválidas!"));

        assertThrows(RuntimeException.class, () -> votosService.cadastrarVoto(votoCadastroDto));

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