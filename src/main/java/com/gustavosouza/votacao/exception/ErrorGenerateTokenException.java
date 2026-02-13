package com.gustavosouza.votacao.exception;

public class ErrorGenerateTokenException extends RuntimeException {

    public ErrorGenerateTokenException() {
        super("Erro ao gerar o token");
    }

    public ErrorGenerateTokenException(String message) {
        super(message);
    }
}
