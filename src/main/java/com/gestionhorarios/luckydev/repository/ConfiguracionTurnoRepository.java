package com.gestionhorarios.luckydev.repository;

import com.gestionhorarios.luckydev.model.ConfiguracionTurno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConfiguracionTurnoRepository extends JpaRepository<ConfiguracionTurno, Long> {
    // Esto dara los turnos en el orden que el gerente quiere
    List<ConfiguracionTurno> findByDepartamentoIdOrderByOrdenPrioridadAsc(Long departamentoId);
}