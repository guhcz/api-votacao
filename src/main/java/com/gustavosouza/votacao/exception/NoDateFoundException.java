package com.gustavosouza.votacao.exception;

public class NoDateFoundException extends RuntimeException {

    public NoDateFoundException() {
        super("Nenhuma data encontrada de acordo com o filtro!");
    }

    public NoDateFoundException(String message) {
        super(message);
    }

}
