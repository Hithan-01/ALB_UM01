package com.demo.alb_um.Config;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
public class CacheCleaner {

    @CacheEvict(value = "nombreDelCache", allEntries = true)
    public void limpiarCacheAlInicio() {
        // Se ejecuta al iniciar la aplicación y limpia el cache
    }
}