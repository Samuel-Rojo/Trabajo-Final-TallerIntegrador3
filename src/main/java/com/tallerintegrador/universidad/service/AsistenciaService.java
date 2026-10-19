package com.tallerintegrador.universidad.service;

import com.tallerintegrador.universidad.dto.AsistenciaCursoRequest;
import com.tallerintegrador.universidad.dto.AsistenciaDTO;
import com.tallerintegrador.universidad.dto.AsistenciaItem;
import com.tallerintegrador.universidad.dto.ResumenAsistencia;
import com.tallerintegrador.universidad.exception.RecursoNoEncontradoException;
import com.tallerintegrador.universidad.exception.ReglaNegocioException;
import com.tallerintegrador.universidad.model.*;
import com.tallerintegrador.universidad.model.Asistencia.EstadoAsistencia;
import com.tallerintegrador.universidad.model.Inscripcion.EstadoInscripcion;
import com.tallerintegrador.universidad.repository.*;
import com.tallerintegrador.universidad.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final InscripcionRepository inscripcionRepository;
    private final AlumnoRepository alumnoRepository;
    private final CursoRepository cursoRepository;// de tus compañeros
    private final CursoMateriaRepository cursoMateriaRepository; // de tus compañeros (nombre a confirmar)

    // RF41: registra la asistencia de todo un curso en una fecha (todo o nada)
    @Transactional
    public List<AsistenciaDTO> registrar(AsistenciaCursoRequest req) {
        Curso curso = cursoRepository.findById(req.cursoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("curso", req.cursoId()));

        // RN13: el docente debe tener asignado el curso
        if (!cursoMateriaRepository.existsByCursoAndDocenteIdDocente(curso, req.docenteId())) {
            throw new ReglaNegocioException("El docente no tiene asignado este curso");
        }

        List<AsistenciaDTO> registradas = new ArrayList<>();
        for (AsistenciaItem item : req.asistencias()) {
            Inscripcion inscripcion = inscripcionRepository.findById(item.inscripcionId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("inscripción", item.inscripcionId()));

            if (inscripcion.getEstado() != EstadoInscripcion.ACTIVA) {
                throw new ReglaNegocioException("La inscripción " + item.inscripcionId() + " no está activa");
            }
            if (!inscripcion.getCurso().getIdCurso().equals(curso.getIdCurso())) {
                throw new ReglaNegocioException("La inscripción " + item.inscripcionId() + " no pertenece a ese curso");
            }
            // RN16: una única asistencia por alumno, curso y fecha
            if (asistenciaRepository.existsByInscripcionAndFecha(inscripcion, req.fecha())) {
                throw new ReglaNegocioException("Ya hay asistencia registrada para la inscripción "
                        + item.inscripcionId() + " en esa fecha");
            }

            Asistencia asistencia = new Asistencia();
            asistencia.setInscripcion(inscripcion);
            asistencia.setFecha(req.fecha());
            asistencia.setEstado(item.estado());
            asistencia.setObservacion(item.observacion());
            registradas.add(aDTO(asistenciaRepository.save(asistencia)));
        }
        return registradas;
    }

    // RF42
    @Transactional(readOnly = true)
    public List<AsistenciaDTO> historial(Long alumnoId) {
        Alumno alumno = buscarAlumno(alumnoId);
        return asistenciaRepository.findByInscripcionAlumnoOrderByFechaDesc(alumno)
                .stream().map(this::aDTO).toList();
    }

    // RF43: solo PRESENTE cuenta como asistencia (agregar TARDANZA/JUSTIFICADO si la consigna lo pide)
    @Transactional(readOnly = true)
    public ResumenAsistencia resumen(Long alumnoId) {
        Alumno alumno = buscarAlumno(alumnoId);

        long total = asistenciaRepository.countByInscripcionAlumno(alumno);
        long presentes = asistenciaRepository.countByInscripcionAlumnoAndEstado(alumno, EstadoAsistencia.PRESENTE);
        long ausentes = asistenciaRepository.countByInscripcionAlumnoAndEstado(alumno, EstadoAsistencia.AUSENTE);
        double porcentaje = total == 0 ? 0 : (presentes * 100.0) / total;

        return new ResumenAsistencia(total, presentes, ausentes, porcentaje);
    }

    private Alumno buscarAlumno(Long alumnoId) {
        return alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("alumno", alumnoId));
    }

    private AsistenciaDTO aDTO(Asistencia a) {
        return new AsistenciaDTO(
                a.getIdAsistencia(),
                a.getInscripcion().getIdInscripcion(),
                a.getInscripcion().getAlumno().getIdAlumno(),
                a.getFecha(),
                a.getEstado(),
                a.getObservacion());
    }
}
