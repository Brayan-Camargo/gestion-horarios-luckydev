package com.gestionhorarios.luckydev.controller;

import com.gestionhorarios.luckydev.dto.EmpleadoDTO;
import com.gestionhorarios.luckydev.service.EmpleadoService;
import com.gestionhorarios.luckydev.service.GeneradorHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping // Esto responderá a: localhost:8080/api/empleados
    public List<EmpleadoDTO> obtenerTodos() {
        return empleadoService.listarTodosLosEmpleados();
    }

    @Autowired
    private GeneradorHorarioService generadorService;

    @GetMapping("/disponibles/{deptoId}")
    public List<EmpleadoDTO> verDisponibles(@PathVariable Long deptoId) {
        // Simulamos la próxima semana: del lunes 30 de marzo al domingo 5 de abril
        LocalDate inicio = LocalDate.of(2026, 3, 30);
        LocalDate fin = LocalDate.of(2026, 4, 5);

        return generadorService.obtenerPersonalDisponible(deptoId, inicio, fin)
                .stream()
                .map(e -> new EmpleadoDTO(e.getNombre(), e.getPuesto(), e.getDepartamento().getNombre()))
                .collect(Collectors.toList());
    }
}

