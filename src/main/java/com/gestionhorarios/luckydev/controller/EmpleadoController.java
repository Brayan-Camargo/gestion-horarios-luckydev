package com.gestionhorarios.luckydev.controller;

import com.gestionhorarios.luckydev.dto.EmpleadoDTO;
import com.gestionhorarios.luckydev.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping // Esto responderá a: localhost:8080/api/empleados
    public List<EmpleadoDTO> obtenerTodos() {
        return empleadoService.listarTodosLosEmpleados();
    }
}

