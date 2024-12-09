package com.project.bridgebackend.GestioneAlloggio.Controller;

import com.project.bridgebackend.GestioneAlloggio.Service.AlloggioService;
import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.dto.AlloggioDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per la gestione degli alloggi
 */
@RestController
@RequestMapping("/alloggi")
public class AlloggioController {

    @Autowired
    private AlloggioService alloggioService;

    /**
     * Aggiungi un nuovo alloggio nel sistema
     *
     * @param alloggio   l'alloggio da aggiungere
     * @return ResponseEntity con lo stato dell'operazione
     */

    @PostMapping("/aggiungi")
    public ResponseEntity<String> addAlloggio(@RequestBody @Valid AlloggioDTO alloggio) {
        try {
            /**
             *Verifico prima se sia un volontoria, in caso positivo può aggiungere l'alloggio, in caso negativo non fa nulla
             */
            //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            //if (authentication != null && authentication.getAuthorities().stream()
                    //.anyMatch(auth -> auth.getAuthority().equals("Volontario"))) {
                boolean result = alloggioService.addAlloggio(alloggio);
                if (result) {
                    return new ResponseEntity<>("Alloggio aggiunto con successo", HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>("Errore nell'aggiunta dell'alloggio", HttpStatus.BAD_REQUEST);
                }
            //} //else {
               // return new ResponseEntity<>("Accesso negato: Solo i volontari possono aggiungere alloggi", HttpStatus.FORBIDDEN);
            //}
        } catch (Exception e) {
            return new ResponseEntity<>("Errore interno del server: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}