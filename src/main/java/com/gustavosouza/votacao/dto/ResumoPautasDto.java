package com.gustavosouza.votacao.dto;

public record ResumoPautasDto(
        Long pautasAbertas,
        Long pautasFechadas,
        Long votosRegistrados
) {
}
