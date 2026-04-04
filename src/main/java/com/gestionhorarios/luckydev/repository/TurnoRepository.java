package com.gestionhorarios.luckydev.repository;

import com.gestionhorarios.luckydev.model.TurnoProgramado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TurnoRepository extends JpaRepository<TurnoProgramado, Long> {
    List<TurnoProgramado> findByEmpleadoId(Long empleadoId);
    //Verificar el tema de equidad del mes
    List<TurnoProgramado> findBySemanaDelAnio(Integer semana);

    @Query("SELECT COUNT(t) FROM TurnoProgramado t WHERE t.esDescanso = true AND t.fechaHoraEntrada = :fecha AND t.empleado.departamento.id = :deptoId")
    long countDescansosPorFechaYDepto(@Param("fecha") LocalDateTime fecha, @Param("deptoId") Long deptoId);
}

