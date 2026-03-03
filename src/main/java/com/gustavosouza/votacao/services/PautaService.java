package com.gustavosouza.votacao.services;

import com.gustavosouza.votacao.dto.PautaAtualizacaoDto;
import com.gustavosouza.votacao.dto.PautaCadastroDto;
import com.gustavosouza.votacao.dto.PautaExibicaoDto;
import com.gustavosouza.votacao.dto.StatusPauta;
import com.gustavosouza.votacao.exception.NoAgendaFoundException;
import com.gustavosouza.votacao.mapstruct.PautaMapper;
import com.gustavosouza.votacao.model.PautaModel;
import com.gustavosouza.votacao.query.PautaQuery;
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

    private final PautaRepository pautaRepository;
    private final PautaMapper pautaMapper;


    public PautaExibicaoDto criarPauta(PautaCadastroDto pautaDto) {

        PautaModel pauta = pautaMapper.pautaModel(pautaDto);

        PautaModel salvo = pautaRepository.save(pauta);
        log.info("Pauta criada com sucesso. pautaId={}, assunto={}, quantidadeDeVotos={}, dataInicio={}, dataEncerramento={}",
                salvo.getIdPauta(), salvo.getAssunto(), salvo.getQuantidadeDeVotosNecessarios(), salvo.getDataInicio(), salvo.getDataEncerramento());

        return pautaMapper.pautaExibicaoDto(salvo);
    }


    public PautaExibicaoDto atualizarPauta(Long idPauta, PautaAtualizacaoDto pautaAtualizacaoDto) {

        log.info("Atualizando pauta. pautaId={}", idPauta);
        PautaModel pauta = pautaRepository.findById(idPauta)
                .orElseThrow(() -> new NoAgendaFoundException());
        log.debug("Pauta atual encontrada. pautaId={}, statusAtual={}, dataEncerramentoAtual={}",
                pauta.getIdPauta(), pauta.getStatus(), pauta.getDataEncerramento());

        pautaMapper.atualizarPauta(pautaAtualizacaoDto, pauta);

        PautaModel salvo = pautaRepository.save(pauta);

        log.info("Pauta atualizada com sucesso. pautaId={}, statusFinal={}, dataEncerramentoFinal={}",
                salvo.getIdPauta(), salvo.getStatus(), salvo.getDataEncerramento());

        return pautaMapper.pautaExibicaoDto(salvo);
    }


    public void excluirPauta(Long idPauta) {
        pautaRepository.deleteById(idPauta);
    }


    public void excluirPautaPeloAssunto(String assunto) {
        pautaRepository.deleteByAssunto(assunto);
    }


    public Page<PautaExibicaoDto> buscarTodasPautas(PautaQuery pautaQuery, Pageable pageable) {
        return pautaRepository.buscarPorFiltros(
                        pautaQuery.getAssunto(),
                        pautaQuery.getVotosNecessarios(),
                        pautaQuery.getDataInicioDe(),
                        pautaQuery.getDataInicioAte(),
                        pautaQuery.getDataEncerramentoDe(),
                        pautaQuery.getDataEncerramentoAte(),
                        pageable
                )
                .map(pautaMapper::pautaExibicaoDto);
    }


    public PautaExibicaoDto buscarPautaPeloId(Long id) {
        return pautaRepository.findById(id)
                .map(pautaMapper::pautaExibicaoDto)
                .orElseThrow(() -> new NoAgendaFoundException());
    }


    @Transactional
    public int fecharPautasVencidas() {

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
            Page<PautaModel> page = pautaRepository.findByStatusAndDataEncerramentoBefore(StatusPauta.ABERTA, agora, pageable);

            if (page.isEmpty()) break;

            var pautas = page.getContent();
            pautas.forEach(p -> p.setStatus(StatusPauta.FECHADA));
            pautaRepository.saveAll(pautas);

            totalFechadas += pautas.size();

            if (!page.hasNext()) break;
            pageable = page.nextPageable();
        }

        return totalFechadas;

    }


}
