package com.demo.alb_um.Modulos.Admn;

import com.demo.alb_um.DTOs.AdminDTO;
import com.demo.alb_um.DTOs.CitaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioAdminServicio {

    @Autowired
    private UsuarioAdminRepositorio usuarioAdminRepositorio;



   public Optional<AdminDTO> obtenerInformacionAdminPorUserName(String userName) {
    Optional<Ent_UsuarioAdmin> adminOpt = usuarioAdminRepositorio.findByUsuario_UserName(userName);

    if (adminOpt.isPresent()) {
        Ent_UsuarioAdmin admin = adminOpt.get();
        String nombreCompleto = admin.getUsuario().getNombre() + " " + admin.getUsuario().getApellido();
        String cargoServicio = admin.getCargoServicio();

        // Convertir las citas a CitaDTO y manejar correctamente los horarios nulos
        List<CitaDTO> citas = admin.getCitas().stream()
                .map(cita -> {
                    // Verificar si HorarioServicio es nulo
                    LocalDate diaSemana = null;
                    LocalTime hora = null;
                    if (cita.getHorarioServicio() != null) {
                        diaSemana = cita.getHorarioServicio().getDiaSemana();
                        hora = cita.getHorarioServicio().getHora();
                    }

                    // Crear y devolver CitaDTO
                    return new CitaDTO(
                        cita.getUsuarioAdmin().getServicio().getNombre(), 
                        diaSemana,
                        hora,
                        cita.getEstadoAsistencia(),
                        cita.getVerificacion(),
                        cita.getAutorizadoPor()
                    );
                })
                .collect(Collectors.toList());

        return Optional.of(new AdminDTO(nombreCompleto, cargoServicio, citas));
    }

    return Optional.empty();
}


    
}
