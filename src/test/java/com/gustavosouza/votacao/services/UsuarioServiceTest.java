package com.gustavosouza.votacao.services;

import com.gustavosouza.votacao.client.dto.LocalidadeResolvida;
import com.gustavosouza.votacao.client.service.LocalidadeIbgeService;
import com.gustavosouza.votacao.dto.UsuarioCadastroDto;
import com.gustavosouza.votacao.dto.UsuarioExibicaoDto;
import com.gustavosouza.votacao.exception.NoCityFoundException;
import com.gustavosouza.votacao.exception.NoUserFoundException;
import com.gustavosouza.votacao.model.UsuarioModel;
import com.gustavosouza.votacao.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.management.RuntimeErrorException;
import java.time.LocalDate;

import static com.gustavosouza.votacao.security.UserRoles.MANAGER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private LocalidadeIbgeService localidadeIbgeService;

    @InjectMocks
    private UsuarioService usuarioService;


    @Test
    @DisplayName("Should save a new user sucessfully when everything is OK")
    void salvarUsuarioCase1() {

        UsuarioCadastroDto usuarioCadastroDto = new UsuarioCadastroDto(
                "Gustavo",
                "gustavo@gmail.com",
                "12345",
                LocalDate.of(2006, 2, 3),
                MANAGER,
                "17017260"
        );

        LocalidadeResolvida localidadeResolvida = new LocalidadeResolvida(
                "17017260",
                "SP",
                "Sao Paulo",
                "Bauru"
        );

        when(localidadeIbgeService.resolverPorCep(usuarioCadastroDto.cep()))
                .thenReturn(localidadeResolvida);

        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ArgumentCaptor<UsuarioModel> captor = ArgumentCaptor.forClass(UsuarioModel.class);

        UsuarioExibicaoDto retorno = usuarioService.salvarUsuario(usuarioCadastroDto);
        assertNotNull(retorno);

        verify(localidadeIbgeService, times(1)).resolverPorCep("17017260");
        verify(usuarioRepository, times(1)).save(captor.capture());

        UsuarioModel salvo = captor.getValue();
        assertNotNull(salvo);

        assertNotEquals("12345", salvo.getSenha());
        assertTrue(new BCryptPasswordEncoder().matches("12345", salvo.getSenha()));

        assertEquals("17017260", salvo.getCep());
        assertEquals("SP", salvo.getUf());
        assertEquals("Sao Paulo", salvo.getEstado());
        assertEquals("Bauru", salvo.getCidade());
    }


    @Test
    @DisplayName("Should throw Exception when user is invalid")
    void salvarUsuarioCase2() {
        UsuarioCadastroDto usuarioCadastroDto = new UsuarioCadastroDto(
                "Gustavo",
                "gustavo@gmail.com",
                "12345",
                LocalDate.of(2006, 2, 3),
                MANAGER,
                "00000000"
        );

        when(localidadeIbgeService.resolverPorCep(usuarioCadastroDto.cep()))
                .thenThrow(new NoCityFoundException());

        assertThrows(NoCityFoundException.class, () -> usuarioService.salvarUsuario(usuarioCadastroDto));

        verify(usuarioRepository, never()).save(any(UsuarioModel.class));
        verify(localidadeIbgeService, times(1)).resolverPorCep("00000000");

    }


    @Test
    @DisplayName("Should user be deleted sucessfully")
    void deletarUsuarioCase1() {
        String email = "gustavo@gmail.com";

        usuarioService.deletarUsuarioPorEmail(email);

        verify(usuarioRepository, times(1)).deleteByEmail(email);
        verifyNoMoreInteractions(usuarioRepository);
    }

}