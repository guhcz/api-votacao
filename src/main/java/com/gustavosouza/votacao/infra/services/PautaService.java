package com.gustavosouza.votacao.infra.services;

import com.gustavosouza.votacao.dto.PautaCadastroDto;
import com.gustavosouza.votacao.dto.PautaExibicaoDto;
import com.gustavosouza.votacao.exception.NoAgendaFoundException;
import com.gustavosouza.votacao.model.PautaModel;
import com.gustavosouza.votacao.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PautaService {

    private final PautaRepository repository;

    public PautaExibicaoDto criarPauta(PautaCadastroDto pautaDto) {
        PautaModel pauta = new PautaModel();
        BeanUtils.copyProperties(pautaDto, pauta);
        return new PautaExibicaoDto(repository.save(pauta));
    }

    public PautaExibicaoDto atualizarPauta(Long idPauta, PautaModel pauta) {
        PautaModel pautaEntity = repository.findById(idPauta).orElseThrow(() -> new NoAgendaFoundException());
        PautaModel pautaAtualizada = PautaModel.builder()
                .assunto(pauta.getAssunto() != null ? pauta.getAssunto() :
                        pautaEntity.getAssunto())
                .quantidadeDeVotosNecessarios(pauta.getQuantidadeDeVotosNecessarios() != null ? pauta.getQuantidadeDeVotosNecessarios() :
                        pautaEntity.getQuantidadeDeVotosNecessarios())
                .dataInicio(pauta.getDataInicio() != null ? pauta.getDataInicio() :
                        pautaEntity.getDataInicio())
                .dataEncerramento(pauta.getDataEncerramento() != null ? pauta.getDataEncerramento() :
                        pautaEntity.getDataEncerramento())
                .idPauta(pautaEntity.getIdPauta())
                .build();

        return new PautaExibicaoDto(repository.save(pautaAtualizada));
    }

    public void excluirPauta(Long idPauta) {
        repository.deleteById(idPauta);
    }

    public void excluirPautaPeloAssunto(String assunto) {
        repository.deleteByAssunto(assunto);
    }

    public Page<PautaExibicaoDto> buscarTodasPautas(Pageable pageable) {
        return repository.findAll(pageable)
                .map(PautaExibicaoDto::new);
    }

    public PautaModel buscarPautaPeloId(Long id) {
        return repository.findById(id).orElseThrow(() -> new NoAgendaFoundException());
    }

    public PautaExibicaoDto buscarPautaPeloAssunto(String assunto) {
        return new PautaExibicaoDto(repository.findByAssunto(assunto)
                .orElseThrow(() -> new NoAgendaFoundException()
                ));
    }

    public Page<PautaExibicaoDto> buscarPautaPeloNumeroDeVotos(Integer quantidadeDeVotosNecessarios, Pageable pageable) {
        Page<PautaModel> pauta = repository.findByQuantidadeDeVotosNecessarios(quantidadeDeVotosNecessarios, pageable);

        if (pauta.isEmpty()) {
            throw new NoAgendaFoundException();
        }
        return pauta.map(PautaExibicaoDto::new);
    }

    public Page<PautaExibicaoDto> buscarPelaDataInicio(LocalDate primeiraData, LocalDate segundaData, Pageable pageable) {
        Page<PautaModel> pauta = repository.findByDataInicioBetween(primeiraData, segundaData, pageable);

        if (pauta.isEmpty()) {
            throw new NoAgendaFoundException();
        }
        return pauta.map(PautaExibicaoDto::new);
    }

    public Page<PautaExibicaoDto> buscarPelaDataEncerramento(LocalDate dataInicial, LocalDate dataFinal, Pageable pageable) {
        Page<PautaModel> pauta = repository.findByDataEncerramentoBetween(dataInicial, dataFinal, pageable);

        if (pauta.isEmpty()) {
            throw new NoAgendaFoundException();
        }
        return pauta.map(PautaExibicaoDto::new);
    }


}
