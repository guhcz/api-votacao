package com.gustavosouza.votacao.controllers;

import com.gustavosouza.votacao.dto.PautaAtualizacaoDto;
import com.gustavosouza.votacao.dto.PautaCadastroDto;
import com.gustavosouza.votacao.dto.PautaExibicaoDto;
import com.gustavosouza.votacao.dto.ResumoPautasDto;
import com.gustavosouza.votacao.model.PautaModel;
import com.gustavosouza.votacao.query.PautaQuery;
import com.gustavosouza.votacao.services.PautaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/pauta")
@RequiredArgsConstructor
public class PautaController {

    private final PautaService pautaService;


    @PostMapping
    public ResponseEntity<PautaExibicaoDto> cadastrarPauta(@RequestBody @Valid PautaCadastroDto pautaDto) {
        PautaExibicaoDto pauta = pautaService.criarPauta(pautaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pauta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PautaExibicaoDto> atualizarPauta(@PathVariable Long id, @RequestBody @Valid PautaAtualizacaoDto pautaAtualizacaoDto) {
        PautaExibicaoDto pauta = pautaService.atualizarPauta(id, pautaAtualizacaoDto);
        return ResponseEntity.status(HttpStatus.OK).body(pauta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPautaPorId(@PathVariable Long id) {
        pautaService.excluirPauta(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<Page<PautaExibicaoDto>> buscarTodasPautas(@Valid @ParameterObject PautaQuery pautaQuery) {
        PageRequest pageable = PageRequest.of(pautaQuery.getPage(), pautaQuery.getSize(), Sort.by("idPauta").descending());
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.buscarTodasPautas(pautaQuery, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PautaExibicaoDto> buscarPautaPeloId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.buscarPautaPeloId(id));
    }

    @GetMapping("/resumo")
    public ResumoPautasDto buscarResumo() {
        return pautaService.buscarResumo();
    }

}
