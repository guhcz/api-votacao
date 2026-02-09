package com.gustavosouza.votacao.controllers;

import com.gustavosouza.votacao.dto.UsuarioCadastroDto;
import com.gustavosouza.votacao.dto.UsuarioExibicaoDto;
import com.gustavosouza.votacao.model.UsuarioModel;
import com.gustavosouza.votacao.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;


    @PostMapping("/cadastrar")
    public UsuarioExibicaoDto salvarUsuario(@RequestBody @Valid UsuarioCadastroDto usuarioDto) {
        var usuario = usuarioService.salvarUsuario(usuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<UsuarioExibicaoDto> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioModel usuario) {
        usuarioService.atualizarUsuarioPorId(id, usuario);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @DeleteMapping("/deletar/{email}")
    public ResponseEntity<Void> deletarUsuarioPorEmail(@PathVariable String email) {
        usuarioService.deletarUsuarioPorEmail(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioExibicaoDto>> buscarTodosUsuario() {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarTodosUsuarios());
    }

    @GetMapping("/buscar/id/{id}")
    public ResponseEntity<UsuarioModel> buscarUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarPorId(id));
    }

    @GetMapping("/buscar/email/{email}")
    public ResponseEntity<UsuarioExibicaoDto> buscarUsuarioPorEmail(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarUsuarioPorEmail(email));
    }

    @GetMapping("/buscar/dataNascimento/{dataInicial}/{dataFinal}")
    public ResponseEntity<List<UsuarioExibicaoDto>> filtrarPelaDataNascimento(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal
    ) {
        return ResponseEntity.ok(usuarioService.filtrarPelaDataNascimento(dataInicial, dataFinal));
    }
}
