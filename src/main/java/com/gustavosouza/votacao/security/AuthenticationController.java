package com.gustavosouza.votacao.security;

import com.gustavosouza.votacao.dto.UsuarioCadastroDto;
import com.gustavosouza.votacao.model.UsuarioModel;
import com.gustavosouza.votacao.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid AuthenticationDto authenticationDto){
        var authToken = new UsernamePasswordAuthenticationToken(authenticationDto.email(), authenticationDto.senha());
        var auth = authenticationManager.authenticate(authToken);

        var usuario = (UserDetails) auth.getPrincipal();
        String token = tokenService.generateToken(usuario);
        
        return ResponseEntity.ok(new TokenResponse(token));

    }

    public record TokenResponse(String token){}

    @PostMapping("/register")
    public ResponseEntity<Void> register (@RequestBody @Valid UsuarioCadastroDto usuarioCadastroDto){
        if (this.usuarioRepository.findByEmail(usuarioCadastroDto.email()).isPresent()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        String senhaCriptografada = new BCryptPasswordEncoder().encode(usuarioCadastroDto.senha());
        UsuarioModel usuario = new UsuarioModel(usuarioCadastroDto.email(), senhaCriptografada, usuarioCadastroDto.role());

        this.usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
