package com.meli.hseixas.listaspartidasfutebol.controller;

import com.meli.hseixas.listaspartidasfutebol.dto.PartidaDto;
import com.meli.hseixas.listaspartidasfutebol.model.Partida;
import com.meli.hseixas.listaspartidasfutebol.service.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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

    @PostMapping
    public ResponseEntity<PartidaDto> cadastrarPartida(@RequestBody Partida partida, UriComponentsBuilder uriBuilder) {
        var partidaCadastrada = partidaService.cadastrarPartida(partida);
        URI uri = uriBuilder.path("/partidas/{id}").buildAndExpand(partidaCadastrada.getId()).toUri();
        return ResponseEntity.created(uri).body(partidaCadastrada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidaDto> alterarPartida(@PathVariable Long id, @RequestBody Partida partida) {
        var partidaAlterada = partidaService.alterarPartida(id, partida);
        return ResponseEntity.ok(partidaAlterada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Partida> excluirPartida(@PathVariable Long id) {
        partidaService.excluirPartida(id);
        return ResponseEntity.noContent().build();
    }
}
