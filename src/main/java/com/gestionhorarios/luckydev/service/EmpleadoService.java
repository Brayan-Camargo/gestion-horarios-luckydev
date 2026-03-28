package com.gestionhorarios.luckydev.service;

import com.gestionhorarios.luckydev.dto.EmpleadoDTO;
import com.gestionhorarios.luckydev.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public List<EmpleadoDTO> listarTodosLosEmpleados() {
        return empleadoRepository.findAll().stream()
                .map(e -> new EmpleadoDTO(
                        e.getNombre(),
                        e.getPuesto(),
                        e.getDepartamento() != null ? e.getDepartamento().getNombre() : "Sin Depto"
                ))
                .collect(Collectors.toList());
    }

    //Aquí debemos crear la logica para el boton princpal de GENERAR HORARIO
}
