package com.project.bridgebackend.Adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DataController {

    private static final String PYTHON_API_URL = "http://127.0.0.1:5000/send_data";  // URL dell'API Python


    @PostMapping("/receive_data_from_python")
    public ResponseEntity<String> receiveDataFromPython(@RequestBody String jsonData) {
        // Elaborazione dei dati ricevuti (esempio)
        System.out.println("Dati ricevuti da Python: " + jsonData);

        // Rispondi con un messaggio di successo
        return new ResponseEntity<>("Dati ricevuti con successo", HttpStatus.OK);
    }

    @PostMapping("/sendToPython")
    public ResponseEntity<?> sendDataToPython(@RequestBody HashMap<String, Object> data) {
        try {
            // Spring deserializza automaticamente i dati JSON nella mappa
            System.out.println("Dati ricevuti da Spring: " + data);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(data, headers);
            ResponseEntity<String> response = restTemplate.exchange(PYTHON_API_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return ResponseEntity.ok(Map.of("message", "Dati inviati con successo a Python"));
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(Map.of("error", "Errore nell'invio dei dati a Python"));
            }
        } catch (Exception e) {
            e.printStackTrace();  // Aggiungi questa riga per visualizzare l'errore
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Errore durante l'invio dei dati a Python"));
        }
    }
}
