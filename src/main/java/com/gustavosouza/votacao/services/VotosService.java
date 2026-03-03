package com.gustavosouza.votacao.services;

import com.gustavosouza.votacao.dto.VotoCadastroDto;
import com.gustavosouza.votacao.dto.VotoExibicaoDto;
import com.gustavosouza.votacao.exception.NoDateFoundException;
import com.gustavosouza.votacao.exception.NoVoteFoundException;
import com.gustavosouza.votacao.mapstruct.VotosMapper;
import com.gustavosouza.votacao.model.PautaModel;
import com.gustavosouza.votacao.model.UsuarioModel;
import com.gustavosouza.votacao.model.VotosModel;
import com.gustavosouza.votacao.query.VotosQuery;
import com.gustavosouza.votacao.repository.PautaRepository;
import com.gustavosouza.votacao.repository.UsuarioRepository;
import com.gustavosouza.votacao.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VotosService {

    private final VotoRepository votoRepository;
    private final PautaRepository pautaRepository;
    private final UsuarioRepository usuarioRepository;
    private final VotosMapper votosMapper;


    public VotoExibicaoDto cadastrarVoto(VotoCadastroDto votoCadastroDto, Long idPauta, Authentication auth) {
        log.debug("Iniciando cadastro de voto. idPauta={}, assunto={}, data={}",
                idPauta, votoCadastroDto.assuntoVotado(), votoCadastroDto.dataVoto());

        PautaModel pauta = pautaRepository.findById(idPauta)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pauta não encontrada"));

        String email = auth.getName();
        UsuarioModel usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

        VotosModel votos = votosMapper.votosModel(votoCadastroDto);

        votos.setPautaModel(pauta);
        votos.setUsuarioModel(usuario);

        VotosModel salvo = votoRepository.save(votos);

        log.info("Voto registrado. votoId={}, pautaId={}, assunto={}, data={}",
                votos.getIdVoto(), idPauta, votos.getAssuntoVotado(), votos.getDataVoto());

        return votosMapper.votoExibicaoDto(salvo);
    }


    public void deletarVoto(Long id) {
        Optional<VotosModel> voto = votoRepository.findById(id);

        if (voto.isPresent()) {
            votoRepository.delete(voto.get());
        } else {
            throw new NoVoteFoundException();
        }
    }

    public Page<VotoExibicaoDto> buscarTodosVotos(VotosQuery votosQuery, Pageable pageable) {
        return votoRepository.buscarPorFiltros(
                votosQuery.getAssunto(),
                votosQuery.getDataInicial(),
                votosQuery.getDataFinal(),
                pageable
        ).map(votosMapper::votoExibicaoDto);
    }

    public VotoExibicaoDto buscarVotosPeloID(Long id) {
        return votoRepository.findById(id)
                .map(votosMapper::votoExibicaoDto)
                .orElseThrow(() -> new NoVoteFoundException());
    }
}
