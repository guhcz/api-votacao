package com.gustavosouza.votacao.mapstruct;

import com.gustavosouza.votacao.dto.VotoCadastroDto;
import com.gustavosouza.votacao.dto.VotoExibicaoDto;
import com.gustavosouza.votacao.model.VotosModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VotosMapper {

    @Mapping(target = "idVoto", ignore = true)
    @Mapping(target = "usuarioModel", ignore = true)
    @Mapping(target = "pautaModel", ignore = true)
    public VotosModel votosModel(VotoCadastroDto votoCadastroDto);

    @Mapping(source = "pautaModel.idPauta", target = "idPauta")
    @Mapping(source = "usuarioModel.idUsuario", target = "idUsuario")
    public VotoExibicaoDto votoExibicaoDto(VotosModel votosModel);

}
