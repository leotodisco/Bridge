package com.project.bridgebackend.GestioneCorso.Controller;

import com.project.bridgebackend.GestioneCorso.Service.GestioneCorsoService;
import com.project.bridgebackend.GestioneCorso.pdf.PDFDoc;
import com.project.bridgebackend.GestioneCorso.pdf.PDFService;
import com.project.bridgebackend.Model.Entity.Corso;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.dao.CorsoDAO;
import com.project.bridgebackend.Model.dao.FiguraSpecializzataDAO;
import com.project.bridgebackend.Model.dto.CorsoDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


/**
 * @author Biagio Gallo.
 * Creato il: 06/12/2024.
 * Controller per la gestione dei corsi.
 */
@RestController
@RequestMapping("/api/corsi")
@CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
public class GestioneCorsoController {

    private static final Logger log = LoggerFactory.getLogger(GestioneCorsoController.class);

    @Autowired
    private GestioneCorsoService gestioneCorsoService;
    @Autowired
    private FiguraSpecializzataDAO figuraSpecializzataDAO;
    @Autowired
    private CorsoDAO corsoDAO;
    @Autowired
    private PDFService pdfService;


    // Endpoint per creare un nuovo corso
    @PostMapping("/crea")
    public ResponseEntity<Corso> creaCorso(@RequestBody @Valid CorsoDTO corsoDTO) {
        try {
            Corso newCorso = new Corso();
            newCorso.setTitolo(corsoDTO.getTitolo());
            newCorso.setLingua(corsoDTO.getLingua());
            newCorso.setCategoriaCorso(corsoDTO.getCategoria());
            newCorso.setDescrizione(corsoDTO.getDescrizione());
            newCorso.setPdf(corsoDTO.getPdf()); // Associa l'ID del PDF

            FiguraSpecializzata figuraSpecializzata = figuraSpecializzataDAO.findByEmail(corsoDTO.getProprietario());
            if (figuraSpecializzata == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            newCorso.setProprietario(figuraSpecializzata);

            Corso createdCorso = gestioneCorsoService.creaCorso(newCorso);
            return new ResponseEntity<>(createdCorso, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/upload")
    @CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
    public ResponseEntity<String> uploadPDF(@RequestParam("nome") String name, @RequestParam("pdf") MultipartFile file) {
        try {
            PDFDoc pdf = pdfService.savePdf(name, file);
            return ResponseEntity.ok(pdf.getId());
        } catch (IOException e) {
            log.error("Errore durante il caricamento del PDF", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il caricamento del PDF");
        }
    }

    @PostMapping("/modifica/{id}")
    @CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
    public ResponseEntity<Corso> modificaCorso(
            @PathVariable Long id,
            @RequestBody @Valid CorsoDTO corsoDTO) {
        try {
            // Trova il corso usando l'ID passato nell'URL
            Corso corso = corsoDAO.findById(id).orElse(null);
            if (corso == null) {
                log.warn("Corso non trovato per ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Trova il proprietario usando l'email dal DTO
            FiguraSpecializzata figuraSpecializzata = figuraSpecializzataDAO.findByEmail(corsoDTO.getProprietario());
            if (figuraSpecializzata == null) {
                log.warn("FiguraSpecializzata non trovata per email: {}", corsoDTO.getProprietario());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Controlla che il proprietario attuale sia quello corretto
            if (!corso.getProprietario().getEmail().equals(figuraSpecializzata.getEmail())) {
                log.warn("Il proprietario del corso non corrisponde. Corso: {}, FiguraSpecializzata: {}", corso.getProprietario(), figuraSpecializzata);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            // Aggiorna i campi del corso
            corso.setId(id);
            corso.setTitolo(corsoDTO.getTitolo());
            corso.setLingua(corsoDTO.getLingua());
            corso.setCategoriaCorso(corsoDTO.getCategoria());
            corso.setDescrizione(corsoDTO.getDescrizione());
            corso.setPdf(corsoDTO.getPdf());

            // Salva il corso modificato
            Corso updatedCorso = gestioneCorsoService.modificaCorso(corso);

            return new ResponseEntity<>(updatedCorso, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Errore durante la modifica del corso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Gestione errori generici
        }
    }

    @PostMapping("/elimina/{id}")
    @CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
    public ResponseEntity<String> eliminaCorso(@PathVariable Long id) {
        try {
            Corso corso = corsoDAO.findById(id).orElse(null);
            if (corso == null) {
                log.warn("Corso non trovato per ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            gestioneCorsoService.eliminaCorso(corso);
            return ResponseEntity.ok("Corso eliminato con successo");
        } catch (Exception e) {
            log.error("Errore durante l'eliminazione del corso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/cerca/{id}")
    @CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
    public ResponseEntity<Corso> cercaCorso(@PathVariable Long id) {
        try {
            Corso corso = corsoDAO.findById(id).orElse(null);
            if (corso == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(corso);
        } catch (Exception e) {
            log.error("Errore durante la ricerca del corso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/listaCorsi")
    @CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
    public ResponseEntity<List<Corso>> findAll() {
        try {
            List<Corso> corsi = corsoDAO.findAll();
            if (corsi == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(corsi);
        } catch (Exception e) {
            log.error("Errore durante la ricerca del corso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}