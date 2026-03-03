package com.gustavosouza.votacao.dto;

import java.time.LocalDate;

public record VotoExibicaoDto(
        Long idVoto,
        String assuntoVotado,
        Boolean voto,
        LocalDate dataVoto,
        Long idPauta,
        Long idUsuario
) {
}
