package com.tallerintegrador.universidad.dto;

import jakarta.validation.constraints.NotNull;

public record InscripcionRequest(
        @NotNull Long alumnoId,
        @NotNull Long cursoId
       ) {}
