package com.demo.alb_um.Modulos.Horario_servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.alb_um.DTOs.HorarioServicioDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class HorarioServicioServicio {

    @Autowired
    private HorarioServicioRepositorio horarioServicioRepositorio;

    // Mapa que asocia facultades con sus respectivas fechas
    private static final Map<String, List<LocalDate>> fechasPorFacultad = Map.of(
        "Artcom", List.of(
            LocalDate.parse("2024-10-07"),
            LocalDate.parse("2024-10-08"),
            LocalDate.parse("2024-10-09"),
            LocalDate.parse("2024-10-10"),
            LocalDate.parse("2024-10-11")
        ),
        "Fitec", List.of(
            LocalDate.parse("2024-10-14"),
            LocalDate.parse("2024-10-15"),
            LocalDate.parse("2024-10-16"),
            LocalDate.parse("2024-10-17"),
            LocalDate.parse("2024-10-18")
        ),
        "Fateo", List.of(
            LocalDate.parse("2024-10-21"),
            LocalDate.parse("2024-10-22"),
            LocalDate.parse("2024-10-23"),
            LocalDate.parse("2024-10-24"),
            LocalDate.parse("2024-10-25")
        ),
        "Faced", List.of(
            LocalDate.parse("2024-10-28"),
            LocalDate.parse("2024-10-29"),
            LocalDate.parse("2024-10-30"),
            LocalDate.parse("2024-10-31"),
            LocalDate.parse("2024-11-01")
        ),
        "Fapsi", List.of(
            LocalDate.parse("2024-11-04"),
            LocalDate.parse("2024-11-05"),
            LocalDate.parse("2024-11-06"),
            LocalDate.parse("2024-11-07"),
            LocalDate.parse("2024-11-08")
        ),
        "Facsa", List.of(
            LocalDate.parse("2024-11-11"),
            LocalDate.parse("2024-11-12"),
            LocalDate.parse("2024-11-13"),
            LocalDate.parse("2024-11-14"),
            LocalDate.parse("2024-11-15")
        ),
        "Facej", List.of(
            LocalDate.parse("2024-11-18"),
            LocalDate.parse("2024-11-19"),
            LocalDate.parse("2024-11-20"),
            LocalDate.parse("2024-11-21"),
            LocalDate.parse("2024-11-22")
        )
    );

    public List<HorarioServicioDTO> obtenerHorariosDisponiblesPorFacultad(String facultad) {
        // Obtener las fechas correspondientes a la facultad
        List<LocalDate> fechasFacultad = obtenerFechasPorFacultad(facultad);
        return horarioServicioRepositorio.findHorariosDisponiblesPorFacultad(fechasFacultad);
    }

    private List<LocalDate> obtenerFechasPorFacultad(String facultad) {
        // Obtener las fechas directamente del mapa, o devolver una lista vacía si no coincide
        return fechasPorFacultad.getOrDefault(facultad, List.of());
    }
}
