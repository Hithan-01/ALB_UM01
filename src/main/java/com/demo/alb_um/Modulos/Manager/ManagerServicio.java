package com.demo.alb_um.Modulos.Manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.alb_um.DTOs.ManagerDTO;
import com.demo.alb_um.DTOs.RegistroAdminDTO;
import com.demo.alb_um.DTOs.RegistroAlumnoDTO;
import com.demo.alb_um.DTOs.RegistroCoachDTO;
import com.demo.alb_um.DTOs.RegistroServicioDTO;
import com.demo.alb_um.Modulos.Actividad_Fisica.ActividadFisicaRepositorio;
import com.demo.alb_um.Modulos.Actividad_Fisica.Entidad_ActividadFisica;
import com.demo.alb_um.Modulos.Admn.Ent_UsuarioAdmin;
import com.demo.alb_um.Modulos.Admn.UsuarioAdminRepositorio;
import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoRepositorio;
import com.demo.alb_um.Modulos.Alumno_Actividad.AlumnoActividadId;
import com.demo.alb_um.Modulos.Alumno_Actividad.AlumnoActividadRepositorio;
import com.demo.alb_um.Modulos.Alumno_Actividad.Ent_AlumnoActividad;
import com.demo.alb_um.Modulos.Carrera.Entidad_carrera;
import com.demo.alb_um.Modulos.Carrera.Repositorio_Carrera;
import com.demo.alb_um.Modulos.Servicio.Ent_Servicio;
import com.demo.alb_um.Modulos.Servicio.ServicioRepositorio;
import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;
import com.demo.alb_um.Modulos.Usuario.UsuarioRepositorio;

import jakarta.transaction.Transactional;

@Service
public class ManagerServicio {

    private final UsuarioRepositorio usuarioRepositorio;

    public ManagerServicio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public ManagerDTO obtenerInformacionManagerPorUserName(String userName) {
        Entidad_Usuario usuario = usuarioRepositorio.findByUserName(userName)
            .orElseThrow(() -> new IllegalArgumentException("Manager no encontrado"));

        ManagerDTO manager = new ManagerDTO();
        manager.setNombre(usuario.getNombre());
        manager.setApellido(usuario.getApellido());
        manager.setEmail(usuario.getEmail());
        manager.setFechaIngreso("2020-01-01"); // Este dato puede venir de otra tabla o fuente
        return manager;
    }


    @Autowired
    private UsuarioRepositorio usuarioRepository;

    @Autowired
    private UsuarioAlumnoRepositorio alumnoRepository;

    @Autowired
    private ServicioRepositorio servicioRepository;

    @Autowired
    private UsuarioAdminRepositorio adminRepository;

    @Autowired
    private Repositorio_Carrera carreraRepository;

    @Autowired
    private ActividadFisicaRepositorio actividadFisicaRepository;

    @Autowired
    private AlumnoActividadRepositorio alumnoActividadRepository;



@Transactional
public void registrarAlumno(RegistroAlumnoDTO dto) {
    // Crear y guardar el Usuario
    Entidad_Usuario usuario = new Entidad_Usuario();
    usuario.setUserName(dto.getUserName());
    usuario.setNombre(dto.getNombre());
    usuario.setApellido(dto.getApellido());
    usuario.setEmail(dto.getEmail());
    usuario.setContrasena(dto.getContrasena());
    usuario.setGenero(dto.getGenero());
    usuario.setFecha_nacimiento(dto.getFechaNacimiento());
    usuario.setTagCredencial(dto.getTagCredencial());
    usuario.setRol("ALUMNO"); // Fijar rol
    usuarioRepository.save(usuario);

    // Buscar la carrera
    Entidad_carrera carrera = carreraRepository.findById(dto.getIdCarrera())
            .orElseThrow(() -> new RuntimeException("Carrera no encontrada con ID: " + dto.getIdCarrera()));

    // Crear y guardar la información específica del Alumno
    Entidad_Usuario_Alumno alumno = new Entidad_Usuario_Alumno();
    alumno.setUsuario(usuario); // Relación con el Usuario
    alumno.setSemestre(dto.getSemestre());
    alumno.setCarrera(carrera); // Relación con Carrera
    alumno.setResidencia(dto.getResidencia());
    alumnoRepository.save(alumno);

    // Crear y guardar la relación con la actividad física (si se seleccionó una)
    if (dto.getIdActividadFisica() != null) {
        Entidad_ActividadFisica actividad = actividadFisicaRepository.findById(dto.getIdActividadFisica())
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada con ID: " + dto.getIdActividadFisica()));

        Ent_AlumnoActividad alumnoActividad = new Ent_AlumnoActividad();
        AlumnoActividadId id = new AlumnoActividadId();
        id.setIdUsuarioAlumno(alumno.getIdUsuarioAlumno());
        id.setIdActividadFisica(actividad.getIdActividadFisica());
        alumnoActividad.setId(id);
        alumnoActividad.setUsuarioAlumno(alumno);
        alumnoActividad.setActividadFisica(actividad);

        alumnoActividadRepository.save(alumnoActividad);
    }
}


    public void registrarAdmin(RegistroAdminDTO dto) {
        // 1. Crear y guardar el Usuario
        Entidad_Usuario usuario = new Entidad_Usuario();
        usuario.setUserName(dto.getUserName());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setContrasena(dto.getContrasena());
        usuario.setGenero(dto.getGenero());
        usuario.setFecha_nacimiento(dto.getFechaNacimiento());
        usuario.setRol("ADMIN"); // Asignamos el rol
        usuario.setTagCredencial(dto.gettagCredencial());
        usuarioRepository.save(usuario);

        // 2. Obtener el Servicio
        Ent_Servicio servicio = servicioRepository.findById(dto.getServicioId())
            .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        // 3. Crear y guardar el Administrador
        Ent_UsuarioAdmin admin = new Ent_UsuarioAdmin();
        admin.setUsuario(usuario); // Relación con el Usuario
        admin.setCargoServicio(dto.getCargoServicio());
        admin.setServicio(servicio); // Relación con el Servicio
        adminRepository.save(admin);
    }

     public void registrarServicio(RegistroServicioDTO dto) {
        Ent_Servicio servicio = new Ent_Servicio();
        servicio.setNombre(dto.getNombre()); // Asigna el nombre del servicio
        servicioRepository.save(servicio);   // Guarda en la base de datos
    }

    public List<Ent_Servicio> obtenerServicios() {
        return servicioRepository.findAll();
    }

    @Transactional
    public void registrarCoach(RegistroCoachDTO coachDTO) {
        Entidad_Usuario usuario = new Entidad_Usuario();
        usuario.setNombre(coachDTO.getNombre());
        usuario.setApellido(coachDTO.getApellido());
        usuario.setEmail(coachDTO.getEmail());
        usuario.setGenero(coachDTO.getGenero());
        usuario.setFecha_nacimiento(coachDTO.getFechaNacimiento());
        usuario.setUserName(coachDTO.getUserName());
        usuario.setContrasena(coachDTO.getContrasena());
        usuario.setTagCredencial(coachDTO.gettagCredencial());
        usuario.setRol("COACH"); // Asignar el rol de Coach
        usuarioRepository.save(usuario);
    }
}
