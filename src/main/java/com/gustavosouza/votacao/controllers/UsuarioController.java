package com.gustavosouza.votacao.controllers;

import com.gustavosouza.votacao.dto.UsuarioCadastroDto;
import com.gustavosouza.votacao.dto.UsuarioExibicaoDto;
import com.gustavosouza.votacao.model.UsuarioModel;
import com.gustavosouza.votacao.services.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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
        var usuario = usuarioService.salvarUsuario(usuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioExibicaoDto> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioModel usuario) {
        usuarioService.atualizarUsuarioPorId(id, usuario);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> deletarUsuarioPorEmail(@PathVariable String email) {
        usuarioService.deletarUsuarioPorEmail(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //ParameterObject
    @GetMapping
    public ResponseEntity<Page<UsuarioExibicaoDto>> buscarTodosUsuario(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataNascimentoInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataNascimentoFim,
            @NotNull(message = "Página deve ser informada.") @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        size = Math.min(size, 100);
        PageRequest pageable = PageRequest.of(page, size, Sort.by("idUsuario").descending());
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarTodosUsuarios(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioModel> buscarUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarPorId(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioExibicaoDto> buscarUsuarioPorEmail(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarUsuarioPorEmail(email));
    }

//    @GetMapping("/data-nascimento")
//    public ResponseEntity<Page<UsuarioExibicaoDto>> filtrarPelaDataNascimento(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        Pageable pageable = PageRequest.of(page, Math.min(size, 100));
//        return ResponseEntity.ok(usuarioService.filtrarPelaDataNascimento(dataInicial, dataFinal, pageable));
//    }
}
