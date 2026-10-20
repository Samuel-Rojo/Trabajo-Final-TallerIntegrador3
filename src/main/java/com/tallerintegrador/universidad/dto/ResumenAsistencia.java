package com.tallerintegrador.universidad.dto;

public record ResumenAsistencia(
        long total,
        long presentes,
        long ausentes,
        double porcentaje) {}
