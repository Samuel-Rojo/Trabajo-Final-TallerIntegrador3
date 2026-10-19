package com.tallerintegrador.universidad.dto;

import com.tallerintegrador.universidad.model.Evaluacion.TipoEvaluacion;

import java.time.LocalDate;

public record EvaluacionDTO(
        Long id,
        Long cursoMateriaId,
        Long cursoId,
        LocalDate fecha,
        TipoEvaluacion tipo,
        String descripcion) {}
