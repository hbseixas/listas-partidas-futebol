package com.meli.hseixas.listaspartidasfutebol.controller;

import com.meli.hseixas.listaspartidasfutebol.dto.PartidaDto;
import com.meli.hseixas.listaspartidasfutebol.model.Partida;
import com.meli.hseixas.listaspartidasfutebol.service.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("partidas")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;

    @GetMapping
    public ResponseEntity<List<PartidaDto>> listarTodasAsPartidas(){
        var todasAsPartidas = partidaService.listarTodasAsPartidas();
        return ResponseEntity.ok(todasAsPartidas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidaDto> listarPartidaPorId(@PathVariable Long id){
        var partidaPorId = partidaService.listarPartidaPorId(id);
        return ResponseEntity.ok(partidaPorId);
    }

    @GetMapping("/goleadas")
    public ResponseEntity<List<PartidaDto>> listarGoleadas(){
        var goleadas = partidaService.listarGoleadas();
        return ResponseEntity.ok(goleadas);
    }

    @GetMapping("/zerogols")
    public ResponseEntity<List<PartidaDto>> listarZeroGols(){
        var zeroGols = partidaService.listarZeroGols();
        return ResponseEntity.ok(zeroGols);
    }

    @GetMapping("/estadio/{nomeEstadio}")
    public ResponseEntity<List<PartidaDto>> listarEstadio(@PathVariable String nomeEstadio){
        var estadio = partidaService.listarEstadio(nomeEstadio);
        return ResponseEntity.ok(estadio);
    }

    @PostMapping
    public ResponseEntity<PartidaDto> cadastrarPartida(@RequestBody PartidaDto partidaDto) {
        var partidaCadastrada = partidaService.cadastrarPartida(partidaDto);
        return new ResponseEntity<>(partidaCadastrada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidaDto> alterarPartida(@PathVariable Long id, @RequestBody PartidaDto partidaDto) {
        var partidaAlterada = partidaService.alterarPartida(id, partidaDto);
        return ResponseEntity.ok(partidaAlterada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Partida> excluirPartida(@PathVariable Long id) {
        partidaService.excluirPartida(id);
        return ResponseEntity.noContent().build();
    }
}
