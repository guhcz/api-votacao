package com.gustavosouza.votacao.services;

import com.gustavosouza.votacao.dto.VotoCadastroDto;
import com.gustavosouza.votacao.exception.NoDateFoundException;
import com.gustavosouza.votacao.exception.NoVoteFoundException;
import com.gustavosouza.votacao.model.PautaModel;
import com.gustavosouza.votacao.model.UsuarioModel;
import com.gustavosouza.votacao.model.VotosModel;
import com.gustavosouza.votacao.repository.PautaRepository;
import com.gustavosouza.votacao.repository.UsuarioRepository;
import com.gustavosouza.votacao.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VotosService {

    private final VotoRepository votoRepository;
    private final PautaRepository pautaRepository;
    private final UsuarioRepository usuarioRepository;

    public VotosModel cadastrarVoto(VotoCadastroDto votoCadastroDto, Long idPauta, Authentication auth) {
        PautaModel pauta = pautaRepository.findById(idPauta)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pauta não encontrada"));

            String email = auth.getName();
            UsuarioModel usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

            VotosModel voto = new VotosModel();
            voto.setAssuntoVotado(votoCadastroDto.assuntoVotado());
            voto.setVoto(votoCadastroDto.voto());
            voto.setDataVoto(votoCadastroDto.dataVoto());

            voto.setPautaModel(pauta);
            voto.setUsuarioModel(usuario);

            return votoRepository.save(voto);
        }
//        VotosModel votos = new VotosModel();
//        BeanUtils.copyProperties(votoCadastroDto, votos);
//        return votoRepository.save(votos);

    public VotosModel atualizarVoto(Long idVoto, VotosModel votosModel) {
        VotosModel votoEntity = votoRepository.findById(idVoto).orElseThrow(() -> new NoVoteFoundException());
        VotosModel votoAtualizado = VotosModel.builder()
                .assuntoVotado(votosModel.getAssuntoVotado() != null ? votosModel.getAssuntoVotado() :
                        votoEntity.getAssuntoVotado())
                .voto(votosModel.getVoto() != null ? votosModel.getVoto() :
                        votoEntity.getVoto())
                .dataVoto(votosModel.getDataVoto() != null ? votosModel.getDataVoto() :
                        votoEntity.getDataVoto())
                .idVoto(votoEntity.getIdVoto())
                .build();

        return votoRepository.save(votoAtualizado);
    }

    public void deletarVoto(Long id) {
        Optional<VotosModel> voto = votoRepository.findById(id);

        if (voto.isPresent()){
            votoRepository.delete(voto.get());
        } else {
            throw new NoVoteFoundException();
        }
    }

    public Page<VotosModel> buscarTodosVotos(Pageable pageable) {
        return votoRepository.findAll(pageable);
    }

    public VotosModel buscarVotosPeloID(Long id) {
        return votoRepository.findById(id).orElseThrow(
                () -> new NoVoteFoundException()
        );
    }

    public Page<VotosModel> buscarPeloAssunto(String assuntoVotado, Pageable pageable) {
        return votoRepository.findByAssuntoVotado(assuntoVotado, pageable).orElseThrow(
                () -> new NoVoteFoundException()
        );
    }

    public Page<VotosModel> buscarPelaData(LocalDate dataInicial, LocalDate dataFinal, Pageable pageable) {
        Page<VotosModel> votos = votoRepository.findByDataVotoBetween(dataInicial, dataFinal, pageable);

        if (votos.isEmpty()) {
            throw new NoDateFoundException();
        } else {
            return votos;
        }
    }
}
