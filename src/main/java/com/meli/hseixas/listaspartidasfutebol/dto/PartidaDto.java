package com.meli.hseixas.listaspartidasfutebol.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PartidaDto {

    private Long id;

    private String clubeMandante;

    private String clubeVisitante;

    private int placarMandante;

    private int placarVisitante;

    private LocalDateTime horario;

    private String nomeEstadio;
}
