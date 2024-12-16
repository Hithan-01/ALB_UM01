package com.demo.alb_um.Modulos.Actividad_Fisica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

@Service
public class ActividadFisicaServicio {

    @Autowired
    private ActividadFisicaRepositorio actividadFisicaRepositorio;

    // Mapa de nombres a códigos (para identificador)
    private static final Map<String, String> CODIGO_NOMBRE = new HashMap<>();

    static {
        CODIGO_NOMBRE.put("Volleyball", "V1");
        CODIGO_NOMBRE.put("Basketball", "B2");
        CODIGO_NOMBRE.put("Futbol", "F3");
        
    }

    public Entidad_ActividadFisica crearActividadFisica(String nombre, String diaSemana, Time hora, String grupo, Integer coachId) {
        // Validaciones de servicio
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la actividad es obligatorio.");
        }
        if (grupo == null || grupo.trim().isEmpty()) {
            throw new IllegalArgumentException("El grupo no puede estar vacío.");
        }
        if (!CODIGO_NOMBRE.containsKey(nombre)) {
            throw new IllegalArgumentException("El nombre de la actividad no es válido.");
        }
    
        // Generar identificador
        String identificadorGrupo = generarIdentificadorGrupo(diaSemana, hora, nombre);
    
        // Crear entidad
        Entidad_ActividadFisica actividad = new Entidad_ActividadFisica();
        actividad.setNombre(nombre);
        actividad.setDiaSemana(diaSemana);
        actividad.setHora(hora);
        actividad.setGrupo(grupo);
        actividad.setCoachId(coachId);
        actividad.setIdentificadorGrupo(identificadorGrupo);
    
        return actividadFisicaRepositorio.save(actividad);
    }
    
    

    // Generar el identificador de grupo
    private String generarIdentificadorGrupo(String diaSemana, Time hora, String nombre) {
        // Determina la letra del día
        String letraDia = "Lunes-Miércoles".equalsIgnoreCase(diaSemana) ? "A" : "B";
        
        // Formatea la hora como dos dígitos (ej. 07, 08)
        String horaFormato = String.format("%02d", hora.toLocalTime().getHour());
        
        // Obtiene el código de la actividad
        String codigoActividad = CODIGO_NOMBRE.getOrDefault(nombre, "XX");
    
        // Genera el identificador concatenando los valores
        return letraDia + horaFormato + codigoActividad;
    }
    
}
