package com.gestionhorarios.luckydev;

import com.gestionhorarios.luckydev.model.Departamento;
import com.gestionhorarios.luckydev.model.Empleado;
import com.gestionhorarios.luckydev.model.Novedad;
import com.gestionhorarios.luckydev.model.enums.TipoNovedad;
import com.gestionhorarios.luckydev.repository.DepartamentoRepository;
import com.gestionhorarios.luckydev.repository.EmpleadoRepository;
import com.gestionhorarios.luckydev.repository.NovedadRepository;
import com.gestionhorarios.luckydev.service.GeneradorHorarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

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
            GeneradorHorarioService generadorService) { // <--- Inyectamos el Generador aquí
        return args -> {
            // 1. Crear Departamento y guardarlo en una variable 'dep'
            Departamento dep = new Departamento();
            dep.setNombre("Ventas Nocturnas");
            dep.setMinutosComidaDefault(60);
            dep.setPersonalMinimoRequerido(1); // Importante para la cobertura
            dep = depRepo.save(dep); // Guardamos y recuperamos con su ID real

            // 2. Crear Empleado 1: Brayan
            Empleado brayan = new Empleado();
            brayan.setNombre("Brayan Camargo");
            brayan.setDepartamento(dep);
            empRepo.save(brayan);

            // 3. Crear Empleado 2: Juan
            Empleado juan = new Empleado();
            juan.setNombre("Juan Perez");
            juan.setDepartamento(dep);
            empRepo.save(juan);

            System.out.println("¡Empleados creados con éxito!");

            // 4. AHORA SÍ: Llamamos al motor con el ID real del departamento creado
            // Esto evita el error de "división por cero" porque ya hay 2 empleados
            System.out.println("Generando barrido de calidad de vida para " + dep.getNombre() + "...");
            generadorService.generarBarridoMensual(dep.getId(), 4, 2026);

            System.out.println("¡Todo listo! Revisa tu base de datos.");
        };
    }
}