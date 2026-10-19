package com.tallerintegrador.universidad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tallerintegrador.universidad.model.Evaluacion;
import com.tallerintegrador.universidad.model.CursoMateria;
import com.tallerintegrador.universidad.model.Curso;
import java.util.List;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {

    // RF37: evaluaciones de una materia de un curso
    List<Evaluacion> findByCursoMateria(CursoMateria cursoMateria);

    // RF37: todas las evaluaciones de un curso, las más recientes primero
    List<Evaluacion> findByCursoMateriaCursoOrderByFechaDesc(Curso curso);
}
