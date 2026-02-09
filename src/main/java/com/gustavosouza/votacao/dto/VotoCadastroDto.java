package com.gustavosouza.votacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record VotoCadastroDto(

        @NotBlank(message = "O assunto votado tem que ser preenchido!")
        String assuntoVotado,

        @NotNull(message = "O voto é obrigatório!")
        Boolean voto,

        @NotNull(message = "A data precisa ser preenchida!")
        LocalDate dataVoto

) {
}
