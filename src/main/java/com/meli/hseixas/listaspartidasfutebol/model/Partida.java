package com.meli.hseixas.listaspartidasfutebol.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String clubeMandante;

    @Column
    private String clubeVisitante;

    @Column
    private Integer placarMandante;

    @Column
    private Integer placarVisitante;

    @Column
    private LocalDateTime horario;

    @Column
    private String nomeEstadio;
}
