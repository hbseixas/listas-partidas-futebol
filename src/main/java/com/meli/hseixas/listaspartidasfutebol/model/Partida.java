package com.meli.hseixas.listaspartidasfutebol.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Partida {

    @Id
    private Long id;

    private String clubeMandante;

    private String clubeVisitante;

    private int placarMandante;

    private int placarVisitante;

    private LocalDateTime horario;

    private String nomeEstadio;


}
