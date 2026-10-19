package com.tallerintegrador.universidad.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "inscripciones")
public class Inscripcion {

    public enum EstadoInscripcion { ACTIVA, ANULADA }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInscripcion;

    @ManyToOne
    @JoinColumn(name= "idAlumno", nullable = false)
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name= "idCurso", nullable = false)
    private Curso curso;

    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    private EstadoInscripcion estado = EstadoInscripcion.ACTIVA;

    public void anular() {
        this.estado = EstadoInscripcion.ANULADA;
    }
}



