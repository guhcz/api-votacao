package com.gustavosouza.votacao.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, VerificarToken verificarToken) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario").permitAll()
                        .requestMatchers(HttpMethod.POST, "/votos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/pauta").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/usuario/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/pauta/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/usuario/email/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/votos/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/pauta/id/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/pauta/assunto/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuario/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/votos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/pauta/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/pauta/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/pauta/resumo").permitAll()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(verificarToken, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
