package com.gestionhorarios.luckydev.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class TurnoProgramado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    private LocalDateTime fechaHoraEntrada;
    private LocalDateTime fechaHoraSalida;

    private String tipoTurno; // Apertura, Cierre, Intermedio
    private boolean esDescanso; // Para Fin Libre Vie, Sab, Dom

    private boolean esPeticionAprobada;
    private boolean esCalidadDeVida;
    private boolean esModificacionManual; //Prioridad 5
    private String notaGerente;

    private Integer semanaDelAnio; //Revisa el tema de equidad de asignacion de turnos
}
