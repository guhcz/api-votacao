package com.gustavosouza.votacao.mapstruct;

import com.gustavosouza.votacao.client.service.LocalidadeIbgeService;
import com.gustavosouza.votacao.dto.UsuarioAtualizacaoDto;
import com.gustavosouza.votacao.dto.UsuarioCadastroDto;
import com.gustavosouza.votacao.dto.UsuarioExibicaoDto;
import com.gustavosouza.votacao.model.UsuarioModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "uf", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "cidade", ignore = true)
    UsuarioModel usuarioModel(UsuarioCadastroDto dto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "uf", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "cidade", ignore = true)
    void atualizarUsuario(UsuarioAtualizacaoDto usuarioAtualizacaoDto, @MappingTarget UsuarioModel usuarioModel);


    UsuarioExibicaoDto usuarioExibicaoDto(UsuarioModel usuarioModel);

}
