package com.gestionhorarios.luckydev.repository;

import com.gestionhorarios.luckydev.model.TurnoProgramado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TurnoRepository extends JpaRepository<TurnoProgramado, Long> {
    List<TurnoProgramado> findByEmpleadoId(Long empleadoId);
    //Verificar el tema de equidad del mes
    List<TurnoProgramado> findBySemanaDelAnio(Integer semana);
}
