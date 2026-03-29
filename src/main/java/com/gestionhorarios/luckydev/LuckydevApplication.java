package com.gestionhorarios.luckydev;

import com.gestionhorarios.luckydev.model.Departamento;
import com.gestionhorarios.luckydev.model.Empleado;
import com.gestionhorarios.luckydev.model.Novedad;
import com.gestionhorarios.luckydev.model.enums.TipoNovedad;
import com.gestionhorarios.luckydev.repository.DepartamentoRepository;
import com.gestionhorarios.luckydev.repository.EmpleadoRepository;
import com.gestionhorarios.luckydev.repository.NovedadRepository;
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
            NovedadRepository novRepo) { // <--- Agregamos el repo de novedades aquí
        return args -> {
            // 1. Crear Departamento
            Departamento dep = new Departamento();
            dep.setNombre("Ventas Nocturnas");
            dep.setMinutosComidaDefault(60);
            dep.setDiasDescansoSemanales(2);
            depRepo.save(dep);

            // 2. Crear Empleado 1: Brayan (Disponible)
            Empleado brayan = new Empleado();
            brayan.setNombre("Brayan Camargo");
            brayan.setPuesto("Líder de Piso");
            brayan.setDepartamento(dep);
            empRepo.save(brayan);

            // 3. Crear Empleado 2: Juan (Estará de vacaciones)
            Empleado juan = new Empleado();
            juan.setNombre("Juan Perez");
            juan.setPuesto("Auxiliar");
            juan.setDepartamento(dep);
            empRepo.save(juan);

            // 4. Registrar Vacaciones para Juan
            // Esta fecha debe coincidir con la que pusimos en el Controller (30 marzo - 5 abril)
            Novedad vacacion = new Novedad();
            vacacion.setEmpleado(juan);
            vacacion.setTipo(TipoNovedad.VACACIONES);
            vacacion.setFechaInicio(LocalDate.of(2026, 3, 28)); // Inicia antes de la semana
            vacacion.setFechaFin(LocalDate.of(2026, 4, 2));    // Termina en medio de la semana
            vacacion.setMinutosAfectados(0); // Para esta prueba no importa el banco de tiempo
            novRepo.save(vacacion);

            System.out.println("¡Escenario de prueba con vacaciones cargado correctamente!");
        };
    }
}