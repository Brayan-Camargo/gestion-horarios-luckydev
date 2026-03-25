package com.gestionhorarios.luckydev.repository;

import com.gestionhorarios.luckydev.model.EventoEspecial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoEspecialRepository extends JpaRepository<EventoEspecial, Long> {
}
