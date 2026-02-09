package com.gustavosouza.votacao.services;

import com.gustavosouza.votacao.dto.VotoCadastroDto;
import com.gustavosouza.votacao.exception.NoDateFoundException;
import com.gustavosouza.votacao.exception.NoVoteFoundException;
import com.gustavosouza.votacao.model.VotosModel;
import com.gustavosouza.votacao.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VotosService {

    private final VotoRepository votoRepository;

    public VotosModel cadastrarVoto(VotoCadastroDto votoCadastroDto) {
        VotosModel votos = new VotosModel();
        BeanUtils.copyProperties(votoCadastroDto, votos);
        return votoRepository.save(votos);
    }

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

    public List<VotosModel> buscarTodosVotos() {
        return votoRepository.findAll();
    }

    public VotosModel buscarVotosPeloID(Long id) {
        return votoRepository.findById(id).orElseThrow(
                () -> new NoVoteFoundException()
        );
    }

    public List<VotosModel> buscarPeloAssunto(String assuntoVotado) {
        return votoRepository.findByAssuntoVotado(assuntoVotado).orElseThrow(
                () -> new NoVoteFoundException()
        );
    }

    public List<VotosModel> buscarPelaData(LocalDate dataInicial, LocalDate dataFinal) {
        List<VotosModel> votos = votoRepository.findByDataVotoBetween(dataInicial, dataFinal);

        if (votos.isEmpty()) {
            throw new NoDateFoundException();
        } else {
            return votos;
        }
    }
}
