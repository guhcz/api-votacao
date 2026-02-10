package com.gustavosouza.votacao.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.criteria.From;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequestDto {
    public String html;
    public String text;
    public String subject;
    public DadosEmailRequest from;
    public List<DadosEmailRequest> to;
}
