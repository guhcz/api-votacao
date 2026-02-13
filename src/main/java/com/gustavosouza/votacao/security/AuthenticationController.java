package com.gustavosouza.votacao.security;

import com.gustavosouza.votacao.exception.NoUserFoundException;
import com.gustavosouza.votacao.model.UsuarioModel;
import com.gustavosouza.votacao.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;


    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid AuthenticationDto authenticationDto) {
        var authToken = new UsernamePasswordAuthenticationToken(authenticationDto.email(), authenticationDto.senha());
        var auth = authenticationManager.authenticate(authToken);

        var token = tokenService.generateToken((UsuarioModel)auth.getPrincipal());

        return ResponseEntity.ok(new TokenResponse(token));

    }

}
