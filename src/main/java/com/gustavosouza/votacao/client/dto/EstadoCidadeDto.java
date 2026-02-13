package com.gustavosouza.votacao.client.dto;

public record EstadoCidadeDto(
        String ufSigla,
        String estadoNome,
        Long municipioId,
        String municipioNome
) {
}
