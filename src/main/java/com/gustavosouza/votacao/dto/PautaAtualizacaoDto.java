package com.gustavosouza.votacao.dto;

import java.time.LocalDate;

public record PautaAtualizacaoDto(
        String assunto,
        Integer quantidadeDeVotosNecessarios,
        LocalDate dataInicio,
        LocalDate dataEncerramento
) {
}
