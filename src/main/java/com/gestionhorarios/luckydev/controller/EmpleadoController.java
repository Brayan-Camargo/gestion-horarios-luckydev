package com.gestionhorarios.luckydev.controller;

import com.gestionhorarios.luckydev.dto.EmpleadoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @GetMapping("/saludo")
    public String saludarGerente() {
        return "Hola! El sistema de gestión de horarios está en linea.";
    }

    @GetMapping("/prueba-dto")
    public EmpleadoDTO probarDto() {
        // Por ahora creamos uno manual para ver cómo se ve en el navegador
        return new EmpleadoDTO("Brayan Camargo", "Gerente LuckyWeb", "Administración");
    }
}
