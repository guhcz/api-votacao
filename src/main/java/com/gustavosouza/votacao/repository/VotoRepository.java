package com.gustavosouza.votacao.repository;

import com.gustavosouza.votacao.model.UsuarioModel;
import com.gustavosouza.votacao.model.VotosModel;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VotoRepository extends JpaRepository<VotosModel, Long> {

    Optional<List<VotosModel>> findByAssuntoVotado(String assuntoVotado);

    List<VotosModel> findByDataVotoBetween(LocalDate dataInicial, LocalDate dataFinal);


}
