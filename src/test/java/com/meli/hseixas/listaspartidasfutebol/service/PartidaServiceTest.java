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

    private List<Partida> partidas = new ArrayList<>();

    private static final Long ID_PARTIDA_INEXISTENTE = 50L;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

//    TESTES DO MÉTODO listarTodasAsPartidas()
    @Test
    void testListarTodasAsPartidas_QuandoExistemPartidas() {
        addPartidas();

        when(partidaRepository.findAll()).thenReturn(partidas);

        List<PartidaDto> partidasDto = partidaService.listarTodasAsPartidas();

        assertEquals(partidas.size(), partidasDto.size());
        assertEquals(3, partidasDto.size());
    }

    @Test
    void testListarTodasAsPartidas_QuandoNaoExistemPartidas() {
        when(partidaRepository.findAll()).thenReturn(partidas);

        assertThrows(ResponseStatusException.class, () -> partidaService.listarTodasAsPartidas());
    }

//    TESTES DO MÉTODO listarPartidaPorId()
    @Test
    void testListarPartidaPorId_QuandoExistePartida() {
        addPartidas();
        Partida partidaEsperada = partidas.get(0);

        when(partidaRepository.findById(partidaEsperada.getId())).thenReturn(Optional.of(partidaEsperada));

        PartidaDto partidaDto = partidaService.listarPartidaPorId(partidaEsperada.getId());

        assertNotNull(partidaDto);
        assertEquals(partidaEsperada.getId(), partidaDto.getId());
    }

    @Test
    void testListarPartidaPorId_QuandoNaoExistePartida() {
        when(partidaRepository.findById(ID_PARTIDA_INEXISTENTE)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> partidaService.listarPartidaPorId(ID_PARTIDA_INEXISTENTE));
    }

//    TESTES DO MÉTODO listarGoleadas()
    @Test
    void testListarGoleadas_QuandoExistemGoleadas() {
        addPartidas();
        partidas.add(new Partida(4L, "Barra Mansa", "Bangu", 4, 1, LocalDateTime.of(2022, 10, 12, 20, 30, 00), "Leão do Sul"));
        partidas.add(new Partida(5L, "Volta Redonda", "Madureira", 3, 0, LocalDateTime.of(2022, 10, 12, 20, 30, 00), "Raulino de Oliveira"));

        when(partidaRepository.findAll()).thenReturn(partidas);

        List<PartidaDto> goleadas = partidaService.listarGoleadas();

        for (PartidaDto partidaDto : goleadas){
            assertTrue(Math.abs(partidaDto.getPlacarMandante() - partidaDto.getPlacarVisitante()) >= 3);
        }
        assertEquals(2, goleadas.size());
    }

    @Test
    void testListarGoleadas_QuandoNaoExistemGoleadas() {
        addPartidas();

        when(partidaRepository.findAll()).thenReturn(partidas);

        List<PartidaDto> goleadas = partidaService.listarGoleadas();

        assertTrue(goleadas.isEmpty());
    }

    @Test
    void testListarGoleadas_QuandoNaoExistemPartidas() {
        when(partidaRepository.findAll()).thenReturn(partidas);

        assertThrows(ResponseStatusException.class, () -> partidaService.listarGoleadas());
    }

//    TESTES DO MÉTODO listarZeroGols()
    @Test
    void testListarZeroGols_QuandoExistemZeroGols() {
        addPartidas();
        partidas.add(new Partida(4L, "Barra Mansa", "Bangu", 0, 0, LocalDateTime.of(2022, 10, 12, 20, 30, 00), "Leão do Sul"));
        partidas.add(new Partida(5L, "Volta Redonda", "Madureira", 0, 0, LocalDateTime.of(2022, 10, 12, 20, 30, 00), "Raulino de Oliveira"));

        when(partidaRepository.findAll()).thenReturn(partidas);

        List<PartidaDto> zeroGols = partidaService.listarZeroGols();

        for (PartidaDto partidaDto : zeroGols){
            assertTrue(partidaDto.getPlacarMandante() == 0 && partidaDto.getPlacarVisitante() == 0);
        }
        assertEquals(2, zeroGols.size());
    }

    @Test
    void testListarZeroGols_QuandoNaoExistemZeroGols() {
        addPartidas();

        when(partidaRepository.findAll()).thenReturn(partidas);

        List<PartidaDto> zeroGols = partidaService.listarZeroGols();

        assertTrue(zeroGols.isEmpty());
    }

    @Test
    void testListarZeroGols_QuandoNaoExistemPartidas() {
        when(partidaRepository.findAll()).thenReturn(partidas);

        assertThrows(ResponseStatusException.class, () -> partidaService.listarZeroGols());
    }

//    TESTES DO MÉTODO listarEstadio(String nomeEstadio)
    @Test
    void testListarEstadio_QuandoExistemPartidasPorEstadio() {
        addPartidas();
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

        when(partidaRepository.findAllByNomeEstadioEqualsIgnoreCase(nomeEstadio)).thenReturn(partidas);

        assertThrows(ResponseStatusException.class, () -> partidaService.listarEstadio(nomeEstadio));
    }

//    TESTES DO MÉTODO listarClube(String nomeClube, boolean clubeMandante, boolean clubeVisitante)
    @Test
    void testListarClube_QuandoExistemPartidasPorClubeMandante() {
        addPartidas();
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
        addPartidas();
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
        addPartidas();
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

        when(partidaRepository.findAllByClubeMandanteEqualsIgnoreCase(nomeClube)).thenReturn(partidas);

        assertThrows(ResponseStatusException.class, () -> partidaService.listarClube(nomeClube, clubeMandante, clubeVisitante));
    }

//    TESTES DO MÉTODO cadastrarPartida(PartidaDto partidaDto)
    @Test
    void testCadastrarPartida_QuandoValidaArgumentos() {
        PartidaDto partidaDto = getPartidaDtoValida();
        PartidaDto partidaCadastrada = partidaService.cadastrarPartida(partidaDto);

        assertEquals(partidaDto, partidaCadastrada);
        verify(partidaRepository, times(1)).save(any());
    }

    @Test
    void testCadastrarPartida_QuandoInvalidaHora() {
        PartidaDto partidaDto = getPartidaDtoHoraInvalida();

        assertThrows(ResponseStatusException.class, () -> partidaService.cadastrarPartida(partidaDto));
        verify(partidaRepository, never()).save(any());
    }

    @Test
    void testCadastrarPartida_QuandoInvalidaEstadioPorDia() {
        addPartidas();
        PartidaDto partidaDto = getPartidaDtoEstadioPorDiaInvalido();
        List<Partida> partidasPorEstadio = partidas.stream()
                .filter(partida -> partida.getNomeEstadio().equalsIgnoreCase(partidaDto.getNomeEstadio()))
                .toList();

        when(partidaRepository.findAllByNomeEstadioEqualsIgnoreCase(partidaDto.getNomeEstadio())).thenReturn(partidasPorEstadio);

        assertThrows(ResponseStatusException.class, () -> partidaService.cadastrarPartida(partidaDto));
        verify(partidaRepository, never()).save(any());
    }

    @Test
    void testCadastrarPartida_QuandoInvalidaIntervaloDeDiasPorClube() {
        addPartidas();
        PartidaDto partidaDto = getPartidaDtoIntervaloDeDiasPorClubeInvalido();
        List<Partida> partidasDosClubes = partidas.stream()
                .filter(partida -> partidaService.isClubeEnvolvidoNaPartida(partida, partidaDto.getClubeMandante())
                        || partidaService.isClubeEnvolvidoNaPartida(partida, partidaDto.getClubeVisitante()))
                .toList();

        when(partidaRepository.findAll()).thenReturn(partidasDosClubes);

        assertThrows(ResponseStatusException.class, () -> partidaService.cadastrarPartida(partidaDto));
        verify(partidaRepository, never()).save(any());
    }

//    TESTES DO MÉTODO alterarPartida(Long id, PartidaDto partidaDto)
    @Test
    void testAlterarPartida_QuandoValidaArgumentos() {
        addPartidas();
        Partida partidaEsperada = partidas.get(0);
        PartidaDto partidaDto = getPartidaDtoValida();

        when(partidaRepository.findById(partidaEsperada.getId())).thenReturn(Optional.of(partidaEsperada));

        PartidaDto partidaAlterada = partidaService.alterarPartida(partidaEsperada.getId(), partidaDto);

        assertNotNull(partidaEsperada);
        assertEquals(partidaDto, partidaAlterada);
        verify(partidaRepository, times(1)).save(any());
    }

    @Test
    void testAlterarPartida_QuandoInvalidaHora() {
        addPartidas();
        Partida partidaEsperada = partidas.get(0);
        PartidaDto partidaDto = getPartidaDtoHoraInvalida();

        when(partidaRepository.findById(partidaEsperada.getId())).thenReturn(Optional.of(partidaEsperada));

        assertNotNull(partidaEsperada);
        assertThrows(ResponseStatusException.class, () -> partidaService.alterarPartida(partidaEsperada.getId(), partidaDto));
        verify(partidaRepository, never()).save(any());
    }

    @Test
    void testAlterarPartida_QuandoInvalidaEstadioPorDia() {
        addPartidas();
        Partida partidaEsperada = partidas.get(0);
        PartidaDto partidaDto = getPartidaDtoEstadioPorDiaInvalido();
        List<Partida> partidasPorEstadio = partidas.stream()
                .filter(partida -> partida.getNomeEstadio().equalsIgnoreCase(partidaDto.getNomeEstadio()))
                .toList();

        when(partidaRepository.findById(partidaEsperada.getId())).thenReturn(Optional.of(partidaEsperada));
        when(partidaRepository.findAllByNomeEstadioEqualsIgnoreCase(partidaDto.getNomeEstadio())).thenReturn(partidasPorEstadio);

        assertNotNull(partidaEsperada);
        assertThrows(ResponseStatusException.class, () -> partidaService.alterarPartida(partidaEsperada.getId(), partidaDto));
        verify(partidaRepository, never()).save(any());
    }

    @Test
    void testAlterarPartida_QuandoInvalidaIntervaloDeDiasPorClube() {
        addPartidas();
        Partida partidaEsperada = partidas.get(0);
        PartidaDto partidaDto = getPartidaDtoIntervaloDeDiasPorClubeInvalido();
        List<Partida> partidasDosClubes = partidas.stream()
                .filter(partida -> partidaService.isClubeEnvolvidoNaPartida(partida, partidaDto.getClubeMandante())
                        || partidaService.isClubeEnvolvidoNaPartida(partida, partidaDto.getClubeVisitante()))
                .toList();

        when(partidaRepository.findById(partidaEsperada.getId())).thenReturn(Optional.of(partidaEsperada));
        when(partidaRepository.findAll()).thenReturn(partidasDosClubes);

        assertNotNull(partidaEsperada);
        assertThrows(ResponseStatusException.class, () -> partidaService.alterarPartida(partidaEsperada.getId(), partidaDto));
        verify(partidaRepository, never()).save(any());
    }

    @Test
    void testAlterarPartida_QuandoNaoExistePartida() {
        PartidaDto partidaDto = getPartidaDtoValida();

        when(partidaRepository.findById(ID_PARTIDA_INEXISTENTE)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> partidaService.alterarPartida(ID_PARTIDA_INEXISTENTE, partidaDto));
    }

//    TESTES DO MÉTODO excluirPartida(Long id)
    @Test
    void testExcluirPartida_QuandoExistePartida() {
        addPartidas();
        Partida partidaEsperada = partidas.get(0);

        when(partidaRepository.findById(partidaEsperada.getId())).thenReturn(Optional.of(partidaEsperada));

        partidaService.excluirPartida(partidaEsperada.getId());

        assertNotNull(partidaEsperada);
        verify(partidaRepository, times(1)).deleteById(partidaEsperada.getId());
    }

    @Test
    void testExcluirPartida_QuandoNaoExistePartida() {
        when(partidaRepository.findById(ID_PARTIDA_INEXISTENTE)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> partidaService.excluirPartida(ID_PARTIDA_INEXISTENTE));
    }

//    MÉTODOS AUXILIARES

    private void addPartidas() {
        partidas.add(new Partida(1L, "Flamengo", "Fluminense", 2, 1, LocalDateTime.of(2023, 10, 12, 20, 30, 00), "Maracanã"));
        partidas.add(new Partida(2L, "Vasco", "Botafogo", 2, 2, LocalDateTime.of(2023, 10, 12, 20, 30, 00), "São Januário"));
        partidas.add(new Partida(3L, "Santos", "Flamengo", 0, 2, LocalDateTime.of(2023, 10, 15, 20, 30, 00), "Vila Belmiro"));
    }

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

    private static PartidaDto getPartidaDtoEstadioPorDiaInvalido() {
        PartidaDto partidaDto = new PartidaDto();
        partidaDto.setClubeMandante("Barcelona");
        partidaDto.setClubeVisitante("Real Madri");
        partidaDto.setPlacarMandante(3);
        partidaDto.setPlacarVisitante(3);
        partidaDto.setHorario(LocalDateTime.of(2023, 10, 12, 19, 30, 00));
        partidaDto.setNomeEstadio("Maracanã");
        return partidaDto;
    }

    private static PartidaDto getPartidaDtoIntervaloDeDiasPorClubeInvalido() {
        PartidaDto partidaDto = new PartidaDto();
        partidaDto.setClubeMandante("Flamengo");
        partidaDto.setClubeVisitante("Botafogo");
        partidaDto.setPlacarMandante(3);
        partidaDto.setPlacarVisitante(3);
        partidaDto.setHorario(LocalDateTime.of(2023, 10, 13, 21, 30, 00));
        partidaDto.setNomeEstadio("Maracanã");
        return partidaDto;
    }
}
