package com.gustavosouza.votacao.controllers;

import com.gustavosouza.votacao.dto.UsuarioAtualizacaoDto;
import com.gustavosouza.votacao.dto.UsuarioCadastroDto;
import com.gustavosouza.votacao.dto.UsuarioExibicaoDto;
import com.gustavosouza.votacao.model.UsuarioModel;
import com.gustavosouza.votacao.query.UsuarioQuery;
import com.gustavosouza.votacao.services.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;


    @PostMapping
    public ResponseEntity<UsuarioExibicaoDto> salvarUsuario(@RequestBody @Valid UsuarioCadastroDto usuarioDto) {
        UsuarioExibicaoDto usuario = usuarioService.salvarUsuario(usuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioExibicaoDto> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioAtualizacaoDto usuarioAtualizacaoDto) {
        UsuarioExibicaoDto usuario = usuarioService.atualizarUsuarioPorId(id, usuarioAtualizacaoDto);
        return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }


    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> deletarUsuarioPorEmail(@PathVariable String email) {
        usuarioService.deletarUsuarioPorEmail(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioExibicaoDto>> buscarTodosUsuario(@Valid @ParameterObject UsuarioQuery usuarioQuery) {
        PageRequest pageable = PageRequest.of(usuarioQuery.getPage(), usuarioQuery.getSize(), Sort.by("idUsuario").descending());
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarTodosUsuarios(usuarioQuery, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioExibicaoDto> buscarUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarPorId(id));
    }

}
