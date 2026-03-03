package com.gustavosouza.votacao.repository;

import com.gustavosouza.votacao.model.UsuarioModel;
import jakarta.transaction.Transactional;
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
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    Optional<UsuarioModel> findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);

    @Query("""
            SELECT u
            FROM UsuarioModel u
            WHERE (:email IS NULL OR LOWER(u.email) = LOWER (:email))
            AND (:dataInicial IS NULL OR u.dataNascimento >= :dataInicial)
            AND (:dataFinal IS NULL OR u.dataNascimento <= :dataFinal)
            """)
    Page<UsuarioModel> buscarPorDataNascimento(
            @Param("email") String email,
            @Param("dataInicial") LocalDate dataInicial,
            @Param("dataFinal") LocalDate dataFinal,
            Pageable pageable);

}
