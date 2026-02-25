package com.gustavosouza.votacao.dto;

import java.time.LocalDate;

public record VotoResponseDto(
        Long idVoto,
        String assuntoVotado,
        Boolean voto,
        LocalDate dataVoto,
        Long idPauta,
        Long idUsuario
) {
}
