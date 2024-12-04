package com.project.bridgebackend.GestioneAlloggio.Controller;

import com.project.bridgebackend.GestioneAlloggio.Service.AlloggioService;
import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.Entity.Volontario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * @param idAlloggio l'identificativo dell'alloggio
     * @return ResponseEntity con lo stato dell'operazione
     */
    @PostMapping("/aggiungi")
    public ResponseEntity<String> addAlloggio(@RequestBody Alloggio alloggio,
                                              @RequestParam int idAlloggio) {
        boolean result = alloggioService.addAlloggio(alloggio, idAlloggio);

        if (result) {
            return new ResponseEntity<>("Alloggio aggiunto con successo", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Errore nell'aggiunta dell'alloggio", HttpStatus.BAD_REQUEST);
        }
    }
}