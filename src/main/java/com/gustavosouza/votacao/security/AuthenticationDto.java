package com.gustavosouza.votacao.security;

public record AuthenticationDto(

        String email,
        String senha

) {
}
