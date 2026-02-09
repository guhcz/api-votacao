package com.gustavosouza.votacao.exception;

public class NoVoteFoundException extends RuntimeException {

    public NoVoteFoundException() {
        super("Registro de voto nao encontrado!");
    }

    public NoVoteFoundException(String message) {
        super(message);
    }

}
