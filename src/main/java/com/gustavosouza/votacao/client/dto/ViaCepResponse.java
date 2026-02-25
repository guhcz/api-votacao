package com.gustavosouza.votacao.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ViaCepResponse(
        String cep,
        String uf,
        String localidade,
        Boolean erro
) {
}
