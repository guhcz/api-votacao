package com.gustavosouza.votacao.client.dto;

public record EstadoCidadeDto(
        String ufSigla,
        String estadoNome,
        String municipioNome
) {
}
