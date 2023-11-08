package com.meli.hseixas.listaspartidasfutebol.service;

import com.meli.hseixas.listaspartidasfutebol.dto.PartidaDto;
import com.meli.hseixas.listaspartidasfutebol.model.Partida;
import com.meli.hseixas.listaspartidasfutebol.repository.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PartidaService {

    @Autowired
    private PartidaRepository partidaRepository;

    public List<PartidaDto> listarTodasAsPartidas() {
        List<Partida> todasAsPartidas = partidaRepository.findAll();
        if (!todasAsPartidas.isEmpty()){
            return todasAsPartidas.stream()
                    .map(this::converterParaDto)
                    .toList();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public PartidaDto listarPartidaPorId(Long id) {
        Optional<Partida> partidaOptional = partidaRepository.findById(id);
        if (partidaOptional.isPresent()) {
            Partida partida = partidaOptional.get();
            return converterParaDto(partida);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public PartidaDto cadastrarPartida(Partida partida) {
        partidaRepository.save(partida);
        return converterParaDto(partida);
    }

    public PartidaDto alterarPartida(Long id, Partida novaPartida) {
        Optional<Partida> partidaOptional = partidaRepository.findById(id);
        if (partidaOptional.isPresent()){
            Partida partida = partidaOptional.get();
            partida.setClubeMandante(novaPartida.getClubeMandante());
            partida.setClubeVisitante(novaPartida.getClubeVisitante());
            partida.setPlacarMandante(novaPartida.getPlacarMandante());
            partida.setPlacarVisitante(novaPartida.getPlacarVisitante());
            partida.setNomeEstadio(novaPartida.getNomeEstadio());
            partida.setHorario(novaPartida.getHorario());
            partidaRepository.save(partida);
            return converterParaDto(partida);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public void excluirPartida(Long id) {
        Optional<Partida> partidaOptional = partidaRepository.findById(id);
        if (partidaOptional.isPresent()){
            partidaRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public PartidaDto converterParaDto (Partida partida) {
        PartidaDto partidaDto = new PartidaDto();
        partidaDto.setId(partida.getId());
        partidaDto.setClubeMandante(partida.getClubeMandante());
        partidaDto.setClubeVisitante(partida.getClubeVisitante());
        partidaDto.setPlacarMandante(partida.getPlacarMandante());
        partidaDto.setPlacarVisitante(partida.getPlacarVisitante());
        partidaDto.setHorario(partida.getHorario());
        partidaDto.setNomeEstadio(partida.getNomeEstadio());
        return partidaDto;
    }
}
