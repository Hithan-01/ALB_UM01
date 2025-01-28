package com.demo.alb_um.Modulos.Inscripcion_Taller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.demo.alb_um.DTOs.AlumnoTallerDTO;
import com.demo.alb_um.DTOs.TallerDTO;
import com.demo.alb_um.DTOs.TallerInscripcionDTO;
import com.demo.alb_um.Modulos.Taller.Ent_Taller;
import com.demo.alb_um.Modulos.Taller.Ent_Taller.EstadoTaller;
import com.demo.alb_um.Modulos.Taller.TallerRepositorio;
import com.demo.alb_um.Modulos.Usuario.Entidad_Usuario;

import jakarta.transaction.Transactional;

import com.demo.alb_um.Modulos.Alumno.Entidad_Usuario_Alumno;
import com.demo.alb_um.Modulos.Alumno.UsuarioAlumnoRepositorio;
import java.time.Duration;

@Service
public class InscripcionTallerServicio {

    @Autowired
    private InscripcionTallerRepositorio RepositorioInscripcionTaller;

    @Autowired
    private TallerRepositorio tallerRepository;

    @Autowired
    private UsuarioAlumnoRepositorio AlumnoRepositorio;



   // Método para verificar usando el username (String)
public boolean estaInscritoEnTaller(String username, Long idTaller) {
    return RepositorioInscripcionTaller.existsByUsuarioAlumno_Usuario_UserNameAndTaller_IdTaller(username, idTaller);
}

// Método para verificar usando el idAlumno (Long)
public boolean estaInscritoEnTaller(Long idAlumno, Long idTaller) {
    return RepositorioInscripcionTaller.existsByUsuarioAlumno_IdUsuarioAlumnoAndTaller_IdTaller(idAlumno, idTaller);
}
    

    public List<Long> obtenerTalleresInscritosIds(String userName) {
        List<Ent_InscripcionTaller> talleresInscritos = RepositorioInscripcionTaller.findByUsuarioAlumno_Usuario_UserName(userName);
        return talleresInscritos.stream()
                .map(taller -> taller.getTaller().getIdTaller())
                .collect(Collectors.toList());
    }
    
    

    public List<TallerDTO> listarTalleresDisponibles() {
        List<Ent_Taller> talleres = tallerRepository.findAll();  // Obtener todos los talleres
    
        return talleres.stream()
            .map(taller -> {
                TallerDTO dto = new TallerDTO(
                    taller.getIdTaller(),
                    taller.getNombre(),
                    taller.getDescripcion(),
                    taller.getFecha(),
                    taller.getHora(),
                    taller.getDuracion(),
                    taller.getCuposDisponibles(),
                    taller.getEstado(),
                    taller.getTiempoTranscurrido()
                );
                    
                // Si no hay cupos disponibles, marcamos el taller como lleno
                dto.setTallerLleno(taller.getCuposDisponibles() == 0);
                
                // Inicializamos estaInscrito como false por defecto
                dto.setEstaInscrito(false);
                
                return dto;
            })
            .collect(Collectors.toList());
    }

    

    // Método para inscribir un alumno a un taller (sin cambios)
    public String inscribirAlumno(Long idTaller, Long idAlumno) {
        Optional<Ent_Taller> tallerOptional = tallerRepository.findById(idTaller);
        Optional<Entidad_Usuario_Alumno> alumnoOptional = AlumnoRepositorio.findById(idAlumno);

        if (tallerOptional.isPresent() && alumnoOptional.isPresent()) {
            Ent_Taller taller = tallerOptional.get();
            Entidad_Usuario_Alumno alumno = alumnoOptional.get();

            // Verificar que haya cupos disponibles
            if (taller.getCuposDisponibles() > 0) {
                Ent_InscripcionTaller inscripcion = new Ent_InscripcionTaller();
                inscripcion.setTaller(taller);
                inscripcion.setUsuarioAlumno(alumno);
                inscripcion.setEstadoAsistencia("pendiente");
                inscripcion.setVerificacion(false);

                // Guardar la inscripción
                RepositorioInscripcionTaller.save(inscripcion);

                // Actualizar los cupos disponibles
                taller.setCuposDisponibles(taller.getCuposDisponibles() - 1);
                tallerRepository.save(taller);

                return "Inscripción exitosa";
            } else {
                return "Cupos agotados";
            }
        } else {
            return "Taller o alumno no encontrados";
        }
    }

    
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void actualizarEstadosTalleres() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDate hoy = ahora.toLocalDate();
    
        // Obtener talleres del día actual
        List<Ent_Taller> talleresDia = tallerRepository.findByFecha(hoy);
    
        for (Ent_Taller taller : talleresDia) {
            LocalTime horaTaller = taller.getHora();
            LocalDateTime horaInicioTaller = LocalDateTime.of(hoy, horaTaller);
            LocalDateTime inicioPaseLista = horaInicioTaller.minusMinutes(10);
            LocalDateTime horaFinPlanificada = horaInicioTaller.plusMinutes(taller.getDuracion());
    
            // Si el taller ya tiene una hora de finalización, usarla; de lo contrario, usar la planificada
            LocalDateTime horaFinTaller = (taller.getHoraFinalizacion() != null) 
                ? taller.getHoraFinalizacion() 
                : horaFinPlanificada;
    
            // El límite para registro de salidas se calcula con base en la hora de finalización real
            LocalDateTime limiteRegistroSalida = horaFinTaller.plusMinutes(15);
    
            // Cambiar de PROGRAMADO a REGISTRO_ABIERTO
            if (taller.getEstado() == EstadoTaller.PROGRAMADO &&
                ahora.isAfter(inicioPaseLista) &&
                ahora.isBefore(horaInicioTaller)) {
                taller.setEstado(EstadoTaller.REGISTRO_ABIERTO);
            }
    
            // Cambiar de REGISTRO_ABIERTO o PROGRAMADO a EN_CURSO
            if ((taller.getEstado() == EstadoTaller.REGISTRO_ABIERTO || taller.getEstado() == EstadoTaller.PROGRAMADO) &&
                ahora.isAfter(horaInicioTaller)) {
                taller.setEstado(EstadoTaller.EN_CURSO);
            }
    
            // Actualizar tiempo transcurrido mientras está EN_CURSO
            if (taller.getEstado() == EstadoTaller.EN_CURSO) {
                long minutosTranscurridos = Duration.between(horaInicioTaller, ahora).toMinutes();
                taller.setTiempoTranscurrido((int) minutosTranscurridos);
    
                // Cambiar de EN_CURSO a FINALIZADO si el tiempo ha terminado
                if (ahora.isAfter(horaFinPlanificada)) {
                    taller.setEstado(EstadoTaller.FINALIZADO);
    
                    // Registrar la hora de finalización y guardar inmediatamente
                    taller.setHoraFinalizacion(ahora); 
                    tallerRepository.saveAndFlush(taller); // Forzar la actualización inmediata
                }
            }
    
            // Cambiar de FINALIZADO a CERRADO si el límite de tiempo para registrar salidas ha expirado
            if (taller.getEstado() == EstadoTaller.FINALIZADO &&
                ahora.isAfter(limiteRegistroSalida)) {
                taller.setEstado(EstadoTaller.CERRADO);
            }
        }
    
        // Guardar todos los cambios pendientes
        tallerRepository.saveAll(talleresDia);
    }
    

    @Transactional
    public void finalizarTaller(Long idTaller) {
        // Buscar el taller por ID
        Ent_Taller taller = tallerRepository.findById(idTaller)
            .orElseThrow(() -> new RuntimeException("Taller no encontrado"));
            
        // Verificar que el estado sea EN_CURSO antes de finalizarlo
        if (taller.getEstado() == EstadoTaller.EN_CURSO) {
            taller.setEstado(EstadoTaller.FINALIZADO);
    
            // Registrar la hora de finalización con la hora actual
            LocalDateTime horaActual = LocalDateTime.now();
            taller.setHoraFinalizacion(horaActual);
    
            // Guardar cambios en la base de datos
            tallerRepository.save(taller);
    
            // Debugging
            System.out.println("Taller finalizado: " + taller.getNombre());
            System.out.println("Hora de finalización registrada: " + horaActual);
        } else {
            throw new RuntimeException("El taller no está en curso, no se puede finalizar.");
        }
    }
    
    private TallerDTO convertToDTO(Ent_Taller taller) {
        return new TallerDTO(
            taller.getIdTaller(),
            taller.getNombre(),
            taller.getDescripcion(),
            taller.getFecha(),
            taller.getHora(),
            taller.getDuracion(),
            taller.getCuposDisponibles(),
            taller.getEstado(),
            taller.getTiempoTranscurrido()
        );
    }

    public List<TallerDTO> obtenerTalleresDelDia() {
        LocalDate hoy = LocalDate.now();
        
        // Debug logs detallados
        System.out.println("\n===== Debug de fechas =====");
        System.out.println("Fecha actual (LocalDate): " + hoy);
        System.out.println("Fecha actual (SQL): " + java.sql.Date.valueOf(hoy));
        
        // Obtener todos los talleres con sus fechas para debug
        List<Object[]> todosLosTalleres = tallerRepository.findAllWithDates();
        System.out.println("\n===== Todos los talleres en BD =====");
        for (Object[] taller : todosLosTalleres) {
            System.out.println(String.format(
                "ID: %s, Nombre: %s, Fecha en BD: %s, Fecha formateada: %s, Fecha actual en BD: %s",
                taller[0], // id
                taller[1], // nombre
                taller[3], // fecha original
                taller[9], // fecha_formateada
                taller[10] // fecha_actual
            ));
        }
    
        // Buscar talleres de hoy
        List<Ent_Taller> talleres = tallerRepository.findByFecha(hoy);
        
        System.out.println("\n===== Talleres filtrados para hoy =====");
        System.out.println("Talleres encontrados: " + talleres.size());
        
        talleres.forEach(taller -> {
            System.out.println(String.format(
                "Taller: %s, Fecha: %s, Hora: %s, Estado: %s",
                taller.getNombre(),
                taller.getFecha(),
                taller.getHora(),
                taller.getEstado()
            ));
        });
    
        return talleres.stream()
            .map(taller -> {
                TallerDTO dto = convertToDTO(taller);
                LocalDateTime horaInicio = LocalDateTime.of(
                    taller.getFecha(),
                    taller.getHora()
                );
                LocalDateTime ahora = LocalDateTime.now();
                LocalDateTime inicioPaseList = horaInicio.minusMinutes(10);
                
                boolean puedeRegistrar = (ahora.isAfter(inicioPaseList) || ahora.isEqual(inicioPaseList)) 
                                       && taller.getEstado() != EstadoTaller.FINALIZADO;
                
                dto.setPuedeRegistrarLlegada(puedeRegistrar);
                return dto;
            })
            .collect(Collectors.toList());
    }
    
    public List<TallerInscripcionDTO> obtenerInscritosEnTaller(Long idTaller) {
        List<Ent_InscripcionTaller> inscripciones = 
            RepositorioInscripcionTaller.findByTaller_IdTaller(idTaller);
        
        return inscripciones.stream()
            .map(inscripcion -> new TallerInscripcionDTO(
                inscripcion.getTaller().getNombre(),
                inscripcion.getTaller().getDescripcion(),
                inscripcion.getTaller().getFecha(),
                inscripcion.getTaller().getHora(),
                inscripcion.getEstadoAsistencia()
            ))
            .collect(Collectors.toList());
    }


    public List<Ent_InscripcionTaller> obtenerInscripcionesPorTaller(Long idTaller) {
        return RepositorioInscripcionTaller.findByTaller_IdTaller(idTaller);
    }
    // Método para registrar la asistencia
    @Transactional
    public void registrarAsistenciaTaller(Long idTaller, List<Long> asistentes) {
        List<Ent_InscripcionTaller> inscripciones = 
            RepositorioInscripcionTaller.findByTaller_IdTaller(idTaller);
        
        for (Ent_InscripcionTaller inscripcion : inscripciones) {
            // Verificar si el alumno está en la lista de asistentes
            boolean asistio = asistentes.contains(inscripcion.getIdInscripcion());
            
            // Actualizar el estado de asistencia
            inscripcion.setEstadoAsistencia(asistio ? "Asistió" : "No Asistió");
            inscripcion.setVerificacion(true);
            
            // Guardar los cambios
            RepositorioInscripcionTaller.save(inscripcion);
        }
    }

    

    public Map<String, Object> registrarAsistenciaPorTag(Long tallerId, String tagCredencial) {
        Map<String, Object> response = new HashMap<>();
        LocalTime ahora = LocalTime.now();

        Optional<Ent_InscripcionTaller> inscripcionOpt = 
        RepositorioInscripcionTaller.findByTaller_IdTallerAndUsuarioAlumno_Usuario_TagCredencial(
                tallerId, tagCredencial);

        if (!inscripcionOpt.isPresent()) {
            response.put("error", true);
            response.put("message", "El alumno no está inscrito en este taller");
            return response;
        }

        Ent_InscripcionTaller inscripcion = inscripcionOpt.get();
        Ent_Taller taller = inscripcion.getTaller();

        if (inscripcion.getHoraLlegada() == null) {
            LocalTime horaInicio = taller.getHora(); // Convertir java.sql.Time a LocalTime
            if (ahora.isAfter(horaInicio.plusMinutes(10))) {
                inscripcion.setHoraLlegada(ahora);
                inscripcion.setEstadoAsistencia("RETARDO");
                response.put("message", "Registro de llegada tardía: " + ahora);
            } else {
                inscripcion.setHoraLlegada(ahora);
                inscripcion.setEstadoAsistencia("EN_CURSO");
                response.put("message", "Registro de llegada exitoso: " + ahora);
            }
        } else if (inscripcion.getHoraSalida() == null) {
            inscripcion.setHoraSalida(ahora);
            
            if ("EN_CURSO".equals(inscripcion.getEstadoAsistencia())) {
                inscripcion.setEstadoAsistencia("ASISTIO");
                inscripcion.setVerificacion(true);
                response.put("message", "Asistencia completa registrada");
            } else {
                inscripcion.setEstadoAsistencia("ASISTENCIA_INCOMPLETA");
                inscripcion.setVerificacion(true);
                response.put("message", "Asistencia incompleta por llegada tardía");
            }
        } else {
            response.put("error", true);
            response.put("message", "La asistencia ya fue registrada completamente");
            return response;
        }

        RepositorioInscripcionTaller.save(inscripcion);
        response.put("error", false);
        return response;
    }

    public Map<String, Object> registrarHoraLlegada(Long tallerId, String identificador) {
        Map<String, Object> response = new HashMap<>();
        LocalTime ahora = LocalTime.now();
    
        try {
            // Debug inicial
            System.out.println("=== Iniciando registro de llegada ===");
            System.out.println("Taller ID: " + tallerId);
            System.out.println("Identificador: " + identificador);
    
            // Obtener y mostrar todos los inscritos para debug
            List<Object[]> inscritos = RepositorioInscripcionTaller.findAllInscritosEnTaller(tallerId);
            System.out.println("Alumnos inscritos en el taller:");
            inscritos.forEach(inscrito -> 
                System.out.println("Username: " + inscrito[0] + ", Tag: " + inscrito[1]));
    
            // Buscar inscripción usando la nueva query combinada
            Optional<Ent_InscripcionTaller> inscripcionOpt = 
                RepositorioInscripcionTaller.findByTallerAndIdentificador(tallerId, identificador);
    
            if (!inscripcionOpt.isPresent()) {
                System.out.println("No se encontró inscripción para: " + identificador);
                response.put("error", true);
                response.put("message", "Alumno no inscrito en este taller");
                return response;
            }
    
            Ent_InscripcionTaller inscripcion = inscripcionOpt.get();
            Ent_Taller taller = inscripcion.getTaller();
            
            System.out.println("Inscripción encontrada para: " + 
                inscripcion.getUsuarioAlumno().getUsuario().getUserName());
    
            // Verificar si ya tiene hora de llegada
            if (inscripcion.getHoraLlegada() != null) {
                response.put("error", true);
                response.put("message", "Ya se registró una hora de llegada para este alumno");
                return response;
            }
    
            LocalTime horaTaller = taller.getHora();
            LocalDateTime horaInicioTaller = LocalDateTime.of(taller.getFecha(), horaTaller);
            LocalDateTime inicioPaseList = horaInicioTaller.minusMinutes(10);
            LocalDateTime finPaseList = horaInicioTaller.plusMinutes(10);
            LocalDateTime ahoraCompleto = LocalDateTime.now();
    
            System.out.println("Hora actual: " + ahoraCompleto);
            System.out.println("Hora inicio taller: " + horaInicioTaller);
            System.out.println("Inicio pase lista: " + inicioPaseList);
            System.out.println("Fin pase lista: " + finPaseList);
    
            // Verificar estado del taller
            if (taller.getEstado() != EstadoTaller.REGISTRO_ABIERTO && 
                taller.getEstado() != EstadoTaller.EN_CURSO && 
                taller.getEstado() != EstadoTaller.PROGRAMADO) {
                System.out.println("Estado inválido del taller: " + taller.getEstado());
                response.put("error", true);
                response.put("message", "El taller no está en un estado válido para registro de llegada");
                return response;
            }
    
            // Registrar llegada según el estado
            switch (taller.getEstado()) {
                case PROGRAMADO:
                    if (ahoraCompleto.isBefore(inicioPaseList)) {
                        response.put("error", true);
                        response.put("message", "Aún no es tiempo de registrar llegada");
                        return response;
                    }
                    inscripcion.setHoraLlegada(ahora);
                    inscripcion.setEstadoAsistencia("LLEGADA_VALIDA");
                    break;
    
                case REGISTRO_ABIERTO:
                case EN_CURSO:
                    inscripcion.setHoraLlegada(ahora);
                    inscripcion.setEstadoAsistencia(
                        ahoraCompleto.isBefore(finPaseList) ? "LLEGADA_VALIDA" : "LLEGADA_INVALIDA"
                    );
                    break;
    
                default:
                    response.put("error", true);
                    response.put("message", "Estado de taller no válido para registro");
                    return response;
            }
    
            // Guardar la inscripción
            try {
                inscripcion = RepositorioInscripcionTaller.save(inscripcion);
                System.out.println("Inscripción guardada con estado: " + inscripcion.getEstadoAsistencia());
                
                String mensaje = inscripcion.getEstadoAsistencia().equals("LLEGADA_VALIDA") 
                    ? "Llegada registrada correctamente a las " + ahora.format(DateTimeFormatter.ofPattern("HH:mm"))
                    : "Llegada registrada como tardía a las " + ahora.format(DateTimeFormatter.ofPattern("HH:mm"));
                
                response.put("error", false);
                response.put("message", mensaje);
                response.put("horaLlegada", ahora.format(DateTimeFormatter.ofPattern("HH:mm")));
                response.put("estadoAsistencia", inscripcion.getEstadoAsistencia());
                
            } catch (Exception e) {
                System.err.println("Error al guardar: " + e.getMessage());
                e.printStackTrace();
                response.put("error", true);
                response.put("message", "Error al guardar el registro: " + e.getMessage());
            }
    
        } catch (Exception e) {
            System.err.println("Error general: " + e.getMessage());
            e.printStackTrace();
            response.put("error", true);
            response.put("message", "Error al procesar el registro: " + e.getMessage());
        }
    
        return response;
    }

    public Map<String, Object> registrarHoraSalida(Long tallerId, String identificador) {
        Map<String, Object> response = new HashMap<>();
        LocalTime ahora = LocalTime.now();
    
        try {
            // Log inicial
            System.out.println("Intentando registrar salida para tallerId: " + tallerId + " con identificador: " + identificador);
    
            Optional<Ent_InscripcionTaller> inscripcionOpt = 
                RepositorioInscripcionTaller.findByTallerAndIdentificador(tallerId, identificador);
    
            if (!inscripcionOpt.isPresent()) {
                System.out.println("No se encontró inscripción para el identificador proporcionado.");
                response.put("error", true);
                response.put("message", "Alumno no inscrito en este taller.");
                return response;
            }
    
            Ent_InscripcionTaller inscripcion = inscripcionOpt.get();
    
            if (inscripcion.getHoraLlegada() == null) {
                System.out.println("Hora de llegada no registrada para el alumno.");
                response.put("error", true);
                response.put("message", "No se ha registrado una hora de llegada.");
                return response;
            }
    
            inscripcion.setHoraSalida(ahora);
    
            if ("LLEGADA_INVALIDA".equals(inscripcion.getEstadoAsistencia())) {
                inscripcion.setEstadoAsistencia("LLEGADA_INVALIDA");
                System.out.println("La hora de llegada fue inválida. Registrando salida.");
                response.put("warning", true);
                response.put("message", "La hora de llegada fue inválida, pero la hora de salida se ha registrado correctamente.");
            } else {
                inscripcion.setEstadoAsistencia("ASISTIO");
                inscripcion.setVerificacion(true);
                System.out.println("Salida registrada correctamente.");
                response.put("message", "Asistencia registrada correctamente.");
            }
    
            RepositorioInscripcionTaller.save(inscripcion);
            response.put("error", false);
            response.put("horaSalida", ahora.toString());
    
        } catch (Exception e) {
            System.err.println("Error al registrar la salida: " + e.getMessage());
            e.printStackTrace();
            response.put("error", true);
            response.put("message", "Error al registrar la salida: " + e.getMessage());
        }
    
        return response;
    }
    
    public void registrarTaller(Ent_Taller taller) {
        // Validaciones
        if (taller.getNombre() == null || taller.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del taller es obligatorio.");
        }
        if (taller.getFecha() == null || taller.getHora() == null) {
            throw new IllegalArgumentException("La fecha y la hora son obligatorias.");
        }
        if (taller.getCupos() == null || taller.getCupos() <= 0) {
            throw new IllegalArgumentException("Los cupos deben ser mayores a 0.");
        }
        if (taller.getDuracion() == null || taller.getDuracion() <= 0) {
            throw new IllegalArgumentException("La duración del taller debe ser mayor a 0 minutos.");
        }
    
        // Validar que la fecha y hora sean futuras
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        LocalDateTime fechaHoraTaller = LocalDateTime.of(taller.getFecha(), taller.getHora());
        if (fechaHoraTaller.isBefore(fechaHoraActual)) {
            throw new IllegalArgumentException("La fecha y hora del taller deben ser futuras.");
        }
    
        // Configurar valores iniciales
        taller.setCuposDisponibles(taller.getCupos()); // Los cupos disponibles deben ser iguales a los cupos totales al inicio
        taller.setEstado(Ent_Taller.EstadoTaller.PROGRAMADO); // Estado inicial: PROGRAMADO
        if (taller.getDescripcion() == null || taller.getDescripcion().trim().isEmpty()) {
            taller.setDescripcion("Descripción no proporcionada."); // Descripción por defecto si no se especifica
        }
    
        // Guardar taller en el repositorio
        tallerRepository.save(taller);
    }
    

    public Optional<Ent_Taller> obtenerTallerPorId(Long id) {
        return tallerRepository.findById(id);
    }
    
    public void actualizarTaller(Long id, Ent_Taller tallerActualizado) {
        // Buscar el taller existente
        Ent_Taller taller = tallerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Taller no encontrado."));
    
        // Validar nombre
        if (tallerActualizado.getNombre() == null || tallerActualizado.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del taller no puede estar vacío.");
        }
    
        // Validar fecha y hora
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        LocalDateTime fechaHoraTaller = LocalDateTime.of(tallerActualizado.getFecha(), tallerActualizado.getHora());
        if (fechaHoraTaller.isBefore(fechaHoraActual)) {
            throw new IllegalArgumentException("La fecha y hora del taller deben ser futuras.");
        }
    
        // Validar cupos
        int cuposOcupados = taller.getCupos() - taller.getCuposDisponibles();
        if (tallerActualizado.getCupos() < cuposOcupados) {
            throw new IllegalArgumentException("Los cupos no pueden ser menores que los cupos ocupados actuales (" + cuposOcupados + ").");
        }
    
        // Validar duración
        if (tallerActualizado.getDuracion() <= 0) {
            throw new IllegalArgumentException("La duración del taller debe ser mayor que 0.");
        }
    
        // Actualizar los datos del taller
        taller.setNombre(tallerActualizado.getNombre());
        taller.setDescripcion(tallerActualizado.getDescripcion());
        taller.setFecha(tallerActualizado.getFecha());
        taller.setHora(tallerActualizado.getHora());
        taller.setDuracion(tallerActualizado.getDuracion());
        taller.setCupos(tallerActualizado.getCupos());
    
        // Calcular cupos disponibles
        int nuevosCuposDisponibles = Math.max(0, tallerActualizado.getCupos() - cuposOcupados);
        taller.setCuposDisponibles(nuevosCuposDisponibles);
    
        // Guardar los cambios
        tallerRepository.save(taller);
    }
    
    
    public void eliminarTaller(Long id) {
        tallerRepository.deleteById(id);
    }
    

    public List<Ent_Taller> obtenerTodosTalleres() {
        return tallerRepository.findAll();
    }
    
    public Map<String, List<AlumnoTallerDTO>> getAlumnosPorEstado(Long idTaller, Ent_Taller.EstadoTaller estado) {
        Map<String, List<AlumnoTallerDTO>> resultado = new HashMap<>();
    
        // Obtener todas las inscripciones con detalles
        List<Ent_InscripcionTaller> inscripciones = RepositorioInscripcionTaller.findAllWithAlumnoDetailsByTallerId(idTaller);
    
        // Mapear las inscripciones a DTOs
        List<AlumnoTallerDTO> alumnosInscritos = inscripciones.stream()
                .map(this::mapToAlumnoTallerDTO)
                .collect(Collectors.toList());
        resultado.put("inscritos", alumnosInscritos);
    
        // Estado REGISTRO_ABIERTO: Mostrar alumnos que tienen hora de llegada
        if (estado == Ent_Taller.EstadoTaller.REGISTRO_ABIERTO) {
            List<AlumnoTallerDTO> alumnosLlegaron = inscripciones.stream()
                    .filter(i -> i.getHoraLlegada() != null)
                    .map(this::mapToAlumnoTallerDTO)
                    .collect(Collectors.toList());
            resultado.put("llegaron", alumnosLlegaron);
        }
    
        // Estado EN_CURSO: Alumnos que llegaron y los que no llegaron
        if (estado == Ent_Taller.EstadoTaller.EN_CURSO) {
            // Filtrar los alumnos que llegaron (tienen hora de llegada)
            List<AlumnoTallerDTO> alumnosLlegaron = inscripciones.stream()
                    .filter(i -> i.getHoraLlegada() != null)
                    .map(this::mapToAlumnoTallerDTO)
                    .collect(Collectors.toList());
            resultado.put("llegaron", alumnosLlegaron);
    
            // Filtrar los alumnos que no llegaron (sin hora de llegada)
            List<AlumnoTallerDTO> alumnosNoLlegaron = inscripciones.stream()
                    .filter(i -> i.getHoraLlegada() == null)
                    .map(this::mapToAlumnoTallerDTO)
                    .collect(Collectors.toList());
            resultado.put("noLlegaron", alumnosNoLlegaron);
        }
    
        // Estado CERRADO o FINALIZADO: Alumnos con asistencia confirmada
        if (estado == Ent_Taller.EstadoTaller.CERRADO || estado == Ent_Taller.EstadoTaller.FINALIZADO) {
            List<AlumnoTallerDTO> alumnosConAsistenciaConfirmada = inscripciones.stream()
                    .filter(i -> "ASISTIO".equalsIgnoreCase(i.getEstadoAsistencia()))
                    .map(this::mapToAlumnoTallerDTO)
                    .collect(Collectors.toList());
            resultado.put("asistieron", alumnosConAsistenciaConfirmada);
        }
    
        return resultado;
    }
    
    
    
 
// Método auxiliar para mapear la inscripción al DTO
private AlumnoTallerDTO mapToAlumnoTallerDTO(Ent_InscripcionTaller inscripcion) {
    Entidad_Usuario_Alumno alumno = inscripcion.getUsuarioAlumno();
    Entidad_Usuario usuario = alumno.getUsuario();

    Long idInscripcion = inscripcion.getIdInscripcion();
    String nombreCompleto = usuario.getNombre() + " " + usuario.getApellido();
    String userName = usuario.getUserName();
    String carrera = (alumno.getCarrera() != null) ? alumno.getCarrera().getNombre() : "Sin Carrera";
    String facultad = (alumno.getCarrera() != null && alumno.getCarrera().getFacultad() != null)
            ? alumno.getCarrera().getFacultad().getNombre()
            : "Sin Facultad";

    // Mapear las nuevas propiedades
    LocalTime horaLlegada = inscripcion.getHoraLlegada();
    LocalTime horaSalida = inscripcion.getHoraSalida();
    String estadoAsistencia = inscripcion.getEstadoAsistencia();

    AlumnoTallerDTO dto = new AlumnoTallerDTO();
    dto.setIdInscripcion(idInscripcion);
    dto.setNombreCompleto(nombreCompleto);
    dto.setUserName(userName);
    dto.setCarrera(carrera);
    dto.setFacultad(facultad);
    dto.setHoraLlegada(horaLlegada);
    dto.setHoraSalida(horaSalida);
    dto.setEstadoAsistencia(estadoAsistencia);

    return dto;
}

    

    // Servicio para obtener la lista de alumnos con detalles
public List<AlumnoTallerDTO> getAlumnosConDetalles(Long idTaller) {
    return RepositorioInscripcionTaller.findAllWithAlumnoDetailsByTallerId(idTaller)
            .stream()
            .map(this::mapToAlumnoTallerDTO) // Usar el método auxiliar para mapear
            .collect(Collectors.toList());
}

    
}
