package com.tallerintegrador.universidad.dto;

import com.tallerintegrador.universidad.model.Asistencia.EstadoAsistencia;

import java.time.LocalDate;

public record AsistenciaDTO(
        Long id,
        Long inscripcionId,
        Long alumnoId,
        LocalDate fecha,
        EstadoAsistencia estado,
        String observacion) {}
