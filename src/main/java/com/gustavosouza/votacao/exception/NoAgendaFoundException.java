package com.gustavosouza.votacao.exception;

public class NoAgendaFoundException extends RuntimeException{

    public NoAgendaFoundException (){
        super ("Pauta nao encontrada!");
    }


}
