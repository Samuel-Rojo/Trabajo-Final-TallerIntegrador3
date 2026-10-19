package com.tallerintegrador.universidad.repository;

import com.tallerintegrador.universidad.model.Alumno;
import com.tallerintegrador.universidad.model.Curso;
import com.tallerintegrador.universidad.model.Inscripcion.EstadoInscripcion;
import com.tallerintegrador.universidad.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    // RF22: ¿ya existe una inscripción activa de este alumno en este curso y ciclo?
    boolean existsByAlumnoAndCursoAndEstado(
            Alumno alumno, Curso curso, EstadoInscripcion estado);

    // RF22: cantidad de inscriptos activos, para comparar con la capacidad del curso
    long countByCursoAndEstado(
            Curso curso, EstadoInscripcion estado);

    // RF23: consultar por alumno, curso o ciclo lectivo
    List<Inscripcion> findByAlumno(Alumno alumno);
    List<Inscripcion> findByCurso(Curso curso);
}