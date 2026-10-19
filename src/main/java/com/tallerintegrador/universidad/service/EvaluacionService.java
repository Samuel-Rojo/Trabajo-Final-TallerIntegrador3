package com.tallerintegrador.universidad.service;

import com.tallerintegrador.universidad.dto.EvaluacionDTO;
import com.tallerintegrador.universidad.dto.EvaluacionRequest;
import com.tallerintegrador.universidad.dto.EvaluacionUpdateRequest;
import com.tallerintegrador.universidad.exception.RecursoNoEncontradoException;
import com.tallerintegrador.universidad.model.*;
import com.tallerintegrador.universidad.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;
    private final CursoMateriaRepository cursoMateriaRepository;   // de tus compañeros
    private final CursoRepository cursoRepository;                 // de tus compañeros

    // RF36 / RN14: la evaluación pertenece a una materia y a un curso (CursoMateria)
    @Transactional
    public EvaluacionDTO registrar(EvaluacionRequest req) {
        CursoMateria cursoMateria = cursoMateriaRepository.findById(req.cursoMateriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("materia del curso", req.cursoMateriaId()));

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setCursoMateria(cursoMateria);
        evaluacion.setFecha(req.fecha());
        evaluacion.setTipo(req.tipo());
        evaluacion.setDescripcion(req.descripcion());
        return aDTO(evaluacionRepository.save(evaluacion));
    }

    // RF37: consultar
    @Transactional(readOnly = true)
    public EvaluacionDTO buscarPorId(Long id) {
        return aDTO(buscar(id));
    }

    @Transactional(readOnly = true)
    public List<EvaluacionDTO> porCursoMateria(Long cursoMateriaId) {
        CursoMateria cursoMateria = cursoMateriaRepository.findById(cursoMateriaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("materia del curso", cursoMateriaId));
        return evaluacionRepository.findByCursoMateria(cursoMateria).stream().map(this::aDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<EvaluacionDTO> porCurso(Long cursoId) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("curso", cursoId));
        return evaluacionRepository.findByCursoMateriaCursoOrderByFechaDesc(curso)
                .stream().map(this::aDTO).toList();
    }

    // RF37: modificar (no se cambia la materia/curso para no romper las calificaciones ya cargadas)
    @Transactional
    public EvaluacionDTO modificar(Long id, EvaluacionUpdateRequest req) {
        Evaluacion evaluacion = buscar(id);
        evaluacion.setFecha(req.fecha());
        evaluacion.setTipo(req.tipo());
        evaluacion.setDescripcion(req.descripcion());
        return aDTO(evaluacion);
    }

    private Evaluacion buscar(Long id) {
        return evaluacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("evaluación", id));
    }

    private EvaluacionDTO aDTO(Evaluacion e) {
        return new EvaluacionDTO(
                e.getIdEvaluacion(),
                e.getCursoMateria().getIdCursoMateria(),
                e.getCursoMateria().getCurso().getIdCurso(),
                e.getFecha(),
                e.getTipo(),
                e.getDescripcion());
    }
}
