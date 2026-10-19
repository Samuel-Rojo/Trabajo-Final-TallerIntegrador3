package com.tallerintegrador.universidad.dto;

import com.tallerintegrador.universidad.model.Evaluacion.TipoEvaluacion;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EvaluacionRequest(
        @NotNull Long cursoMateriaId,
        @NotNull LocalDate fecha,
        @NotNull TipoEvaluacion tipo,
        String descripcion) {}
