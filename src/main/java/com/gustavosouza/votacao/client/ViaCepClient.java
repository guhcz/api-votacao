package com.gustavosouza.votacao.client;

import com.gustavosouza.votacao.client.dto.ViaCepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viaCepClient", url = "${viacep.base-url}")
public interface ViaCepClient {

    @GetMapping("/ws/{cep}/json/")
    ViaCepResponse buscar(@PathVariable String cep);

}
