package com.gustavosouza.votacao.client.dto;

public record AccessTokenResponse(String access_token, String token_type, Integer expires_in) {
}
