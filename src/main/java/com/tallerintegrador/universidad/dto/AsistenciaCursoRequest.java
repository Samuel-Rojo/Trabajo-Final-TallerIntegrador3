package com.tallerintegrador.universidad.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record AsistenciaCursoRequest(
        @NotNull Long docenteId,
        @NotNull Long cursoId,
        @NotNull LocalDate fecha,
        @NotEmpty @Valid List<AsistenciaItem> asistencias) {}
