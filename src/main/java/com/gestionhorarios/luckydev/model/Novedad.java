package com.gestionhorarios.luckydev.model;

import com.gestionhorarios.luckydev.model.enums.TipoNovedad;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Novedad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    @Enumerated(EnumType.STRING)
    private TipoNovedad tipo;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private String observacion;
    private Integer minutosAfectados;
}
