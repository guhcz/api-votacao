package com.gustavosouza.votacao.mapstruct;

import com.gustavosouza.votacao.dto.PautaAtualizacaoDto;
import com.gustavosouza.votacao.dto.PautaCadastroDto;
import com.gustavosouza.votacao.dto.PautaExibicaoDto;
import com.gustavosouza.votacao.model.PautaModel;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PautaMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idPauta", ignore = true)
    @Mapping(target = "votosModel", ignore = true)
    @Mapping(target = "status", ignore = true)
    public PautaModel pautaModel (PautaCadastroDto pautaCadastroDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idPauta", ignore = true)
    @Mapping(target = "votosModel", ignore = true)
    @Mapping(target = "status", ignore = true)
    void atualizarPauta(PautaAtualizacaoDto pautaAtualizacaoDto, @MappingTarget PautaModel pautaModel);

    public PautaExibicaoDto pautaExibicaoDto (PautaModel pautaModel);

}
