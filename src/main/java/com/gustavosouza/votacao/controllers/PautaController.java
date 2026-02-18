package com.gustavosouza.votacao.controllers;

import com.gustavosouza.votacao.dto.PautaCadastroDto;
import com.gustavosouza.votacao.dto.PautaExibicaoDto;
import com.gustavosouza.votacao.model.PautaModel;
import com.gustavosouza.votacao.services.PautaService;
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

@RestController
@RequestMapping("/pauta")
@RequiredArgsConstructor
public class PautaController {

    private final PautaService pautaService;


    @PostMapping("/cadastrar")
    public ResponseEntity<PautaExibicaoDto> cadastrarPauta(@RequestBody @Valid PautaCadastroDto pautaDto){
        PautaExibicaoDto pauta = pautaService.criarPauta(pautaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pauta);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<PautaExibicaoDto> atualizarPauta(@PathVariable Long id, @RequestBody @Valid PautaModel pautaModel){
        pautaService.atualizarPauta(id, pautaModel);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/deletar/id/{id}")
    public ResponseEntity<Void> deletarPautaPorId(@PathVariable Long id){
        pautaService.excluirPauta(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/deletar/assunto/{assunto}")
    public ResponseEntity<Void> deletarPautaPorAssunto(@PathVariable String assunto){
        pautaService.excluirPautaPeloAssunto(assunto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping ("/buscar")
    public ResponseEntity<Page<PautaExibicaoDto>> buscarTodasPautas(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        size = Math.min(size, 100);
        PageRequest pageable = PageRequest.of(page, size, Sort.by("idPauta").descending());

        return ResponseEntity.status(HttpStatus.OK).body(pautaService.buscarTodasPautas(pageable));
    }

    @GetMapping("/buscar/id/{id}")
    public ResponseEntity<PautaModel> buscarPautaPeloId(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.buscarPautaPeloId(id));
    }

    @GetMapping("/buscar/assunto/{assunto}")
    public ResponseEntity<PautaExibicaoDto> buscarPautaPeloAssunto(@PathVariable String assunto){
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.buscarPautaPeloAssunto(assunto));
    }


    @GetMapping("/buscar/votos/{quantidadeDeVotosNecessarios}")
    public ResponseEntity<Page<PautaExibicaoDto>> buscarPautaPeloNumeroDeVotos(@RequestParam Integer quantidadeDeVotosNecessarios,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, Math.min(size, 100));
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.buscarPautaPeloNumeroDeVotos(quantidadeDeVotosNecessarios, pageable));
    }

    @GetMapping("/buscar/dataInicio/{primeiraData}/{segundaData}")
    public ResponseEntity<Page<PautaExibicaoDto>> buscarPelaDataInicio(@PathVariable LocalDate primeiraData, @PathVariable LocalDate segundaData, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, Math.min(size, 100));
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.buscarPelaDataInicio(primeiraData, segundaData, pageable));
    }

    @GetMapping("buscar/dataEncerramento/{dataInicial}/{dataFinal}")
    public ResponseEntity<Page<PautaExibicaoDto>> buscarPelaDataEncerramento(@PathVariable LocalDate dataInicial, @PathVariable LocalDate dataFinal, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, Math.min(size, 100));
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.buscarPelaDataEncerramento(dataInicial,dataFinal, pageable));
    }


}
