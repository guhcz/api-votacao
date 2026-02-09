package com.gustavosouza.votacao.repository;

import com.gustavosouza.votacao.model.PautaModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
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

    Optional<PautaModel> findByAssunto(String assunto);

    List<PautaModel> findByQuantidadeDeVotosNecessarios(Integer quantidadeDeVotosNecessarios);

    List<PautaModel> findByDataInicioBetween (LocalDate primeiraData, LocalDate segundaData);

    List<PautaModel> findByDataEncerramentoBetween (LocalDate dataInicial, LocalDate dataFinal);

}