package com.gustavosouza.votacao.services;

import com.gustavosouza.votacao.client.service.LocalidadeIbgeService;
import com.gustavosouza.votacao.dto.UsuarioAtualizacaoDto;
import com.gustavosouza.votacao.dto.UsuarioCadastroDto;
import com.gustavosouza.votacao.dto.UsuarioExibicaoDto;
import com.gustavosouza.votacao.exception.NoDateFoundException;
import com.gustavosouza.votacao.exception.NoUserFoundException;
import com.gustavosouza.votacao.mapstruct.UsuarioMapper;
import com.gustavosouza.votacao.model.UsuarioModel;
import com.gustavosouza.votacao.query.UsuarioQuery;
import com.gustavosouza.votacao.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final LocalidadeIbgeService localidadeIbgeService;
    private final PasswordEncoder passwordEncoder;


    public UsuarioExibicaoDto salvarUsuario(UsuarioCadastroDto usuarioCadastroDto) {

        log.debug("Iniciando cadastrar de usuário. nome={}, email={}",
                usuarioCadastroDto.nome(), usuarioCadastroDto.email());

        UsuarioModel usuario = usuarioMapper.usuarioModel(usuarioCadastroDto);

        usuario.setSenha(passwordEncoder.encode(usuarioCadastroDto.senha()));

        var localizacao = localidadeIbgeService.resolverPorCep(usuarioCadastroDto.cep());
        usuario.setCep(localizacao.cep());
        usuario.setUf(localizacao.uf());
        usuario.setEstado(localizacao.estado());
        usuario.setCidade(localizacao.cidade());

        UsuarioModel salvo = usuarioRepository.save(usuario);

        log.info("Usuário criado com sucesso. usuarioId={}, nome={}, email={}",
                salvo.getIdUsuario(), salvo.getNome(), salvo.getEmail());

        return usuarioMapper.usuarioExibicaoDto(salvo);
    }

    public UsuarioExibicaoDto atualizarUsuarioPorId(Long id, UsuarioAtualizacaoDto usuarioAtualizacaoDto) {

        log.info("Atualizando usuário. usuarioId={}", id);
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoUserFoundException());

        log.debug("Usuario atual encontrado. usuarioId={}, nome={}", usuario.getIdUsuario(), usuario.getNome());

        usuarioMapper.atualizarUsuario(usuarioAtualizacaoDto, usuario);

        if (usuarioAtualizacaoDto.senha() != null && !usuarioAtualizacaoDto.senha().isBlank()){
            usuario.setSenha(passwordEncoder.encode(usuarioAtualizacaoDto.senha()));
        }

        if (usuarioAtualizacaoDto.cep() != null && !usuarioAtualizacaoDto.cep().isBlank()) {
            var localizacao = localidadeIbgeService.resolverPorCep(usuarioAtualizacaoDto.cep());
            usuario.setCep(localizacao.cep());
            usuario.setUf(localizacao.uf());
            usuario.setEstado(localizacao.estado());
            usuario.setCidade(localizacao.cidade());
        }

        UsuarioModel salvo = usuarioRepository.save(usuario);

        log.info("Usuário atualizado com sucesso.");

        return usuarioMapper.usuarioExibicaoDto(salvo);
    }

    public void deletarUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public Page<UsuarioExibicaoDto> buscarTodosUsuarios(UsuarioQuery usuarioQuery, Pageable pageable) {
        return usuarioRepository.buscarPorDataNascimento(
                        usuarioQuery.getEmail(),
                        usuarioQuery.getDataInicial(),
                        usuarioQuery.getDataFinal(),
                        pageable)
                .map(usuarioMapper::usuarioExibicaoDto);
    }

    public UsuarioExibicaoDto buscarPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .map(usuarioMapper::usuarioExibicaoDto)
                .orElseThrow(() -> new NoUserFoundException()
                );
    }
}
