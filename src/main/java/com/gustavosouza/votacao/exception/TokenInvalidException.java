package com.gustavosouza.votacao.exception;

public class TokenInvalidException extends RuntimeException{

    public TokenInvalidException(){
        super("Token inválido para confirmacao de e-mail!");
    }

    public TokenInvalidException (String message){
        super(message);
    }
}
