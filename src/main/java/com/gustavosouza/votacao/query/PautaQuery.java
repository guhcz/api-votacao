package com.gustavosouza.votacao.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Schema(name = "PautaQuery", description = "Parâmetros de filtro e paginação para consulta de pautas.")
public class PautaQuery {

    @Schema(description = "Filtrar por assunto.", example = "Orçamento", nullable = true)
    private String assunto;

    @Schema(description = "Filtrar por quantidades de votos.", example = "50", nullable = true)
    @Min(value = 0, message = "Quantidade de votos deve ser maior que 0.")
    private Integer votosNecessarios;

    @Schema(description = "Data inicial do filtro de data de início da pauta.", example = "2026-01-01", nullable = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataInicioDe;

    @Schema(description = "Data final do filtro de data de início da pauta.", example = "2026-01-31", nullable = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataInicioAte;

    @Schema(description = "Data inicial do filtro de data de encerramento da pauta.", example = "2026-02-01", nullable = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataEncerramentoDe;

    @Schema(description = "Data inicial do filtro de data de encerramento da pauta.", example = "2026-02-01", nullable = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataEncerramentoAte;

    @Schema(description = "Número da página.", example = "0", defaultValue = "0")
    @Min(value = 0, message = "Página deve ser maior que 0.")
    private Integer page = 0;

    @Schema(description = "Tamanho da página.", example = "10", defaultValue = "10")
    @Min(value = 1, message = "Tamanho mínimo deve ser 1")
    @Max(value = 100, message = "Tamanho máximo deve ser 100.")
    private Integer size = 10;

}
