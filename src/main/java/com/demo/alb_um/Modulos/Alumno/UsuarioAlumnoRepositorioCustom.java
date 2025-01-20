package com.demo.alb_um.Modulos.Alumno;
import com.demo.alb_um.DTOs.AlumnoBusquedaDTO;
import com.demo.alb_um.DTOs.BusquedaFaltas;

import java.util.Date;
import java.util.List;

public interface UsuarioAlumnoRepositorioCustom {
    List<AlumnoBusquedaDTO> busquedaAvanzada(Long idFacultad, Long idCarrera, Integer numeroFaltas, 
    String semestre, String filtroFaltas);

    List<BusquedaFaltas> buscarFaltasPorFiltros(Date fecha, Long idFacultad, Long idCarrera, String semestre);
}

