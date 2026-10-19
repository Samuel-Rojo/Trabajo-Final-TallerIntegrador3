package com.tallerintegrador.universidad.dto;

import com.tallerintegrador.universidad.model.Asistencia.EstadoAsistencia;
import jakarta.validation.constraints.NotNull;

public record AsistenciaItem(
        @NotNull Long inscripcionId,
        @NotNull EstadoAsistencia estado,
        String observacion) {}
