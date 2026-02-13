package com.gustavosouza.votacao.exception;

public class NoCityFoundException extends RuntimeException{

    public NoCityFoundException(){
        super("CEP inválido ou nao encontrado!");
    }

    public NoCityFoundException(String message){
        super(message);
    }

}
