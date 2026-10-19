package com.tallerintegrador.universidad.service;

import com.tallerintegrador.universidad.dto.InscripcionDTO;
import com.tallerintegrador.universidad.dto.InscripcionRequest;
import com.tallerintegrador.universidad.exception.RecursoNoEncontradoException;
import com.tallerintegrador.universidad.exception.ReglaNegocioException;
import com.tallerintegrador.universidad.model.*;
import com.tallerintegrador.universidad.model.Inscripcion.EstadoInscripcion;
import com.tallerintegrador.universidad.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final AlumnoRepository alumnoRepository;   // de tus compañeros
    private final CursoRepository cursoRepository;     // de tus compañeros

    // RF22
    @Transactional
    public InscripcionDTO inscribir(InscripcionRequest req) {
        // RN04: alumno y curso deben existir
        Alumno alumno = alumnoRepository.findById(req.alumnoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("alumno", req.alumnoId()));
        Curso curso = cursoRepository.findById(req.cursoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("curso", req.cursoId()));

        // RN04: el alumno debe estar activo.
        // String.valueOf funciona tanto si estado es String como si es un enum.
        // Cambiá "ACTIVO" por el valor exacto que usen tus compañeros (ACTIVO / ACTIVA).
        if (!"ACTIVO".equalsIgnoreCase(String.valueOf(alumno.getEstado()))) {
            throw new ReglaNegocioException("El alumno no está activo");
        }

        // RN01: no puede haber dos inscripciones activas al mismo curso
        if (inscripcionRepository.existsByAlumnoAndCursoAndEstado(
                alumno, curso, EstadoInscripcion.ACTIVA)) {
            throw new ReglaNegocioException("El alumno ya está inscripto en ese curso");
        }

        // RN02: el curso no puede superar su capacidad máxima
        long inscriptos = inscripcionRepository.countByCursoAndEstado(curso, EstadoInscripcion.ACTIVA);
        if (!curso.tieneCupo((int) inscriptos)) {
            throw new ReglaNegocioException("El curso no tiene cupo disponible");
        }

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setAlumno(alumno);
        inscripcion.setCurso(curso);
        inscripcion.setFecha(LocalDate.now());
        inscripcion.setEstado(EstadoInscripcion.ACTIVA);
        return aDTO(inscripcionRepository.save(inscripcion));
    }

    // RF23
    @Transactional(readOnly = true)
    public List<InscripcionDTO> porAlumno(Long alumnoId) {
        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("alumno", alumnoId));
        return inscripcionRepository.findByAlumno(alumno).stream().map(this::aDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<InscripcionDTO> porCurso(Long cursoId) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("curso", cursoId));
        return inscripcionRepository.findByCurso(curso).stream().map(this::aDTO).toList();
    }

    // RF24
    @Transactional
    public InscripcionDTO anular(Long id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("inscripción", id));
        if (inscripcion.getEstado() == EstadoInscripcion.ANULADA) {
            throw new ReglaNegocioException("La inscripción ya está anulada");
        }
        inscripcion.anular();   // al ser @Transactional, el cambio se guarda solo
        return aDTO(inscripcion);
    }

    private InscripcionDTO aDTO(Inscripcion i) {
        return new InscripcionDTO(
                i.getIdInscripcion(),
                i.getAlumno().getIdAlumno(),
                i.getCurso().getIdCurso(),
                i.getFecha(),
                i.getEstado());
    }
}
