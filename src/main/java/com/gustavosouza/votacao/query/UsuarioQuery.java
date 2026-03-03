package com.gustavosouza.votacao.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Schema(name = "UsuarioQuery", description = "Parâmetros de filtro e paginação para consulta de usuários.")
public class UsuarioQuery {

    @Schema(description = "Data de nascimento inicial (YYYY-MM-DD).", example = "1990-01-01", nullable = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataInicial;

    @Schema(description = "Data de nascimento final (YYYY-MM-DD).", example = "1990-01-01", nullable = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataFinal;

    @Schema(description = "Número de páginas.", example = "0", defaultValue = "0")
    @Min(value = 0, message = "O n'mero de página deve ser maior que 0.")
    private Integer page = 0;

    @Schema(description = "Tamanho da página.", example = "10", defaultValue = "10")
    @Min(value = 1, message = "Mínimo 1")
    @Max(value = 100, message = "Máximo 100")
    private Integer size = 10;

    @Schema(description = "Filtrar por e-mail.", example = "teste@gmail.com", nullable = true)
    @Email(message = "E-mail inválido!")
    private String email;

}
