package com.project.bridgebackend.GestioneCorso.Controller;

import com.project.bridgebackend.GestioneCorso.Service.GestioneCorsoService;
import com.project.bridgebackend.GestioneCorso.pdf.PDFDoc;
import com.project.bridgebackend.GestioneCorso.pdf.PDFService;
import com.project.bridgebackend.Model.Entity.Corso;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.dao.CorsoDAO;
import com.project.bridgebackend.Model.dao.FiguraSpecializzataDAO;
import com.project.bridgebackend.Model.dto.CorsoDTO;
import com.project.bridgebackend.util.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author ...
 * Controller per la gestione dei corsi.
 */
@RestController
@RequestMapping("/api/corsi")
@CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
public class GestioneCorsoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GestioneCorsoController.class);

    @Autowired
    private GestioneCorsoService gestioneCorsoService;

    @Autowired
    private FiguraSpecializzataDAO figuraSpecializzataDAO;

    @Autowired
    private CorsoDAO corsoDAO;

    @Autowired
    private PDFService pdfService;

    @Autowired
    private JwtService jwtService;

    /**
     * Endpoint per creare un nuovo corso.
     *
     * @param authorizationHeader Header di autorizzazione contenente il token JWT
     * @param corsoDTO           DTO contenente i dati del corso da creare
     * @return ResponseEntity con il corso creato o errore
     */
    @PostMapping("/crea")
    public ResponseEntity<Corso> creaCorso(
            @RequestHeader("Authorization") final String authorizationHeader,
            @RequestBody @Valid final CorsoDTO corsoDTO) {
        try {
            // Estrai il token JWT dall'header Authorization
            String token = authorizationHeader.replace("Bearer ", "");
            // Estrai l'email dell'utente loggato dal token
            String emailUtenteAutenticato = jwtService.extractUsername(token);

            // Recupera la figura specializzata proprietaria dal database
            FiguraSpecializzata figuraSpecializzata = figuraSpecializzataDAO.findByEmail(emailUtenteAutenticato);
            if (figuraSpecializzata == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            // Creazione del nuovo corso
            Corso newCorso = new Corso();
            newCorso.setTitolo(corsoDTO.getTitolo());
            newCorso.setLingua(corsoDTO.getLingua());
            newCorso.setCategoriaCorso(corsoDTO.getCategoria());
            newCorso.setDescrizione(corsoDTO.getDescrizione());
            newCorso.setPdf(corsoDTO.getPdf()); // Associa l'ID del PDF
            newCorso.setProprietario(figuraSpecializzata); // Associa l'utente autenticato

            // Salva il corso nel database
            Corso createdCorso = gestioneCorsoService.creaCorso(newCorso);
            return new ResponseEntity<>(createdCorso, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error("Errore durante la creazione del corso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint per modificare un corso esistente.
     *
     * @param id                 l'ID del corso da modificare
     * @param authorizationHeader Header di autorizzazione contenente il token JWT
     * @param corsoDTO           DTO contenente i dati aggiornati del corso
     * @return ResponseEntity con il corso aggiornato o errore
     */
    @PostMapping("/modifica/{id}")
    public ResponseEntity<Corso> modificaCorso(
            @PathVariable final Long id,
            @RequestHeader("Authorization") final String authorizationHeader,
            @RequestBody @Valid final CorsoDTO corsoDTO) {
        try {
            // Estrai il token JWT dall'header Authorization
            String token = authorizationHeader.replace("Bearer ", "");
            // Estrai l'email dell'utente loggato dal token
            String emailUtenteAutenticato = jwtService.extractUsername(token);

            // Trova il corso usando l'ID passato nell'URL
            Corso corso = corsoDAO.findById(id).orElse(null);
            if (corso == null) {
                LOGGER.warn("Corso non trovato per ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Verifica che l'utente autenticato sia il proprietario del corso
            if (!corso.getProprietario().getEmail().equals(emailUtenteAutenticato)) {
                LOGGER.warn("L'utente {} non è autorizzato a modificare il corso con ID: {}", emailUtenteAutenticato, id);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            // Aggiorna i campi del corso
            corso.setTitolo(corsoDTO.getTitolo());
            corso.setLingua(corsoDTO.getLingua());
            corso.setCategoriaCorso(corsoDTO.getCategoria());
            corso.setDescrizione(corsoDTO.getDescrizione());
            corso.setPdf(corsoDTO.getPdf());

            // Salva il corso modificato
            Corso updatedCorso = gestioneCorsoService.modificaCorso(corso);

            return new ResponseEntity<>(updatedCorso, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Errore durante la modifica del corso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint per eliminare un corso esistente.
     *
     * @param id                 l'ID del corso da eliminare
     * @param authorizationHeader Header di autorizzazione contenente il token JWT
     * @return ResponseEntity con il risultato dell'operazione
     */
    @PostMapping("/elimina/{id}")
    public ResponseEntity<String> eliminaCorso(
            @PathVariable final Long id,
            @RequestHeader("Authorization") final String authorizationHeader) {
        try {
            // Estrai il token JWT dall'header Authorization
            String token = authorizationHeader.replace("Bearer ", "");
            // Estrai l'email dell'utente loggato dal token
            String emailUtenteAutenticato = jwtService.extractUsername(token);

            // Recupera il corso dal database
            Corso corso = corsoDAO.findById(id).orElse(null);
            if (corso == null) {
                LOGGER.warn("Corso non trovato per ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Corso non trovato.");
            }

            // Verifica che l'utente autenticato sia il proprietario del corso
            if (!corso.getProprietario().getEmail().equals(emailUtenteAutenticato)) {
                LOGGER.warn("L'utente {} non è autorizzato a eliminare il corso con ID: {}", emailUtenteAutenticato, id);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non sei autorizzato a eliminare questo corso.");
            }

            // Elimina il corso
            gestioneCorsoService.eliminaCorso(corso);
            return ResponseEntity.ok("Corso eliminato con successo.");
        } catch (Exception e) {
            LOGGER.error("Errore durante l'eliminazione del corso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'eliminazione del corso.");
        }
    }

    // Altri metodi (cercaCorso, findAll, downloadPDF) rimangono invariati o possono essere protetti similmente se necessario.

    /**
     * Endpoint per cercare un corso esistente tramite il suo ID.
     *
     * @param id l'ID del corso da cercare
     * @return ResponseEntity con il corso trovato o errore
     */
    @GetMapping("/cerca/{id}")
    public ResponseEntity<Corso> cercaCorso(
            @PathVariable final Long id) {
        try {
            Corso corso = corsoDAO.findById(id).orElse(null);
            if (corso == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(corso);
        } catch (Exception e) {
            LOGGER.error("Errore durante la ricerca del corso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint per ottenere la lista di tutti i corsi disponibili.
     *
     * @return ResponseEntity con la lista di corsi o errore
     */
    @GetMapping("/listaCorsi")
    public ResponseEntity<List<Corso>> findAll() {
        try {
            List<Corso> corsi = corsoDAO.findAll();
            if (corsi == null || corsi.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(corsi);
        } catch (Exception e) {
            LOGGER.error("Errore durante la ricerca del corso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint per il download di un file PDF associato a un corso.
     *
     * @param id       l'ID del corso per il quale scaricare il PDF
     * @param response l'oggetto HttpServletResponse per inviare il file PDF
     */
    @GetMapping("/download/{id}")
    public void downloadPDF(
            @PathVariable final Long id,
            final HttpServletResponse response) {
        try {
            // Recupera il corso dal database
            Corso corso = corsoDAO.findById(id).orElse(null);
            if (corso == null || corso.getPdf() == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Corso o PDF non trovato");
                return;
            }

            // Recupera il PDF associato al corso
            PDFDoc pdfDoc = pdfService.getPdf(corso.getPdf());
            if (pdfDoc == null || pdfDoc.getPdf() == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "PDF non trovato");
                return;
            }

            // Imposta i headers per il download
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + pdfDoc.getNomePdf());
            response.setContentLength(pdfDoc.getPdf().length);

            // Scrivi il contenuto del PDF nel response
            try (OutputStream outputStream = response.getOutputStream()) {
                outputStream.write(pdfDoc.getPdf());
            }
        } catch (Exception e) {
            LOGGER.error("Errore durante il download del PDF", e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il download del PDF");
            } catch (IOException ex) {
                LOGGER.error("Errore durante l'invio della risposta di errore", ex);
            }
        }
    }
}
