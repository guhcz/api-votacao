package com.gustavosouza.votacao.dto;

import com.gustavosouza.votacao.model.PautaModel;
import com.gustavosouza.votacao.model.UsuarioModel;

import java.time.LocalDate;

public record PautaExibicaoDto(
        Long idPauta,
        String assunto,
        Integer quantidadeDeVotosNecessarios,
        LocalDate dataInicio,
        LocalDate dataEncerramento,
        StatusPauta status
) {

    public PautaExibicaoDto(PautaModel pauta) {
        this(
                pauta.getIdPauta(),
                pauta.getAssunto(),
                pauta.getQuantidadeDeVotosNecessarios(),
                pauta.getDataInicio(),
                pauta.getDataEncerramento(),
                pauta.getStatus()
        );
    }

}
