package com.gustavosouza.votacao.dto;

import com.gustavosouza.votacao.security.UserRoles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UsuarioAtualizacaoDto(
        String nome,
        @Email(message = "O e-mail está inválido!") String email,
        String senha,
        LocalDate dataNascimento,
        UserRoles role,
        @Size(min = 8, max = 8, message = "O CEP está inválido!") String cep
) {
}
