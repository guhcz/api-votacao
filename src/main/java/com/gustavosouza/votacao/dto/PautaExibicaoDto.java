package com.gustavosouza.votacao.dto;

import com.gustavosouza.votacao.model.PautaModel;
import com.gustavosouza.votacao.model.UsuarioModel;

import java.time.LocalDate;

public record PautaExibicaoDto(
        String assunto,
        Integer quantidadeDeVotosNecessarios,
        LocalDate dataInicio,
        LocalDate dataEncerramento
) {

    public PautaExibicaoDto(PautaModel pauta) {
        this(
                pauta.getAssunto(),
                pauta.getQuantidadeDeVotosNecessarios(),
                pauta.getDataInicio(),
                pauta.getDataEncerramento()
        );
    }

}
