package com.gustavosouza.votacao.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LocalidadeResolvida(
        String cep,
        String uf,
        String estado,
        String cidade
) {
}
