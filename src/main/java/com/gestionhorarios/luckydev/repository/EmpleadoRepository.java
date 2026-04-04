package com.gestionhorarios.luckydev.repository;

import com.gestionhorarios.luckydev.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    long countByDepartamentoId(Long departamentoId);

    List<Empleado> findByDepartamentoId(Long departamentoId);
}