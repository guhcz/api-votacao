package com.gustavosouza.votacao.controllers;

import com.gustavosouza.votacao.dto.VotoCadastroDto;
import com.gustavosouza.votacao.dto.VotoExibicaoDto;
import com.gustavosouza.votacao.model.VotosModel;
import com.gustavosouza.votacao.query.VotosQuery;
import com.gustavosouza.votacao.services.VotosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/votos")
public class VotoController {

    private final VotosService votosService;

    @PostMapping("/{idPauta}")
    public ResponseEntity<VotoExibicaoDto> cadastrarVoto(@RequestBody @Valid VotoCadastroDto dto, @PathVariable Long idPauta, Authentication auth) {
        VotoExibicaoDto voto = votosService.cadastrarVoto(dto, idPauta, auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(voto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVoto(@PathVariable Long id) {
        votosService.deletarVoto(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<Page<VotoExibicaoDto>> listarTodos(@Valid @ParameterObject VotosQuery votosQuery) {
        PageRequest pageable = PageRequest.of(votosQuery.getPage(), votosQuery.getSize(), Sort.by("idVoto").descending());
        return ResponseEntity.status(HttpStatus.OK).body(votosService.buscarTodosVotos(votosQuery, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VotoExibicaoDto> buscarPeloId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(votosService.buscarVotosPeloID(id));
    }

}
