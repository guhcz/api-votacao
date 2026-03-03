package com.gustavosouza.votacao.repository;

import com.gustavosouza.votacao.model.UsuarioModel;
import com.gustavosouza.votacao.model.VotosModel;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VotoRepository extends JpaRepository<VotosModel, Long> {

    @Query("""
            SELECT v
            FROM VotosModel v
            WHERE (:assuntoVotado IS NULL OR LOWER(v.assuntoVotado) LIKE LOWER(CONCAT('%', :assuntoVotado, '%')))
            AND (:dataInicial IS NULL OR v.dataVoto >= :dataInicial)
            AND (:dataFinal IS NULL OR v.dataVoto <= :dataFinal)
            """)
    Page<VotosModel> buscarPorFiltros(
            @Param("assuntoVotado") String assuntoVotado,
            @Param("dataInicial") LocalDate dataInicial,
            @Param("dataFinal") LocalDate dataFinal,
            Pageable pageable
    );
}
