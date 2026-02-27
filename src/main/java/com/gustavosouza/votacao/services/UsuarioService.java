package com.gustavosouza.votacao.services;

import com.gustavosouza.votacao.client.service.LocalidadeIbgeService;
import com.gustavosouza.votacao.dto.UsuarioCadastroDto;
import com.gustavosouza.votacao.dto.UsuarioExibicaoDto;
import com.gustavosouza.votacao.exception.NoDateFoundException;
import com.gustavosouza.votacao.exception.NoUserFoundException;
import com.gustavosouza.votacao.mapstruct.UsuarioMapper;
import com.gustavosouza.votacao.model.UsuarioModel;
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
    private final LocalidadeIbgeService localidadeIbgeService;
    private final UsuarioMapper usuarioMapper;


    public UsuarioExibicaoDto salvarUsuario(UsuarioCadastroDto usuarioDto) {

        log.debug("Iniciando cadastrar de usuário. nome={}, email={}",
                usuarioDto.nome(), usuarioDto.email());
        UsuarioModel usuario = usuarioMapper.usuarioModel(usuarioDto);

        UsuarioModel salvo = usuarioRepository.save(usuario);
        return usuarioMapper.usuarioExibicaoDto(salvo);

//        String senhaCriptografada = new BCryptPasswordEncoder().encode(usuarioDto.senha());
//
//        UsuarioModel usuario = new UsuarioModel();
//        BeanUtils.copyProperties(usuarioDto, usuario);
//        usuario.setSenha(senhaCriptografada);
//
//        var localizacao = localidadeIbgeService.resolverPorCep(usuarioDto.cep());
//        usuario.setCep(localizacao.cep());
//        usuario.setUf(localizacao.uf());
//        usuario.setEstado(localizacao.estado());
//        usuario.setCidade(localizacao.cidade());
//
////        var usuario = UsuarioModel.builder()
////                .cep(localizacao.cep())
////                .uf(localizacao.uf())
////                .estado(localizacao.estado())
////                .cidade(localizacao.cidade())
////                .build();
//
//
//
//        log.info("Usuário criado com sucesso. usuarioId={}, nome={}, email={}",
//                usuario.getIdUsuario(), usuario.getNome(), usuario.getEmail());
//
//        return new UsuarioExibicaoDto(usuarioRepository.save(usuario));
    }


    public UsuarioExibicaoDto atualizarUsuarioPorId(Long id, UsuarioModel usuario) {
        log.info("Atualizando usuário. usuarioId={}", id);
        UsuarioModel usuarioEntity = usuarioRepository.findById(id).orElseThrow(() -> new NoUserFoundException());

        log.debug("Usuario atual encontrada. usuarioId={}, nome={}", usuario.getIdUsuario(), usuario.getNome());

        UsuarioModel usuarioAtualizado = UsuarioModel.builder()
                .email(usuario.getEmail() != null ? usuario.getEmail() :
                        usuarioEntity.getEmail())
                .nome(usuario.getNome() != null ? usuario.getNome() :
                        usuarioEntity.getNome())
                .senha(usuario.getSenha() != null ? usuario.getSenha() :
                        usuarioEntity.getSenha())
                .dataNascimento(usuario.getDataNascimento() != null ? usuario.getDataNascimento() :
                        usuarioEntity.getDataNascimento())
                .cep(usuario.getCep() != null ? usuario.getCep() :
                        usuarioEntity.getCep())
                .uf(usuario.getUf()!= null ? usuario.getUf() :
                        usuarioEntity.getUf())
                .estado(usuario.getEstado() != null ? usuario.getEstado() :
                        usuarioEntity.getEstado())
                .cidade(usuario.getCidade() != null ? usuario.getCidade() :
                        usuarioEntity.getCidade())
                .role(usuario.getRole() != null ? usuario.getRole() :
                        usuarioEntity.getRole())
                .idUsuario(usuarioEntity.getIdUsuario())
                .build();

        log.info("Usuário atualizado com sucesso.");

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

        log.debug("Buscando usuário pela data de nascimento entre {} e {} (page={}, size={}, sort={})",
                dataInicial, dataFinal, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<UsuarioModel> usuario = usuarioRepository.findByDataNascimentoBetween(dataInicial, dataFinal, pageable);
        if (usuario.isEmpty()) {
            throw new NoDateFoundException();
        }

        log.debug("Usuários encontrados nos intervalor {}..{}. totalElementos={}, totalPages={}",
                dataInicial, dataFinal, usuario.getTotalElements(), usuario.getTotalPages());

        return usuario.map(UsuarioExibicaoDto::new);
    }
}
