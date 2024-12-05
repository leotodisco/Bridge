package com.project.bridgebackend.registrazione.controller;

import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import com.project.bridgebackend.Model.dto.UtenteDTO;
import com.project.bridgebackend.registrazione.service.RegistrazioneService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping(path = "authentication")
public class RegistrazioneController {
    @Autowired
    private RegistrazioneService registrazioneService;

    @PostMapping(value = "/registrazioneVolontario")
    public ResponseEntity<String> registrazione(@RequestBody UtenteDTO volontario) throws Exception {
        try {
            // Estrazione dei dati da UtenteDTO
            String nome = volontario.getNomeUtente();
            String cognome = volontario.getCognomeUtente();
            String email = volontario.getEmailUtente();
            String password = volontario.getPasswordUtente();
            String salt = volontario.getSaltUtente();
            String nazionalita = volontario.getNazionalitaUtente();
            String lingueParlate = volontario.getLingueParlateUtente();
            Gender genere = volontario.getGenderUtente();
            Ruolo ruolo = volontario.getRuoloUtente();
            LocalDate dataNascita = volontario.getDataNascitaUtente();
            String Skill = volontario.getSkillUtente();
            TitoloDiStudio titoloDiStudio = volontario.getTitoloDiStudioUtente();
            byte[] fotoProfilo = volontario.getFotoUtente();

            // Crea un nuovo oggetto Volontario con i dati estratti
            Volontario v = new Volontario(email, nome, cognome, lingueParlate, fotoProfilo, Skill, dataNascita, titoloDiStudio, ruolo, genere, salt, nazionalita, password);

            // Chiamata al service per registrare il volontario
            registrazioneService.registraVolontario(v, volontario.getPasswordUtente());

            return ResponseEntity.ok("Registrazione avvenuta con successo!");
        } catch (ConstraintViolationException e) {
            // Errori di validazione (come annotazioni @NotNull, @Pattern, ecc.)
            StringBuilder errorMessage = new StringBuilder("Errore di validazione: ");
            e.getConstraintViolations().forEach(violation ->
                    errorMessage.append(violation.getMessage()).append(". "));

            // Stampa l'errore completo nel log
            e.printStackTrace();

            // Restituisce il messaggio di errore con codice 400 (Bad Request)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
        } catch (IllegalArgumentException e) {
            // Errori generici, come password non combacianti o volontario già esistente
            // Stampa l'errore completo nel log
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Gestione di altri errori generici
            // Stampa l'errore completo nel log
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la registrazione.");
        }
    }


    @GetMapping("/test")
    public ResponseEntity<String> testMessage() {
        String message = "Questo è un semplice messaggio!";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
