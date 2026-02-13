package com.gustavosouza.votacao.infra.services;

import com.gustavosouza.votacao.client.service.LocalidadeIbgeService;
import com.gustavosouza.votacao.dto.UsuarioCadastroDto;
import com.gustavosouza.votacao.dto.UsuarioExibicaoDto;
import com.gustavosouza.votacao.exception.NoDateFoundException;
import com.gustavosouza.votacao.exception.NoUserFoundException;
import com.gustavosouza.votacao.model.UsuarioModel;
import com.gustavosouza.votacao.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final LocalidadeIbgeService localidadeIbgeService;


    public UsuarioExibicaoDto salvarUsuario(UsuarioCadastroDto usuarioDto) {

        String senhaCriptografada = new BCryptPasswordEncoder().encode(usuarioDto.senha());

        UsuarioModel usuario = new UsuarioModel();
        BeanUtils.copyProperties(usuarioDto, usuario);
        usuario.setSenha(senhaCriptografada);

        var localizacao = localidadeIbgeService.resolverPorCep(usuarioDto.cep());
        usuario.setCep(localizacao.cep());
        usuario.setUf(localizacao.uf());
        usuario.setEstado(localizacao.estado());
        usuario.setCidade(localizacao.cidade());
        usuario.setMunicipioIbgeId(localizacao.municipioIbgeId());

        return new UsuarioExibicaoDto(usuarioRepository.save(usuario));
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

    public Page<UsuarioExibicaoDto> buscarTodosUsuarios(PageRequest pageable) {
        return usuarioRepository.findAll(pageable)
                .map(UsuarioExibicaoDto::new);
    }

    public UsuarioModel buscarPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario).orElseThrow(
                () -> new NoUserFoundException()
        );
    }

    public UsuarioExibicaoDto buscarUsuarioPorEmail(String email) {
        return new UsuarioExibicaoDto((UsuarioModel) usuarioRepository.findByEmail(email).orElseThrow(
                () -> new NoUserFoundException()
        ));
    }

    public Page<UsuarioExibicaoDto> filtrarPelaDataNascimento(LocalDate dataInicial, LocalDate dataFinal, Pageable pageable) {
        Page<UsuarioModel> usuario = usuarioRepository.findByDataNascimentoBetween(dataInicial, dataFinal, pageable);
        if (usuario.isEmpty()) {
            throw new NoDateFoundException();
        }
        return usuario.map(UsuarioExibicaoDto::new);
    }
}
