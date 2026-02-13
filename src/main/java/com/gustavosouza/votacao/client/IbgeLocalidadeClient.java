package com.gustavosouza.votacao.client;

import com.gustavosouza.votacao.client.dto.IbgeMunicipioDto;
import com.gustavosouza.votacao.client.dto.IbgeUfDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@FeignClient(name = "ibgeLocalidadesClient", url = "${ibge.base-url}")
public interface IbgeLocalidadeClient {

    @GetMapping("/estados")
    List<IbgeUfDto> listarUfs(@RequestParam(value = "orderBy", required = false) String orderBy);

    @GetMapping("/estados/{uf}")
    IbgeUfDto buscarUf(@PathVariable String uf);

    @GetMapping("/estados/{uf}/municipios")
    List<IbgeMunicipioDto> listarMunicipiosDaUf(@PathVariable String uf, @RequestParam(value = "orderBy", required = false) String orderBy);

    @GetMapping("/municipios/{id}")
    IbgeMunicipioDto buscarMunicipio(@PathVariable Long id);


}
