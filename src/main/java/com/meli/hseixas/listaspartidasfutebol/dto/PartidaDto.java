package com.meli.hseixas.listaspartidasfutebol.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PartidaDto {

    private Long id;

    @NotBlank
    private String clubeMandante;

    @NotBlank
    private String clubeVisitante;

    private int placarMandante;

    private int placarVisitante;

    @NotNull
    private LocalDateTime horario;

    @NotBlank
    private String nomeEstadio;
}
