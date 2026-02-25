package com.gustavosouza.votacao.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PautaStatusJob {

    private final PautaService pautaService;

    @Scheduled(fixedDelay = 60000)
    public void executar() {
        log.info("Iniciando Job para fechamento de pautas vencidas.");
        int pautasVencidas = pautaService.fecharPautasVencidas();
        log.info("Job encerrado com sucesso. Pautas fechadas: {}", pautasVencidas);
    }

}
