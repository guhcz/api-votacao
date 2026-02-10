package com.gustavosouza.votacao.client.dto;

public record AccessTokenRequest(String grand_type, String client_id, String client_secret) {
}
