package com.gestionhorarios.luckydev;

import com.gestionhorarios.luckydev.model.Departamento;
import com.gestionhorarios.luckydev.model.Empleado;
import com.gestionhorarios.luckydev.repository.DepartamentoRepository;
import com.gestionhorarios.luckydev.repository.EmpleadoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LuckydevApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckydevApplication.class, args);
    }

    @Bean
    public CommandLineRunner setup(EmpleadoRepository empRepo, DepartamentoRepository depRepo) {
        return args -> {
            // 1. Crear Departamento (Necesario para el horario)
            Departamento dep = new Departamento();
            dep.setNombre("Ventas Nocturnas");
            dep.setMinutosComidaDefault(60);
            depRepo.save(dep);

            // 2. Crear Empleado (Sin datos irrelevantes)
            Empleado emp = new Empleado();
            emp.setNombre("Brayan Camargo");
            emp.setPuesto("Líder de Piso");
            emp.setDepartamento(dep); // Esto sí es clave para saber su horario

            empRepo.save(emp);

            System.out.println("¡Datos de prueba para horarios guardados correctamente!");
        };
    }
}