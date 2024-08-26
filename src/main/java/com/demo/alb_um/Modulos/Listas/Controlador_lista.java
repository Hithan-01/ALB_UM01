package com.demo.alb_um.Modulos.Listas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/listas")
public class Controlador_lista {

    @Autowired
    private Servicio_lista listaServicio;

    @GetMapping
    public List<Entidad_Lista> obtenerTodasLasListas() {
        return listaServicio.obtenerTodasLasListas();
    }
}
