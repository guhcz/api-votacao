package com.gustavosouza.votacao.client.service;

import com.gustavosouza.votacao.client.IbgeLocalidadeClient;
import com.gustavosouza.votacao.client.ViaCepClient;
import com.gustavosouza.votacao.client.dto.EstadoCidadeDto;
import com.gustavosouza.votacao.client.dto.LocalidadeResolvida;
import com.gustavosouza.votacao.exception.NoCityFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LocalidadeIbgeService {

    private final IbgeLocalidadeClient ibgeLocalidadeClient;
    private final ViaCepClient viaCepClient;

    public LocalidadeResolvida resolverPorCep(String cep){
        String cepLimpo = cep.replaceAll("\\\\D", "");

        var via = viaCepClient.buscar(cepLimpo);
        if (Boolean.TRUE.equals(via.erro()) || via.uf() == null || via.localidade() == null){
            throw new NoCityFoundException();
        }


        var ufDto = ibgeLocalidadeClient.buscarUf(via.uf());

        return new LocalidadeResolvida(
                cepLimpo,
                via.uf(),
                ufDto.nome(),
                via.localidade()
        );
    }

}
