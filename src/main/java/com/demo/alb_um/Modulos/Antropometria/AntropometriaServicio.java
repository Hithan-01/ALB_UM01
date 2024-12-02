package com.demo.alb_um.Modulos.Antropometria;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.alb_um.Modulos.Citas.Ent_Cita;
import com.demo.alb_um.Modulos.Admn.Ent_UsuarioAdmin;
import com.demo.alb_um.Modulos.Citas.CitaRepositorio;

@Service
public class AntropometriaServicio {

    private final AntropometriaRepositorio antropometriaRepositorio;
    private final CitaRepositorio citaRepositorio;

    @Autowired
    public AntropometriaServicio(AntropometriaRepositorio antropometriaRepositorio, CitaRepositorio citaRepositorio) {
        this.antropometriaRepositorio = antropometriaRepositorio;
        this.citaRepositorio = citaRepositorio;
    }

    public void guardarDatosAntroYValidarCita(Long citaId, Ent_Antro datosAntro, Ent_UsuarioAdmin admin) {
    // Verificar si ya existen datos antropométricos para esta cita
    if (antropometriaRepositorio.existsByCitaId(citaId)) {
        throw new IllegalArgumentException("Ya existen datos antropométricos para esta cita.");
    }

    // Buscar la cita por ID
    Ent_Cita cita = citaRepositorio.findById(citaId)
        .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

    // Asociar la cita y el alumno a los datos antropométricos
    datosAntro.setCita(cita);
    datosAntro.setUsuarioAlumno(cita.getUsuarioAlumno());

    // Calcular IMC antes de guardar (peso / (talla ^ 2))
    if (datosAntro.getPeso() != null && datosAntro.getTalla() != null && datosAntro.getTalla() > 0) {
        double tallaEnMetros = datosAntro.getTalla() / 100; // Convertir a metros
        datosAntro.setImc(datosAntro.getPeso() / (tallaEnMetros * tallaEnMetros));
    } else {
        throw new IllegalArgumentException("Peso y talla son requeridos y deben ser válidos.");
    }

    // Guardar los datos antropométricos
    antropometriaRepositorio.save(datosAntro);

    // Validar la cita (marcar como asistió)
    cita.setEstadoAsistencia("ASISTIO");
    cita.setVerificacion(true);
    cita.setAutorizadoPor(admin.getUsuario().getNombre() + " " + admin.getUsuario().getApellido());
    citaRepositorio.save(cita);
}


public List<Ent_Antro> obtenerDatosPorAlumno(Long alumnoId) {
    return antropometriaRepositorio.findByUsuarioAlumno_IdUsuarioAlumnoOrderByCita_HorarioServicio_DiaSemanaDesc(alumnoId);
}

public Optional<Ent_Antro> obtenerUltimoDatoPorAlumno(Long alumnoId) {
    return antropometriaRepositorio.findByUsuarioAlumno_IdUsuarioAlumnoOrderByCita_HorarioServicio_DiaSemanaDesc(alumnoId)
                                   .stream()
                                   .findFirst();
}


public Optional<Ent_Antro> obtenerDatosAntropometricosPorCita(Long idUsuarioAlumno) {
    // Buscar citas confirmadas y asistidas de antropometría
    List<Ent_Cita> citasAntropometria = citaRepositorio.findCitasConfirmadasByUsuarioAndServicio(
        idUsuarioAlumno, "Antropometria", "ASISTIO");
        System.out.println("Citas encontradas: " + citasAntropometria.size());
    // Si hay citas, obtener los datos antropométricos de la primera
    if (!citasAntropometria.isEmpty()) {
        return antropometriaRepositorio.findByCitaId(citasAntropometria.get(0).getIdCita());
    }
    System.out.println("No se encontraron datos antropométricos.");
    return Optional.empty();
}


}
