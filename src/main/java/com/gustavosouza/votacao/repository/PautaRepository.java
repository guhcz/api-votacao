package com.gustavosouza.votacao.repository;

import com.gustavosouza.votacao.dto.StatusPauta;
import com.gustavosouza.votacao.model.PautaModel;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PautaRepository extends JpaRepository<PautaModel, Long> {

    @Transactional
    void deleteById (Long id);

    @Transactional
    void deleteByAssunto (String assunto);

    Page<PautaModel> findByStatusAndDataEncerramentoBefore(StatusPauta status, LocalDate agora, Pageable pageable);

    @Query("""
                    SELECT DISTINCT p
                    FROM PautaModel p
                    LEFT JOIN p.votosModel v
                    WHERE (:assunto IS NULL OR LOWER(p.assunto) LIKE LOWER(CONCAT('%', :assunto, '%')))
                    AND (:dataInicioDe IS NULL OR p.dataInicio >= :dataInicioDe)
                    AND (:dataInicioAte IS NULL OR p.dataInicio <= :dataInicioAte)
                    AND (:dataEncerramentoDe IS NULL OR p.dataEncerramento >= :dataEncerramentoDe)
                    AND (:dataEncerramentoAte IS NULL OR p.dataEncerramento <= :dataEncerramentoAte)
                    GROUP BY p
                    HAVING (:quantidadeVotos IS NULL OR COUNT(v) >= :quantidadeVotos)
            """)
    Page<PautaModel> buscarPorFiltros(
            @Param("assunto") String assunto,
            @Param("quantidadeVotos") Integer quantidadeVotos,
            @Param("dataInicioDe") LocalDate dataInicioDe,
            @Param("dataInicioAte") LocalDate dataInicioAte,
            @Param("dataEncerramentoDe") LocalDate dataEncerramentoDe,
            @Param("dataEncerramentoAte") LocalDate dataEncerramentoAte,
            Pageable pageable
    );


}