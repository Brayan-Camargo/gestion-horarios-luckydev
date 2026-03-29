package com.gestionhorarios.luckydev.repository;

import com.gestionhorarios.luckydev.model.Novedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NovedadRepository extends JpaRepository<Novedad, Long> {
    List<Novedad> findByEmpleadoId(Long empleadoId);
}
