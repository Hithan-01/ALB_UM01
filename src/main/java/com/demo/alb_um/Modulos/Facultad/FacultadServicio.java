package com.demo.alb_um.Modulos.Facultad;

import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
@Service
public class FacultadServicio {

    @Autowired
    private Repositorio_Facultad facultadRepositorio;

    public List<Entidad_facultad> obtenerTodas() {
        return facultadRepositorio.findAll();
    }
}