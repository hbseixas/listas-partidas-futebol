package com.meli.hseixas.listaspartidasfutebol.service;

import com.meli.hseixas.listaspartidasfutebol.dto.PartidaDto;
import com.meli.hseixas.listaspartidasfutebol.model.Partida;
import com.meli.hseixas.listaspartidasfutebol.repository.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PartidaService {

    @Autowired
    private PartidaRepository partidaRepository;

    public static final int INTERVALO_MAXIMO_EM_SEGUNDOS = 172800;

    public List<PartidaDto> listarTodasAsPartidas() {
        List<Partida> todasAsPartidas = partidaRepository.findAll();
        if (!todasAsPartidas.isEmpty()) {
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

    public List<PartidaDto> listarGoleadas() {
        List<Partida> todasAsPartidas = partidaRepository.findAll();
        List<Partida> goleadas = new ArrayList<>();
        if (!todasAsPartidas.isEmpty()) {
            for (Partida partida : todasAsPartidas){
                if (Math.abs(partida.getPlacarMandante() - partida.getPlacarVisitante()) >= 3){
                    goleadas.add(partida);
                }
            }
            return goleadas.stream()
                    .map(this::converterParaDto)
                    .toList();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public List<PartidaDto> listarZeroGols() {
        List<Partida> todasAsPartidas = partidaRepository.findAll();
        List<Partida> zeroGols = new ArrayList<>();
        if (!todasAsPartidas.isEmpty()) {
            for (Partida partida : todasAsPartidas){
                if (partida.getPlacarMandante() == 0 && partida.getPlacarVisitante() == 0){
                    zeroGols.add(partida);
                }
            }
            return zeroGols.stream()
                    .map(this::converterParaDto)
                    .toList();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public List<PartidaDto> listarEstadio(String nomeEstadio) {
        List<Partida> todasAsPartidas = partidaRepository.findAllByNomeEstadioEqualsIgnoreCase(nomeEstadio);
        if (!todasAsPartidas.isEmpty()) {
            return todasAsPartidas.stream()
                    .map(this::converterParaDto)
                    .toList();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public List<PartidaDto> listarClube(String nomeClube, boolean clubeMandante, boolean clubeVisitante) {
        List<Partida> partidasMandante = partidaRepository.findAllByClubeMandanteEqualsIgnoreCase(nomeClube);
        List<Partida> partidasVisitante = partidaRepository.findAllByClubeVisitanteEqualsIgnoreCase(nomeClube);
        List<Partida> partidasClube = new ArrayList<>();

        if (!(partidasMandante.isEmpty() && partidasVisitante.isEmpty())) {
            if (clubeMandante && !clubeVisitante){
                return partidasMandante.stream()
                        .map(this::converterParaDto)
                        .toList();
            }
            if (!clubeMandante && clubeVisitante){
                return partidasVisitante.stream()
                        .map(this::converterParaDto)
                        .toList();
            }
            partidasClube.addAll(partidasMandante);
            partidasClube.addAll(partidasVisitante);
            return partidasClube.stream()
                    .map(this::converterParaDto)
                    .toList();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public PartidaDto cadastrarPartida(PartidaDto partidaDto) {
        if (validarHorario(partidaDto) && validarEstadioPorDia(partidaDto) && validarIntervaloDeDiasPorClube(partidaDto)) {
            Partida partida = converterParaEntity(partidaDto, new Partida());
            partidaRepository.save(partida);
            partidaDto.setId(partida.getId());
            return partidaDto;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public PartidaDto alterarPartida(Long id, PartidaDto partidaDto) {
        Optional<Partida> partidaOptional = partidaRepository.findById(id);
        if (partidaOptional.isPresent()) {
            if (validarHorario(partidaDto) && validarEstadioPorDia(partidaDto) && validarIntervaloDeDiasPorClube(partidaDto)){
                Partida partida = partidaOptional.get();
                partidaRepository.save(converterParaEntity(partidaDto, partida));
                partidaDto.setId(id);
                return partidaDto;
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public void excluirPartida(Long id) {
        Optional<Partida> partidaOptional = partidaRepository.findById(id);
        if (partidaOptional.isPresent()) {
            partidaRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public PartidaDto converterParaDto(Partida partida) {
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

    private Partida converterParaEntity(PartidaDto partidaDto, Partida partida) {
        partida.setClubeMandante(partidaDto.getClubeMandante());
        partida.setClubeVisitante(partidaDto.getClubeVisitante());
        partida.setPlacarMandante(partidaDto.getPlacarMandante());
        partida.setPlacarVisitante(partidaDto.getPlacarVisitante());
        partida.setNomeEstadio(partidaDto.getNomeEstadio());
        partida.setHorario(partidaDto.getHorario());
        return partida;
    }

    public boolean validarHorario(PartidaDto partidaDto){
        return partidaDto.getHorario().getHour() >= 8 && partidaDto.getHorario().getHour() < 22;
    }

    public boolean validarEstadioPorDia(PartidaDto partidaDto) {
        LocalDate dataPartidaDto = partidaDto.getHorario().toLocalDate();
        List<Partida> partidasPorEstadio = partidaRepository.findAllByNomeEstadioEqualsIgnoreCase(partidaDto.getNomeEstadio());
        for (Partida partida : partidasPorEstadio){
            LocalDate dataPartida = partida.getHorario().toLocalDate();
            if (dataPartida.equals(dataPartidaDto)){
                return false;
            }
        }
        return true;
    }

    public boolean validarIntervaloDeDiasPorClube(PartidaDto partidaDto) {
        List<Partida> partidasDosClubes = obterPartidasDosClubes(partidaDto);
        for (Partida partida : partidasDosClubes) {
            if (isIntervaloValidoEntrePartidas(partidaDto, partida)){
                return false;
            }
        }
        return true;
    }

    private List<Partida> obterPartidasDosClubes (PartidaDto partidaDto){
        List<Partida> todasAsPartidas = partidaRepository.findAll();
        List<Partida> partidasDosClubes = new ArrayList<>();
        for (Partida partida : todasAsPartidas){
            if (isClubeEnvolvidoNaPartida(partida, partidaDto.getClubeMandante()) ||
                    isClubeEnvolvidoNaPartida(partida,partidaDto.getClubeVisitante())){
                partidasDosClubes.add(partida);
            }
        }
        return partidasDosClubes;
    }

    private boolean isClubeEnvolvidoNaPartida(Partida partida, String clube){
        return partida.getClubeMandante().equalsIgnoreCase(clube) ||
                partida.getClubeVisitante().equalsIgnoreCase(clube);
    }

    private boolean isIntervaloValidoEntrePartidas(PartidaDto partidaDto, Partida partida){
        Duration intervalo = Duration.between(partidaDto.getHorario(), partida.getHorario());
        return Math.abs(intervalo.toSeconds()) <= INTERVALO_MAXIMO_EM_SEGUNDOS;
    }
}
