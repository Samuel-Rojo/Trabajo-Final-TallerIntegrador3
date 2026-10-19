package com.tallerintegrador.universidad.controller;

import com.tallerintegrador.universidad.dto.CalificacionDTO;
import com.tallerintegrador.universidad.dto.CalificacionRequest;
import com.tallerintegrador.universidad.dto.CalificacionUpdateRequest;
import com.tallerintegrador.universidad.service.CalificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {

    private final CalificacionService service;

    // RF38
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CalificacionDTO registrar(@Valid @RequestBody CalificacionRequest req) {
        return service.registrar(req);
    }

    // RF39
    @PutMapping("/{id}")
    public CalificacionDTO modificar(@PathVariable Long id,
                                     @Valid @RequestBody CalificacionUpdateRequest req) {
        return service.modificar(id, req);
    }

    // RF40
    @GetMapping("/alumno/{alumnoId}")
    public List<CalificacionDTO> porAlumno(@PathVariable Long alumnoId) {
        return service.porAlumno(alumnoId);
    }

    @GetMapping("/evaluacion/{evaluacionId}")
    public List<CalificacionDTO> porEvaluacion(@PathVariable Long evaluacionId) {
        return service.porEvaluacion(evaluacionId);
    }
}
