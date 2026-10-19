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
@Table(name="Asistencia")
public class Asistencia {

    public enum EstadoAsistencia { PRESENTE, AUSENTE, TARDANZA, JUSTIFICADO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAsistencia;

    @ManyToOne
    @JoinColumn(name = "idInscrpcion", nullable = false)
    private Inscripcion inscripcion;

    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    private Asistencia.EstadoAsistencia estado = Asistencia.EstadoAsistencia.PRESENTE;

    private String observacion;


}
