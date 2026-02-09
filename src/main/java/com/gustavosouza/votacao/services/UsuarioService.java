package com.gustavosouza.votacao.services;

import com.gustavosouza.votacao.dto.UsuarioCadastroDto;
import com.gustavosouza.votacao.dto.UsuarioExibicaoDto;
import com.gustavosouza.votacao.exception.NoDateFoundException;
import com.gustavosouza.votacao.exception.NoUserFoundException;
import com.gustavosouza.votacao.model.UsuarioModel;
import com.gustavosouza.votacao.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioExibicaoDto salvarUsuario(UsuarioCadastroDto usuarioDto) {
        if (this.usuarioRepository.findByEmail(usuarioDto.email()) != null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        String senhaCriptografada = new BCryptPasswordEncoder().encode(usuarioDto.senha());
        UsuarioModel usuario = new UsuarioModel(usuarioDto.email(), senhaCriptografada, usuarioDto.role());

        this.usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).build();



//        UsuarioModel usuario = new UsuarioModel();
//        BeanUtils.copyProperties(usuarioDto, usuario);
//        return new UsuarioExibicaoDto(usuarioRepository.save(usuario));
    }

    public UsuarioExibicaoDto atualizarUsuarioPorId(Long id, UsuarioModel usuario) {
        UsuarioModel usuarioEntity = usuarioRepository.findById(id).orElseThrow(() -> new NoUserFoundException());
        UsuarioModel usuarioAtualizado = UsuarioModel.builder()
                .email(usuario.getEmail() != null ? usuario.getEmail() :
                        usuarioEntity.getEmail())
                .nome(usuario.getNome() != null ? usuario.getNome() :
                        usuarioEntity.getNome())
                .senha(usuario.getSenha() != null ? usuario.getSenha() :
                        usuarioEntity.getSenha())
                .dataNascimento(usuario.getDataNascimento() != null ? usuario.getDataNascimento() :
                        usuarioEntity.getDataNascimento())
                .idUsuario(usuarioEntity.getIdUsuario())
                .build();
        return new UsuarioExibicaoDto(usuarioRepository.save(usuarioAtualizado));
    }

    public void deletarUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public List<UsuarioExibicaoDto> buscarTodosUsuarios() {
        return usuarioRepository
                .findAll()
                .stream()
                .map(UsuarioExibicaoDto::new)
                .toList();
    }

    public UsuarioModel buscarPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario).orElseThrow(
                () -> new NoUserFoundException()
        );
    }

    public UsuarioExibicaoDto buscarUsuarioPorEmail(String email) {
        return new UsuarioExibicaoDto(usuarioRepository.findByEmail(email).orElseThrow(
                () -> new NoUserFoundException()
        ));
    }

    public List<UsuarioExibicaoDto> filtrarPelaDataNascimento(LocalDate dataInicial, LocalDate dataFinal) {
        List<UsuarioModel> usuario = usuarioRepository.findByDataNascimentoBetween(dataInicial, dataFinal);
        if (usuario.isEmpty()) {
            throw new NoDateFoundException();
        }
        return usuario
                .stream()
                .map(UsuarioExibicaoDto::new)
                .toList();
    }
}
