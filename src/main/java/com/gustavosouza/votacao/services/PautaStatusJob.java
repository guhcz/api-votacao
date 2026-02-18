package com.gustavosouza.votacao.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PautaStatusJob {

    private final PautaService pautaService;

    @Scheduled(fixedDelay = 60000)
    public void executar() {
        int pautasVencidas = pautaService.fecharPautasVencidas();
    }

}
