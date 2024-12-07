package com.project.bridgebackend.GestioneCorso.Controller;

import com.project.bridgebackend.GestioneCorso.Service.GestioneCorsoService;
import com.project.bridgebackend.Model.dto.CorsoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/corsi")
public class GestioneCorsoController {

    @Autowired
    private GestioneCorsoService gestioneCorsoService;

    // Endpoint per creare un nuovo corso
    @PostMapping("/crea")
    public ResponseEntity<CorsoDTO> creaCorso(@RequestBody CorsoDTO corsoDTO) {
        CorsoDTO createdCorso = gestioneCorsoService.creaCorso(corsoDTO);
        return ResponseEntity.ok(createdCorso);
    }

    // Endpoint per modificare un corso esistente
    @PutMapping("/modifica")
    public ResponseEntity<CorsoDTO> modificaCorso(@RequestBody CorsoDTO corsoDTO) {
        CorsoDTO updatedCorso = gestioneCorsoService.modificaCorso(corsoDTO);
        return ResponseEntity.ok(updatedCorso);
    }

    // Endpoint per eliminare un corso esistente
    @DeleteMapping("/elimina")
    public ResponseEntity<Void> eliminaCorso(@RequestBody CorsoDTO corsoDTO) {
        gestioneCorsoService.eliminaCorso(corsoDTO);
        return ResponseEntity.noContent().build();
    }

    // Endpoint per ottenere tutti i corsi
    @GetMapping("/tutti")
    public ResponseEntity<List<CorsoDTO>> findAll() {
        List<CorsoDTO> corsi = gestioneCorsoService.findAll();
        return ResponseEntity.ok(corsi);
    }
}