package com.gustavosouza.votacao.model;

import com.gustavosouza.votacao.dto.StatusPauta;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PAUTA")
public class PautaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PAUTAS")
    @SequenceGenerator(name = "SEQ_PAUTAS", sequenceName = "SEQ_PAUTAS", allocationSize = 1)
    @Column(name = "ID_PAUTA")
    private Long idPauta;

    @Column(name = "ASSUNTO", unique = true)
    private String assunto;

    @Column(name = "VOTOS_NECESSARIOS")
    private Integer quantidadeDeVotosNecessarios;

    @Column(name = "DATA_INICIO")
    private LocalDate dataInicio;

    @Column(name = "DATA_ENCERRAMENTO")
    private LocalDate dataEncerramento;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private StatusPauta status = StatusPauta.ABERTA;

    @OneToMany(mappedBy = "pautaModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<VotosModel> votosModel = new ArrayList<>();
}
