package com.gestionhorarios.luckydev;

import com.gestionhorarios.luckydev.model.Departamento;
import com.gestionhorarios.luckydev.repository.DepartamentoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LuckydevApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckydevApplication.class, args);
    }
}