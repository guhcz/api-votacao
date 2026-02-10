package com.gustavosouza.votacao.exception;

public class NoUserFoundException extends RuntimeException {

    public NoUserFoundException() {
        super("Usuário nao encontrado!");
    }

    public NoUserFoundException(String message) {
        super(message);
    }
}
