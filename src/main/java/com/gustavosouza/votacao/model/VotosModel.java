package com.gustavosouza.votacao.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "VOTOS")
public class VotosModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_VOTO")
    Long idVoto;

    @Column(name = "ASSUNTO_VOTADO", unique = true)
    String assuntoVotado;

    @Column(name = "VOTO")
    Boolean voto;

    @Column(name = "DATA_VOTO")
    LocalDate dataVoto;

}
