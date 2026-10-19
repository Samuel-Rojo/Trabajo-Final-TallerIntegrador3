package com.tallerintegrador.universidad.dto;

import com.tallerintegrador.universidad.model.Inscripcion.EstadoInscripcion;

import java.time.LocalDate;

public record InscripcionDTO(
        Long id,
        Long alumnoId,
        Long cursoId,
        LocalDate fecha,
        EstadoInscripcion estado) {}
