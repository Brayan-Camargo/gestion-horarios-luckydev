package com.gestionhorarios.luckydev.service;

import com.gestionhorarios.luckydev.model.Empleado;
import com.gestionhorarios.luckydev.model.Novedad;
import com.gestionhorarios.luckydev.model.enums.TipoNovedad;
import com.gestionhorarios.luckydev.repository.EmpleadoRepository;
import com.gestionhorarios.luckydev.repository.NovedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NovedadService {

    @Autowired
    private NovedadRepository novedadRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Transactional
    public Novedad registrarNovedad(Novedad novedad) {
        Novedad guardada = novedadRepository.save(novedad);
        Empleado emp = novedad.getEmpleado();

        if (novedad.getTipo() == TipoNovedad.FUERZA_MAYOR) {
            emp.setMinutosDeuda(emp.getMinutosDeuda() + novedad.getMinutosAfectados());
        }

        if (novedad.getTipo() == TipoNovedad.PERMISO_ESPECIAL) {
            emp.setMinutosDeuda(emp.getMinutosDeuda() - novedad.getMinutosAfectados());
        }

        empleadoRepository.save(emp);
        return guardada;
    }
}
