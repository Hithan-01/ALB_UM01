package com.demo.alb_um.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.demo.alb_um.DTOs.DatosAlumnoAPI;

@Service
public class ApiVigilanciaServicio {

    private final WebClient webClient;

    public ApiVigilanciaServicio(WebClient.Builder webClientBuilder, 
                                 @Value("${api.vigilancia.url}") String apiUrl) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

    public DatosAlumnoAPI obtenerDatosPorMatricula(String matricula) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/vigilancia/consulta")
                                             .queryParam("codigo", matricula)
                                             .build())
                .retrieve()
                .bodyToMono(DatosAlumnoAPI.class)
                .block(); 
    }
}
