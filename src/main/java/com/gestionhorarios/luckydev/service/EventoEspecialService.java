package com.gestionhorarios.luckydev.service;

import com.gestionhorarios.luckydev.model.EventoEspecial;
import com.gestionhorarios.luckydev.repository.EventoEspecialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoEspecialService {

    @Autowired
    private EventoEspecialRepository eventoEspecialRepository;

    public List<EventoEspecial> listarEventos(){
        return eventoEspecialRepository.findAll();
    }

    public EventoEspecial guardarEvento(EventoEspecial evento){
        return eventoEspecialRepository.save(evento);
    }
}
