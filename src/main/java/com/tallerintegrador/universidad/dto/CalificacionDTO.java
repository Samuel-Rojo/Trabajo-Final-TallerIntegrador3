package com.tallerintegrador.universidad.dto;



public record CalificacionDTO(
        Long id,
        Long inscripcionId,
        Long evaluacionId,
        Long alumnoId,
        Double nota,
        String observaciones) {}
