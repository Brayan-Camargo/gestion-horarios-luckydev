package com.gestionhorarios.luckydev.service;

import com.gestionhorarios.luckydev.model.Empleado;
import com.gestionhorarios.luckydev.model.Novedad;
import com.gestionhorarios.luckydev.repository.EmpleadoRepository;
import com.gestionhorarios.luckydev.repository.NovedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeneradorHorarioService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private NovedadRepository novedadRepository;

    public List<Empleado> obtenerPersonalDisponible(Long departamentoId, LocalDate fechaDeInicio, LocalDate fechaFin) {
        List<Empleado> todos = empleadoRepository.findAll().stream()
                .filter(e -> e.getDepartamento().getId().equals(departamentoId))
                .collect(Collectors.toList());

        return todos.stream().filter(empleado -> {
            List<Novedad> novedades = novedadRepository.findByEmpleadoId(empleado.getId());

            boolean tieneConflicto = novedades.stream().anyMatch(n ->
                    (n.getFechaInicio().isBefore(fechaFin) || n.getFechaInicio().isEqual(fechaFin)) &&
                            (n.getFechaFin().isAfter(fechaDeInicio) || n.getFechaFin().isEqual(fechaDeInicio))
                    );
            return !tieneConflicto;
        }).collect(Collectors.toList());
    }
}
