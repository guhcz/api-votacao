package com.gustavosouza.votacao.dto;

import com.gustavosouza.votacao.security.UserRoles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UsuarioCadastroDto(
        @NotBlank(message = "O campo nome precisa ser preenchido!")
        String nome,

        @NotBlank(message = "O campo e-mail precisa ser preenchido!")
        @Email(message = "O e-mail está inválido!")
        String email,

        @NotBlank(message = "O campo senha precisa ser preenchido!")
        String senha,

        @NotNull(message = "O campo data de nascimento precisa ser preenchido!")
        LocalDate dataNascimento,

        @NotBlank(message = "O campo nome precisa ser preenchido!")
        UserRoles role
) {
}
