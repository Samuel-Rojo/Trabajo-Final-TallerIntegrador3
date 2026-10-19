package com.tallerintegrador.universidad.dto;

import jakarta.validation.constraints.NotNull;



public record CalificacionRequest(
        @NotNull Long docenteId,
        @NotNull Long inscripcionId,
        @NotNull Long evaluacionId,
        @NotNull Double nota,
        String observaciones) {}
