package com.gustavosouza.votacao.services;

import com.gustavosouza.votacao.client.dto.LocalidadeResolvida;
import com.gustavosouza.votacao.client.service.LocalidadeIbgeService;
import com.gustavosouza.votacao.dto.UsuarioCadastroDto;
import com.gustavosouza.votacao.dto.UsuarioExibicaoDto;
import com.gustavosouza.votacao.exception.NoCityFoundException;
import com.gustavosouza.votacao.exception.NoUserFoundException;
import com.gustavosouza.votacao.mapstruct.UsuarioMapper;
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
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;


    @Test
    @DisplayName("Should save a new user sucessfully when everything is OK")
    void dadoParametrosValidos_quandoCriarUsuario_deveCriarComSucesso() {

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

        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setNome("Gustavo");
        usuarioModel.setEmail("gustavo@gmail.com");
        usuarioModel.setSenha("12345");
        usuarioModel.setDataNascimento(LocalDate.of(2006, 2, 3));
        usuarioModel.setRole(MANAGER);
        usuarioModel.setCep("17017260");

        UsuarioExibicaoDto usuarioExibicaoDto = new UsuarioExibicaoDto(
                1L,
                "Gustavo",
                "gustavo@gmail.com",
                LocalDate.of(2006, 2, 3)
        );

        when(localidadeIbgeService.resolverPorCep("17017260")).thenReturn(localidadeResolvida);
        when(usuarioMapper.usuarioModel(usuarioCadastroDto)).thenReturn(usuarioModel);
        when(passwordEncoder.encode("12345")).thenReturn("hash-123");
        when(usuarioRepository.save(any(UsuarioModel.class))).thenAnswer(inv -> inv.getArgument(0));
        when(usuarioMapper.usuarioExibicaoDto(any(UsuarioModel.class))).thenReturn(usuarioExibicaoDto);

        ArgumentCaptor<UsuarioModel> captor = ArgumentCaptor.forClass(UsuarioModel.class);

        UsuarioExibicaoDto retorno = usuarioService.salvarUsuario(usuarioCadastroDto);

        assertNotNull(retorno);

        verify(localidadeIbgeService, times(1)).resolverPorCep("17017260");
        verify(usuarioMapper, times(1)).usuarioModel(usuarioCadastroDto);
        verify(usuarioRepository, times(1)).save(captor.capture());
        verify(usuarioMapper, times(1)).usuarioExibicaoDto(any(UsuarioModel.class));
        verify(passwordEncoder, times(1)).encode("12345");

        UsuarioModel salvo = captor.getValue();
        assertNotNull(salvo);

        assertNotEquals("12345", salvo.getSenha());
        assertEquals("hash-123", salvo.getSenha()); // ou o valor que você stubou
        verify(passwordEncoder).encode("12345");

        assertEquals("17017260", salvo.getCep());
        assertEquals("SP", salvo.getUf());
        assertEquals("Sao Paulo", salvo.getEstado());
        assertEquals("Bauru", salvo.getCidade());

    }


    @Test
    @DisplayName("Should user be deleted sucessfully")
    void dadoUsuarioValidos_quandoForDeletarUsuario_deveDeletarComSucesso() {
        String email = "gustavo@gmail.com";

        usuarioService.deletarUsuarioPorEmail(email);

        verify(usuarioRepository, times(1)).deleteByEmail(email);
        verifyNoMoreInteractions(usuarioRepository);
    }

}