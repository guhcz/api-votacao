package com.gustavosouza.votacao.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.gustavosouza.votacao.exception.ErrorGenerateTokenException;
import com.gustavosouza.votacao.exception.TokenInvalidException;
import com.gustavosouza.votacao.model.UsuarioModel;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class TokenService {

    @Value("${api.security.token.security:}")
    private String secret;

    @PostConstruct
    void check() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("api.security.token.security não foi carregada (Vault/props).");
        }
    }

    private static final String ISSUER = "Usuário token";

    private Algorithm algorithm() {
        return Algorithm.HMAC256(secret);
    }

    public String generateToken(UserDetails user) {
        try {
            String token = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getUsername())
                    .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
                    .sign(algorithm());
            return token;
        } catch (JWTCreationException exception) {
            throw new ErrorGenerateTokenException();
        }
    }

    public String validateAndGetSubject(String token) {
        try {
            return JWT.require(algorithm())
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    public String generateEmailConfirmationToken(UsuarioModel usuario) {
        Instant expiresAt = Instant.now().plus(30, ChronoUnit.MINUTES);
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(usuario.getEmail())
                .withClaim("purpose", "email_confirm")
                .withExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
                .sign(algorithm());
    }

    public String validateEmailConfirmationTokenAndGetEmail(String token) {
        var decoded = JWT.require(algorithm())
                .withIssuer(ISSUER)
                .build()
                .verify(token);
        String purpose = decoded.getClaim("purpose").asString();
        if (!"email_confirm".equals(purpose)) {
            throw new TokenInvalidException();
        }
        return decoded.getSubject();
    }
}
