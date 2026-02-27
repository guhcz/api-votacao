package com.gustavosouza.votacao.services;

import com.gustavosouza.votacao.dto.StatusPauta;
import com.gustavosouza.votacao.model.PautaModel;
import com.gustavosouza.votacao.repository.PautaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PautaServiceTesteParametrizado {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    @ParameterizedTest
    @EnumSource(StatusPauta.class)
    @DisplayName("Should mark as closed the returned agendas")
    void dadoStatusValido_quandoFecharPautasVencidas_deveAtualizarStatusParaFechada(StatusPauta statusInicial){

        PautaModel p1 = new PautaModel();
        p1.setStatus(statusInicial);
        p1.setDataEncerramento(LocalDate.now().minusDays(1));

        PautaModel p2 = new PautaModel();
        p2.setStatus(statusInicial);
        p2.setDataEncerramento(LocalDate.now().minusDays(2));

        Page<PautaModel> page1 = new PageImpl<>(List.of(p1, p2));
        Page<PautaModel> vazio = Page.empty();

        when(pautaRepository.findByStatusAndDataEncerramentoBefore(
                eq(StatusPauta.ABERTA),
                any(LocalDate.class),
                any(Pageable.class)))
                .thenReturn(page1)
                .thenReturn(vazio);

        ArgumentCaptor<List<PautaModel>> captorAll =ArgumentCaptor.forClass(List.class);

        int total = pautaService.fecharPautasVencidas();

        assertEquals(2, total);

        verify(pautaRepository, times(1))
                .findByStatusAndDataEncerramentoBefore(eq(StatusPauta.ABERTA), any(LocalDate.class), any(Pageable.class));

        verify(pautaRepository, times(1)).saveAll(captorAll.capture());

        List<PautaModel> salvas = captorAll.getValue();
        assertEquals(2, salvas.size());
        assertTrue(salvas.stream().allMatch(p -> p.getStatus() == StatusPauta.FECHADA));

        verifyNoMoreInteractions(pautaRepository);
    }
}
