package com.gustavosouza.votacao.security;

import com.gustavosouza.votacao.exception.NoUserFoundException;
import com.gustavosouza.votacao.model.UsuarioModel;
import com.gustavosouza.votacao.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class VerificarToken extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        String token = recoverToken(request);

        if (token != null){
            String email = tokenService.validateAndGetSubject(token);

            UsuarioModel usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new NoUserFoundException());

            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);

    }

    private String recoverToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer")) return null;
        return header.substring(7);
    }

}
