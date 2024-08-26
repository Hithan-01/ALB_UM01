package com.demo.alb_um.Modulos.Listas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Servicio_lista {

    @Autowired
    private Repositorio_lista listaRepositorio;

    public List<Entidad_Lista> obtenerTodasLasListas() {
        return listaRepositorio.findAll();
    }
}
