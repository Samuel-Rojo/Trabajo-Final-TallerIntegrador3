package com.tallerintegrador.universidad.dto;

import jakarta.validation.constraints.NotNull;


public record CalificacionUpdateRequest(
        @NotNull Long docenteId,
        @NotNull Double nota,
        String observacion) {}
