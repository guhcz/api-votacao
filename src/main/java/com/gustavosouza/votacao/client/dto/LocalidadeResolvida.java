package com.gustavosouza.votacao.client.dto;

public record LocalidadeResolvida(
        String cep,
        String uf,
        String estado,
        String cidade,
        Long municipioIbgeId
) {
}
