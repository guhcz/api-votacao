package com.gustavosouza.votacao.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IbgeUfDto(
        Long id,
        String sigla,
        String nome
) {
}
