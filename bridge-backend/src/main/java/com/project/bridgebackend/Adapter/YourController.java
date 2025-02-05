package com.project.bridgebackend.Adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/bridge")
public class YourController {

    private static final Logger logger = LoggerFactory.getLogger(YourController.class);

    @PostMapping("/to-python")
    public String sendDataToPython(@RequestBody Map<String, Object> payload, @RequestHeader("Authorization") String authHeader) {
        logger.info("Ricevuta una richiesta POST per '/to-python' con payload: " + payload);
        logger.info("Authorization Header: " + authHeader);

        // Logica per trattare i dati ricevuti e inviare la risposta
        return "Dati ricevuti correttamente";
    }
}
