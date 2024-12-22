package com.project.bridgebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * La classe principale per l'avvio dell'applicazione backend.
 * <p>
 * Questo è il punto di ingresso dell'applicazione Spring Boot.
 * Al suo avvio, la classe esegue il metodo main,
 * che avvia il contesto Spring Boot,
 * caricando la configurazione e le impostazioni necessarie per l'applicazione.
 * </p>
 */
@SpringBootApplication
public class BridgeBackendApplication {

    /**
     * Il metodo main per l'avvio dell'applicazione Spring Boot.
     * <p>
     * Questo metodo avvia l'applicazione backend,
     * che inizializza il contesto Spring Boot e,
     * consente l'esecuzione del server.
     * </p>
     * @param args I parametri di avvio dell'applicazione.
     */
    public static void main(final String[] args) {
        System.out.println("ciao!!!!!!");
        SpringApplication.run(BridgeBackendApplication.class, args);
    }

}
