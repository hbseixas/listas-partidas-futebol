package com.meli.hseixas.listaspartidasfutebol.service;

import com.meli.hseixas.listaspartidasfutebol.dto.PartidaDto;
import com.meli.hseixas.listaspartidasfutebol.model.Partida;
import com.meli.hseixas.listaspartidasfutebol.repository.PartidaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PartidaServiceTest {

    @InjectMocks
    private PartidaService partidaService;

    @Mock
    private PartidaRepository partidaRepository;


    @Mock
    private LocalDateTime horario;

    private List<Partida> partidas = new ArrayList<>();

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        partidas.add(new Partida(1L, "Flamengo", "Fluminense", 2, 1, horario, "Maracanã"));
        partidas.add(new Partida(2L, "Vasco", "Botafogo", 2, 2, horario, "São Januário"));
        partidas.add(new Partida(3L, "Santos", "Flamengo", 0, 2, horario, "Vila Belmiro"));
    }

//    TESTES DO MÉTODO listarTodasAsPartidas()

    @Test
    void testListarTodasAsPartidas_QuandoExistemPartidas() {
        when(partidaRepository.findAll()).thenReturn(partidas);

        List<PartidaDto> partidasDto = partidaService.listarTodasAsPartidas();

        assertEquals(partidas.size(), partidasDto.size());
        assertEquals(3, partidasDto.size());
    }

    @Test
    void testListarTodasAsPartidas_QuandoNaoExistemPartidas() {
        when(partidaRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(ResponseStatusException.class, () -> partidaService.listarTodasAsPartidas());
    }

//    TESTES DO MÉTODO listarPartidaPorId()

    @Test
    void testListarPartidaPorId_QuandoExistePartida() {
        Partida partidaEsperada = partidas.get(0);

        when(partidaRepository.findById(partidaEsperada.getId())).thenReturn(Optional.of(partidaEsperada));

        PartidaDto partidaDto = partidaService.listarPartidaPorId(partidaEsperada.getId());

        assertNotNull(partidaDto);
        assertEquals(partidaEsperada.getId(), partidaDto.getId());
    }

    @Test
    void testListarPartidaPorId_QuandoNaoExistePartida() {
        Long idPartidaInexistente = 50L;

        when(partidaRepository.findById(idPartidaInexistente)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> partidaService.listarPartidaPorId(idPartidaInexistente));
    }

//    TESTES DO MÉTODO listarGoleadas()

    @Test
    void testListarGoleadas_QuandoExistemGoleadas() {
        partidas.add(new Partida(4L, "Barra Mansa", "Bangu", 4, 1, horario, "Leão do Sul"));
        partidas.add(new Partida(5L, "Volta Redonda", "Madureira", 3, 0, horario, "Raulino de Oliveira"));

        when(partidaRepository.findAll()).thenReturn(partidas);

        List<PartidaDto> goleadas = partidaService.listarGoleadas();

        for (PartidaDto partidaDto : goleadas){
            assertTrue(Math.abs(partidaDto.getPlacarMandante() - partidaDto.getPlacarVisitante()) >= 3);
        }
        assertEquals(2, goleadas.size());
    }

    @Test
    void testListarGoleadas_QuandoNaoExistemGoleadas() {
        when(partidaRepository.findAll()).thenReturn(partidas);

        List<PartidaDto> goleadas = partidaService.listarGoleadas();

        assertTrue(goleadas.isEmpty());
    }

    @Test
    void testListarGoleadas_QuandoNaoExistemPartidas() {
        when(partidaRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(ResponseStatusException.class, () -> partidaService.listarGoleadas());
    }

//    TESTES DO MÉTODO listarZeroGols()

    @Test
    void testListarZeroGols_QuandoExistemZeroGols() {
        partidas.add(new Partida(4L, "Barra Mansa", "Bangu", 0, 0, horario, "Leão do Sul"));
        partidas.add(new Partida(5L, "Volta Redonda", "Madureira", 0, 0, horario, "Raulino de Oliveira"));

        when(partidaRepository.findAll()).thenReturn(partidas);

        List<PartidaDto> zeroGols = partidaService.listarZeroGols();

        for (PartidaDto partidaDto : zeroGols){
            assertTrue(partidaDto.getPlacarMandante() == 0 && partidaDto.getPlacarVisitante() == 0);
        }
        assertEquals(2, zeroGols.size());
    }

    @Test
    void testListarZeroGols_QuandoNaoExistemZeroGols() {
        when(partidaRepository.findAll()).thenReturn(partidas);

        List<PartidaDto> goleadas = partidaService.listarZeroGols();

        assertTrue(goleadas.isEmpty());
    }

    @Test
    void testListarZeroGols_QuandoNaoExistemPartidas() {
        when(partidaRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(ResponseStatusException.class, () -> partidaService.listarZeroGols());
    }

//    TESTES DO MÉTODO listarEstadio(String nomeEstadio)

    @Test
    void testListarEstadio_QuandoExistemPartidasPorEstadio() {
        String nomeEstadio = "Maracanã";

        List<Partida> partidasPorEstadio = partidas.stream()
                .filter(partida -> partida.getNomeEstadio().equalsIgnoreCase(nomeEstadio))
                .toList();

        when(partidaRepository.findAllByNomeEstadioEqualsIgnoreCase(nomeEstadio)).thenReturn(partidasPorEstadio);

        List<PartidaDto> partidasPorEstadioDto = partidaService.listarEstadio(nomeEstadio);

        assertEquals(partidasPorEstadio.size(), partidasPorEstadioDto.size());
        assertEquals(1, partidasPorEstadioDto.size());
    }

    @Test
    void testListarEstadio_QuandoNaoExistemPartidasPorEstadio() {
        String nomeEstadio = "Estádio Inexistente";

        when(partidaRepository.findAllByNomeEstadioEqualsIgnoreCase(nomeEstadio)).thenReturn(new ArrayList<>());

        assertThrows(ResponseStatusException.class, () -> partidaService.listarEstadio(nomeEstadio));
    }

//    TESTES DO MÉTODO listarClube(String nomeClube, boolean clubeMandante, boolean clubeVisitante)

    @Test
    void testListarClube_QuandoExistemPartidasPorClubeMandante() {
        String nomeClube = "Flamengo";
        boolean clubeMandante = true;
        boolean clubeVisitante = false;

        List<Partida> partidasPorClubeMandante = partidas.stream()
                .filter(partida -> partida.getClubeMandante().equalsIgnoreCase(nomeClube))
                .toList();

        when(partidaRepository.findAllByClubeMandanteEqualsIgnoreCase(nomeClube)).thenReturn(partidasPorClubeMandante);

        List<PartidaDto> partidasPorClubeMandanteDto = partidaService.listarClube(nomeClube, clubeMandante, clubeVisitante);

        assertEquals(partidasPorClubeMandante.size(), partidasPorClubeMandanteDto.size());
        assertEquals(1, partidasPorClubeMandanteDto.size());
    }

    @Test
    void testListarClube_QuandoExistemPartidasPorClubeVisitante() {
        String nomeClube = "Fluminense";
        boolean clubeMandante = false;
        boolean clubeVisitante = true;

        List<Partida> partidasPorClubeVisitante = partidas.stream()
                .filter(partida -> partida.getClubeVisitante().equalsIgnoreCase(nomeClube))
                .toList();

        when(partidaRepository.findAllByClubeVisitanteEqualsIgnoreCase(nomeClube)).thenReturn(partidasPorClubeVisitante);

        List<PartidaDto> partidasPorClubeVisitanteDto = partidaService.listarClube(nomeClube, clubeMandante, clubeVisitante);

        assertEquals(partidasPorClubeVisitante.size(), partidasPorClubeVisitanteDto.size());
        assertEquals(1, partidasPorClubeVisitanteDto.size());
    }

    @Test
    void testListarClube_QuandoExistemPartidasPorClube() {
        String nomeClube = "Flamengo";
        boolean clubeMandante = true;
        boolean clubeVisitante = true;

        List<Partida> partidasPorClube = partidas.stream()
                .filter(partida -> partida.getClubeMandante().equalsIgnoreCase(nomeClube)
                        || partida.getClubeVisitante().equalsIgnoreCase(nomeClube))
                .toList();

        when(partidaRepository.findAllByClubeMandanteEqualsIgnoreCase(nomeClube)).thenReturn(partidasPorClube);

        List<PartidaDto> partidasPorClubeDto = partidaService.listarClube(nomeClube, clubeMandante, clubeVisitante);

        assertEquals(partidasPorClube.size(), partidasPorClubeDto.size());
        assertEquals(2, partidasPorClubeDto.size());
    }

    @Test
    void testListarClube_QuandoNaoExistemPartidasPorClube() {
        String nomeClube = "Clube Inexistente";
        boolean clubeMandante = true;
        boolean clubeVisitante = true;

        when(partidaRepository.findAllByClubeMandanteEqualsIgnoreCase(nomeClube)).thenReturn(new ArrayList<>());

        assertThrows(ResponseStatusException.class, () -> partidaService.listarClube(nomeClube, clubeMandante, clubeVisitante));
    }

//    TESTES DO MÉTODO cadastrarPartida(PartidaDto partidaDto)

    @Test
    void testCadastrarPartida_QuandoValidaArgumentos() {
        PartidaDto partidaDto = getPartidaDtoValida();
        partidaService.cadastrarPartida(partidaDto);

        verify(partidaRepository, times(1)).save(any());
    }

    @Test
    void testCadastrarPartida_QuandoInvalidaHora() {
        PartidaDto partidaDto = getPartidaDtoHoraInvalida();

        assertThrows(ResponseStatusException.class, () -> partidaService.cadastrarPartida(partidaDto));
        verify(partidaRepository, never()).save(any());
    }
//    TODO
//    teste com estadio por dia invalido e intervalo de dias inválido.


//    MÉTODOS AUXILIARES

    private static PartidaDto getPartidaDtoValida() {
        PartidaDto partidaDto = new PartidaDto();
        partidaDto.setClubeMandante("Barcelona");
        partidaDto.setClubeVisitante("Real Madri");
        partidaDto.setPlacarMandante(3);
        partidaDto.setPlacarVisitante(3);
        partidaDto.setHorario(LocalDateTime.of(2023, 10, 12, 20, 30, 00));
        partidaDto.setNomeEstadio("Camp Nou");
        return partidaDto;
    }

    private static PartidaDto getPartidaDtoHoraInvalida() {
        PartidaDto partidaDto = new PartidaDto();
        partidaDto.setClubeMandante("Barcelona");
        partidaDto.setClubeVisitante("Real Madri");
        partidaDto.setPlacarMandante(3);
        partidaDto.setPlacarVisitante(3);
        partidaDto.setHorario(LocalDateTime.of(2023, 10, 12, 23, 30, 00));
        partidaDto.setNomeEstadio("Camp Nou");
        return partidaDto;
    }

}
