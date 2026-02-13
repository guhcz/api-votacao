package com.gustavosouza.votacao.controllers;

import com.gustavosouza.votacao.dto.VotoCadastroDto;
import com.gustavosouza.votacao.model.UsuarioModel;
import com.gustavosouza.votacao.model.VotosModel;
import com.gustavosouza.votacao.services.VotosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/votos")
public class VotoController {

    private final VotosService votosService;

    @PostMapping("/cadastro")
    public ResponseEntity<VotosModel> cadastrarVoto(@RequestBody @Valid VotoCadastroDto votoCadastroDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(votosService.cadastrarVoto(votoCadastroDto));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<VotosModel> atualizarVoto(@PathVariable Long id, @RequestBody @Valid VotosModel votosModel) {
        return ResponseEntity.status(HttpStatus.OK).body(votosService.atualizarVoto(id, votosModel));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarVoto(@PathVariable Long id) {
        votosService.deletarVoto(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<VotosModel>> listarTodos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        size = Math.min(size, 100);
        PageRequest pageable = PageRequest.of(page, size, Sort.by("idVoto").descending());
        return ResponseEntity.status(HttpStatus.OK).body(votosService.buscarTodosVotos(pageable));
    }

    @GetMapping("/buscar/id/{id}")
    public ResponseEntity<VotosModel> buscarPeloId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(votosService.buscarVotosPeloID(id));
    }

    @GetMapping("/buscar/assunto/{assunto}")
    public ResponseEntity<Page<VotosModel>> buscarPeloAssunto(@PathVariable String assunto, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100));
        return ResponseEntity.status(HttpStatus.OK).body(votosService.buscarPeloAssunto(assunto, pageable));
    }

    @GetMapping("/buscar/data/{dataInicial}/{dataFinal}")
    public ResponseEntity<Page<VotosModel>> buscarPelaData(@PathVariable LocalDate dataInicial, @PathVariable LocalDate dataFinal, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100));
        return ResponseEntity.status(HttpStatus.OK).body(votosService.buscarPelaData(dataInicial, dataFinal, pageable));
    }

}
