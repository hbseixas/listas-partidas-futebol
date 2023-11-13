package com.meli.hseixas.listaspartidasfutebol.dto;

import jakarta.validation.constraints.*;
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

    @NotNull
    @PositiveOrZero
    private Integer placarMandante;

    @NotNull
    @PositiveOrZero
    private Integer placarVisitante;

    @NotNull
    @Past
    private LocalDateTime horario;

    @NotBlank
    private String nomeEstadio;
}
