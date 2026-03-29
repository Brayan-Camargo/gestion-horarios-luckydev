package com.gestionhorarios.luckydev.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

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
    private Integer diasDeuda = 0;
    private Integer minutosDeuda = 0;

    private Integer conteoAperturasMes = 0;
    private Integer conteoCierresMes = 0;
    private LocalDate ultimoFinDeSemanaCalidad;

    private LocalDate fechaUltimoFinDeSemanaLargo;
    private Integer peticionesAceptadasMes = 0; // Se deben limitar a un maximo de 2 al mes
}
