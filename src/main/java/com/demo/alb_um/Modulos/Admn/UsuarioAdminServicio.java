package com.demo.alb_um.Modulos.Admn;

import com.demo.alb_um.DTOs.AdminDTO;
import com.demo.alb_um.DTOs.CitaDTO;

import com.demo.alb_um.Modulos.Citas.Ent_Cita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

            List<CitaDTO> citas = admin.getCitas().stream()
                    .map(this::convertirACitaDTO)
                    .collect(Collectors.toList());

            return Optional.of(new AdminDTO(nombreCompleto, cargoServicio, citas));
        }

        return Optional.empty();
    }

    private CitaDTO convertirACitaDTO(Ent_Cita cita) {
        return new CitaDTO(
            cita.getHorarioServicio().getDiaSemana(),
            cita.getHorarioServicio().getHora(),
            cita.getEstadoAsistencia(),
            cita.getVerificacion(),
            cita.getAutorizadoPor()
        );
    }
}
