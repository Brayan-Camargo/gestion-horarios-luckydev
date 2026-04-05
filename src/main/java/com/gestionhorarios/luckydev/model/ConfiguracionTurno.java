package com.gestionhorarios.luckydev.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalTime;

@Entity
@Data
public class ConfiguracionTurno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombrePersonalizado; // "Apertura", "Intermedio", "Corte", etc.
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private Integer ordenPrioridad; // 1, 2, 3...

    @ManyToOne
    private Departamento departamento;
}