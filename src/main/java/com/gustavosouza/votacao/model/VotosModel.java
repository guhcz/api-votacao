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
    private Long idVoto;

    @Column(name = "ASSUNTO_VOTADO", unique = true)
    private String assuntoVotado;

    @Column(name = "VOTO")
    private Boolean voto;

    @Column(name = "DATA_VOTO")
    private LocalDate dataVoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioModel usuarioModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pauta", nullable = false)
    private PautaModel pautaModel;
}
