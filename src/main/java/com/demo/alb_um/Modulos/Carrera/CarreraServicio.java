package com.demo.alb_um.Modulos.Carrera;

import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
@Service
public class CarreraServicio {

    @Autowired
    private Repositorio_Carrera carreraRepositorio;

    public List<Entidad_carrera> obtenerTodas() {
        return carreraRepositorio.findAll();
    }
}
