package com.gestionhorarios.luckydev.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Desactiva protección contra ataques CSRF (solo para desarrollo)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // ESTO permite que entres a cualquier URL sin contraseña
                )
                .build();
    }
}
