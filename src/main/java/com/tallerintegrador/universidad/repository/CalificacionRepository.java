package com.tallerintegrador.universidad.repository;

import com.tallerintegrador.universidad.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tallerintegrador.universidad.model.Inscripcion;
import com.tallerintegrador.universidad.model.Calificacion;
import com.tallerintegrador.universidad.model.Evaluacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

    // RF38: una inscripción no debería tener dos notas en la misma evaluación
    boolean existsByInscripcionAndEvaluacion(Inscripcion inscripcion, Evaluacion evaluacion);

    // RF38/RF39: notas de una evaluación (todos los alumnos)
    List<Calificacion> findByEvaluacion(Evaluacion evaluacion);

    // RF40: notas de un alumno
    List<Calificacion> findByInscripcionAlumno(Alumno alumno);

    // RF44: promedio de un alumno (devuelve null si no tiene notas)
    @Query("select avg(c.nota) from Calificacion c where c.inscripcion.alumno = :alumno")
    Double promedioPorAlumno(@Param("alumno") Alumno alumno);
}
