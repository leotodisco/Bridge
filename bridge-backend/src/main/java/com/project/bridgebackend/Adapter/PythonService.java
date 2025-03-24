package com.project.bridgebackend.Adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class PythonService {

    private static final Logger logger = LoggerFactory.getLogger(PythonService.class);
    private final WebClient webClient;

    // Costruttore per WebClient
    public PythonService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:5000").build();
    }

    // Metodo che invia i dati al server Python
    public Mono<String> sendDataToPython(Map<String, Object> data) {
        logger.info("Invio dati a Python: {}", data);

        // Invia i dati come POST a Flask
        return webClient.post()
                .uri("/send_data")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(data)  // Corpo della richiesta
                .retrieve()
                .bodyToMono(String.class) // Ottieni la risposta come stringa
                .doOnError(error -> logger.error("Errore durante la richiesta a Python: {}", error.getMessage()));
    }
}
