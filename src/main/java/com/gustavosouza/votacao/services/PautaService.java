package com.gustavosouza.votacao.services;

import com.gustavosouza.votacao.dto.PautaCadastroDto;
import com.gustavosouza.votacao.dto.PautaExibicaoDto;
import com.gustavosouza.votacao.dto.StatusPauta;
import com.gustavosouza.votacao.exception.NoAgendaFoundException;
import com.gustavosouza.votacao.model.PautaModel;
import com.gustavosouza.votacao.repository.PautaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PautaService {

    private final PautaRepository repository;

    public PautaExibicaoDto criarPauta(PautaCadastroDto pautaDto) {
        PautaModel pauta = new PautaModel();
        BeanUtils.copyProperties(pautaDto, pauta);

        PautaModel salva = repository.save(pauta);

        log.info("Pauta criada com sucesso. pautaId={}, assunto={}, quantidadeDeVotos={}, dataInicio={}, dataEncerramento={}",
                salva.getIdPauta(), salva.getAssunto(), salva.getQuantidadeDeVotosNecessarios(), salva.getDataInicio(), salva.getDataEncerramento());

        return new PautaExibicaoDto(salva);
    }


    public PautaExibicaoDto atualizarPauta(Long idPauta, PautaModel pauta) {
        log.info("Atualizando pauta. pautaId={}", idPauta);

        PautaModel pautaEntity = repository.findById(idPauta).orElseThrow(() -> new NoAgendaFoundException());

        log.debug("Pauta atual encontrada. pautaId={}, statusAtual={}, dataEncerramentoAtual={}",
                pautaEntity.getIdPauta(), pautaEntity.getStatus(), pautaEntity.getDataEncerramento());

        PautaModel pautaAtualizada = PautaModel.builder()
                .assunto(pauta.getAssunto() != null ? pauta.getAssunto() :
                        pautaEntity.getAssunto())
                .quantidadeDeVotosNecessarios(pauta.getQuantidadeDeVotosNecessarios() != null ? pauta.getQuantidadeDeVotosNecessarios() :
                        pautaEntity.getQuantidadeDeVotosNecessarios())
                .dataInicio(pauta.getDataInicio() != null ? pauta.getDataInicio() :
                        pautaEntity.getDataInicio())
                .dataEncerramento(pauta.getDataEncerramento() != null ? pauta.getDataEncerramento() :
                        pautaEntity.getDataEncerramento())
                .status(pauta.getStatus() != null ? pauta.getStatus() :
                        pautaEntity.getStatus())
                .idPauta(pautaEntity.getIdPauta())
                .build();

        PautaModel salva = repository.save(pautaAtualizada);

        log.info("Pauta atualizada com sucesso. pautaId={}, statusFinal={}, dataEncerramentoFinal={}",
                salva.getIdPauta(), salva.getStatus(), salva.getDataEncerramento());

        return new PautaExibicaoDto(salva);
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

        log.debug("Buscando pautas por qtdVotosNecessarios={} (page={}, size={}, sort={})",
                quantidadeDeVotosNecessarios,
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<PautaModel> pauta = repository.findByQuantidadeDeVotosNecessarios(quantidadeDeVotosNecessarios, pageable);

        if (pauta.isEmpty()) {
            throw new NoAgendaFoundException();
        }

        log.debug("Pautas encontradas por qtdVotosNecessarios={}. totalElements={}, totalPages={}",
                quantidadeDeVotosNecessarios, pauta.getTotalElements(), pauta.getTotalPages());

        return pauta.map(PautaExibicaoDto::new);
    }


    public Page<PautaExibicaoDto> buscarPelaDataInicio(LocalDate primeiraData, LocalDate segundaData, Pageable pageable) {
        Page<PautaModel> pauta = repository.findByDataInicioBetween(primeiraData, segundaData, pageable);

        log.debug("Buscando pautas por dataInicio entre {} e {} (page={}, size={}, sort={})",
                primeiraData, segundaData,
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        if (pauta.isEmpty()) {
            throw new NoAgendaFoundException();
        }

        log.debug("Pautas encontradas no intervalo {}..{}. totalElements={}, totalPages={}",
                primeiraData, segundaData, pauta.getTotalElements(), pauta.getTotalPages());
        return pauta.map(PautaExibicaoDto::new);
    }


    public Page<PautaExibicaoDto> buscarPelaDataEncerramento(LocalDate dataInicial, LocalDate dataFinal, Pageable pageable) {

        log.debug("Buscando pautas por data de encerramento entre {} e {} (page={}, size={}, sort={})",
                dataInicial, dataFinal, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<PautaModel> pauta = repository.findByDataEncerramentoBetween(dataInicial, dataFinal, pageable);

        if (pauta.isEmpty()) {
            throw new NoAgendaFoundException();
        }

        log.debug("Pautas encerradas encontradas, no intervalo {}..{}. totalElements={}, totalPages={}",
                dataInicial, dataFinal, pauta.getTotalElements(), pauta.getTotalPages());

        return pauta.map(PautaExibicaoDto::new);
    }


    @Transactional
    public int fecharPautasVencidas(){

        LocalDate agora = LocalDate.now();

        int totalFechadas = 0;

        Pageable pageable = PageRequest.of(
                0,
                200,
                Sort.by("dataEncerramento").ascending()
                        .and(Sort.by("idPauta").ascending()
                        )
        );

        while (true) {
            log.debug("Buscando página {} de pautas vencidas", pageable.getPageNumber());
            Page<PautaModel> page = repository.findByStatusAndDataEncerramentoBefore(StatusPauta.ABERTA, agora, pageable);

            if (page.isEmpty()) break;

            var pautas = page.getContent();
            pautas.forEach(p -> p.setStatus(StatusPauta.FECHADA));
            repository.saveAll(pautas);

            totalFechadas += pautas.size();

            if (!page.hasNext()) break;
            pageable = page.nextPageable();
        }

        return totalFechadas;

    }


}
