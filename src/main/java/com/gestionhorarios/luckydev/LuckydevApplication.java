package com.gestionhorarios.luckydev;

import com.gestionhorarios.luckydev.model.ConfiguracionTurno;
import com.gestionhorarios.luckydev.model.Departamento;
import com.gestionhorarios.luckydev.model.Empleado;
import com.gestionhorarios.luckydev.repository.ConfiguracionTurnoRepository;
import com.gestionhorarios.luckydev.repository.DepartamentoRepository;
import com.gestionhorarios.luckydev.repository.EmpleadoRepository;
import com.gestionhorarios.luckydev.repository.NovedadRepository;
import com.gestionhorarios.luckydev.service.GeneradorHorarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalTime;

@SpringBootApplication
public class LuckydevApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckydevApplication.class, args);
    }

    @Bean
    public CommandLineRunner setup(
            EmpleadoRepository empRepo,
            DepartamentoRepository depRepo,
            NovedadRepository novRepo,
            ConfiguracionTurnoRepository configTurnoRepo, // <--- PASO A: Inyectamos el nuevo Repo
            GeneradorHorarioService generadorService) {
        return args -> {
            // 1. Crear Departamento
            Departamento dep = new Departamento();
            dep.setNombre("Ventas Nocturnas");
            dep.setMinutosComidaDefault(60);
            dep.setPersonalMinimoRequerido(1);
            dep = depRepo.save(dep);

            // 2. PASO B: Configurar los turnos base para este departamento
            // Esto es lo que el sistema leerá para saber qué horarios asignar
            if (configTurnoRepo.count() == 0) {
                ConfiguracionTurno apertura = new ConfiguracionTurno();
                apertura.setNombrePersonalizado("Apertura");
                apertura.setHoraEntrada(LocalTime.of(8, 0));
                apertura.setHoraSalida(LocalTime.of(17, 0));
                apertura.setOrdenPrioridad(1);
                apertura.setDepartamento(dep);
                configTurnoRepo.save(apertura);

                ConfiguracionTurno cierre = new ConfiguracionTurno();
                cierre.setNombrePersonalizado("Cierre");
                cierre.setHoraEntrada(LocalTime.of(13, 0));
                cierre.setHoraSalida(LocalTime.of(22, 0));
                cierre.setOrdenPrioridad(2);
                cierre.setDepartamento(dep);
                configTurnoRepo.save(cierre);

                System.out.println("✅ Configuración de turnos (Apertura/Cierre) creada.");
            }

            // 3. Crear Empleados
            Empleado brayan = new Empleado();
            brayan.setNombre("Brayan Camargo");
            brayan.setDepartamento(dep);
            empRepo.save(brayan);

            Empleado juan = new Empleado();
            juan.setNombre("Juan Perez");
            juan.setDepartamento(dep);
            empRepo.save(juan);

            System.out.println("¡Empleados creados con éxito!");

            // 4. PASO C: Llamar al generador de MES COMPLETO
            // Ahora usamos 'generarMesCompleto' que ya incluye tus turnos elásticos
            System.out.println("Iniciando generación automática del mes...");
            generadorService.generarMesCompleto(dep.getId(), 4, 2026);

            System.out.println("¡Todo listo! Revisa tu base de datos para ver el mes de Abril.");
        };
    }
}