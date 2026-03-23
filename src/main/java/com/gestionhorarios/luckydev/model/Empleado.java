package com.gestionhorarios.luckydev.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = true)
    private Departamento departamento;

    private String nombre;
    private String puesto;
    private Integer horasContratoSemanal;
    private Double saldoHoras;
    private String observaciones;
}
