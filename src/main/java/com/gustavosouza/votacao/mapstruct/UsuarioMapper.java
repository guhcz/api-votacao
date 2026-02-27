package com.gustavosouza.votacao.mapstruct;

import com.gustavosouza.votacao.client.service.LocalidadeIbgeService;
import com.gustavosouza.votacao.dto.UsuarioCadastroDto;
import com.gustavosouza.votacao.dto.UsuarioExibicaoDto;
import com.gustavosouza.votacao.model.UsuarioModel;
import lombok.AllArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(componentModel = "spring")
@AllArgsConstructor
public abstract class UsuarioMapper {

    private BCryptPasswordEncoder passwordEncoder;
    private LocalidadeIbgeService localidadeIbgeService;

    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "uf", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "cidade", ignore = true)
    public abstract UsuarioModel usuarioModel (UsuarioCadastroDto usuarioCadastroDto);

    public abstract UsuarioExibicaoDto usuarioExibicaoDto (UsuarioModel usuarioModel);

    @AfterMapping
    protected void preencherRegras(UsuarioCadastroDto usuarioCadastroDto, @MappingTarget UsuarioModel usuarioModel){
        usuarioModel.setSenha(passwordEncoder.encode(usuarioCadastroDto.senha()));

        var localizacao = localidadeIbgeService.resolverPorCep(usuarioCadastroDto.cep());
        usuarioModel.setCep(localizacao.cep());
        usuarioModel.setUf(localizacao.uf());
        usuarioModel.setEstado(localizacao.estado());
        usuarioModel.setCidade(localizacao.cidade());
    }

}
