package com.gestionhorarios.luckydev.service;

import com.gestionhorarios.luckydev.model.Empleado;
import com.gestionhorarios.luckydev.model.Novedad;
import com.gestionhorarios.luckydev.model.enums.TipoNovedad;
import com.gestionhorarios.luckydev.repository.EmpleadoRepository;
import com.gestionhorarios.luckydev.repository.NovedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class NovedadService {

    @Autowired
    private NovedadRepository novedadRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Transactional
    public Novedad registrarNovedad(Novedad novedad) {
        // Valida si es petición de descanso
        if (novedad.getTipo() == TipoNovedad.PETICION_DESCANSO && !esVentanaDePeticionesAbierta()) {
            throw new RuntimeException("Lo sentimos, la ventana de peticiones está cerrada. Abre los últimos 3 días del mes.");
        }

        // Guarda la novedad
        Novedad guardada = novedadRepository.save(novedad);
        Empleado emp = novedad.getEmpleado();

        // Logica de banco de tiempo en minutos
        if (novedad.getTipo() == TipoNovedad.FUERZA_MAYOR) {
            emp.setMinutosDeuda(emp.getMinutosDeuda() + novedad.getMinutosAfectados());
        }

        if (novedad.getTipo() == TipoNovedad.PERMISO_ESPECIAL) {
            emp.setMinutosDeuda(emp.getMinutosDeuda() - novedad.getMinutosAfectados());
        }

        // Actualiza al empleado
        empleadoRepository.save(emp);

        return guardada;
    }

    public boolean esVentanaDePeticionesAbierta() {
        LocalDate hoy = LocalDate.now();
        // Obtenemos el primer día del PRÓXIMO mes
        LocalDate primerDiaProximoMes = hoy.plusMonths(1).withDayOfMonth(1);

        // La ventana sera de 3 días antes del inicio del mes
        LocalDate inicioVentana = primerDiaProximoMes.minusDays(3);
        LocalDate finVentana = primerDiaProximoMes.minusDays(1);

        // Si hoy está entre el inicio y el fin de la ventana, regresamos true
        return (hoy.isEqual(inicioVentana) || hoy.isAfter(inicioVentana))
                && (hoy.isEqual(finVentana) || hoy.isBefore(finVentana));
    }
}
