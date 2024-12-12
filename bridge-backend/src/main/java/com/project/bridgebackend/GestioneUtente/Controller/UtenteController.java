package com.project.bridgebackend.GestioneUtente.Controller;

import com.project.bridgebackend.GestioneUtente.Service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
@RequestMapping(path = "areaPersonale")
public class UtenteController {
    @Autowired
    private UtenteService utenteService;

    @DeleteMapping("/{email}")
    public ResponseEntity<String> eliminaUtente(@PathVariable("email") String email) {
        try {
            utenteService.eliminaUtente(email);
            return ResponseEntity.ok("Utente con email " + email + " eliminato con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'eliminazione dell'utente: " + e.getMessage());
        }
    }
}
