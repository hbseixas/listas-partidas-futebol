package com.meli.hseixas.listaspartidasfutebol.repository;

import com.meli.hseixas.listaspartidasfutebol.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    List<Partida> findAllByNomeEstadioEqualsIgnoreCase(String nomeEstadio);
    List<Partida> findAllByClubeMandanteEqualsIgnoreCase(String nomeClube);
    List<Partida> findAllByClubeVisitanteEqualsIgnoreCase(String nomeClube);
}
