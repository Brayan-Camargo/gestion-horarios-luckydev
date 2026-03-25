package com.gestionhorarios.luckydev.service;

import com.gestionhorarios.luckydev.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    //Aquí debemos crear la logica para el boton princpal de GENERAR HORARIO
}
