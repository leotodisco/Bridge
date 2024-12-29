package com.project.bridgebackend.GestioneCorso.Controller;

import com.project.bridgebackend.GestioneCorso.Service.GestioneCorsoService;
import com.project.bridgebackend.GestioneCorso.pdf.PDFDoc;
import com.project.bridgebackend.GestioneCorso.pdf.PDFService;
import com.project.bridgebackend.Model.Entity.Corso;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Utente;
import com.project.bridgebackend.Model.dao.CorsoDAO;
import com.project.bridgebackend.Model.dao.FiguraSpecializzataDAO;
import com.project.bridgebackend.Model.dao.UtenteDAO;
import com.project.bridgebackend.Model.dto.CorsoDTO;
import com.project.bridgebackend.util.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
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
    @Autowired
    private JwtService jwtService;
    @Qualifier("utenteDAO")
    @Autowired
    private UtenteDAO utenteDAO;

    /**
     * Endpoint per creare un nuovo corso.
     *
     * @param authorizationHeader Header di autorizzazione contenente il token JWT
     * @param corsoDTO DTO contenente i dati del corso da creare
     * @return ResponseEntity con il corso creato o errore
     */
    @PostMapping("/crea")
    public ResponseEntity<Corso> creaCorso(
            @RequestHeader("Authorization") final String authorizationHeader,
            @RequestBody @Valid CorsoDTO corsoDTO) {
        try {
            log.info("Richiesta per la creazione di un corso ricevuta.");
            log.info("Authorization Header: {}", authorizationHeader);

            // Estrai il token dall'header
            String token = authorizationHeader.replace("Bearer ", "");
            log.info("Token estratto: {}", token);

            // Estrai l'email dal token
            String emailUtenteAutenticato = jwtService.extractUsername(token);
            log.info("Email utente autenticato: {}", emailUtenteAutenticato);

            // Recupera la figura specializzata associata all'email
            FiguraSpecializzata figuraSpecializzata = figuraSpecializzataDAO.findByEmail(emailUtenteAutenticato);
            if (figuraSpecializzata == null) {
                log.warn("FiguraSpecializzata non trovata per email: {}", emailUtenteAutenticato);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            // Creazione del corso
            Corso newCorso = new Corso();
            newCorso.setTitolo(corsoDTO.getTitolo());
            newCorso.setLingua(corsoDTO.getLingua());
            newCorso.setCategoriaCorso(corsoDTO.getCategoria());
            newCorso.setDescrizione(corsoDTO.getDescrizione());
            newCorso.setPdf(corsoDTO.getPdf());
            newCorso.setProprietario(figuraSpecializzata);

            // Salvataggio del corso
            Corso createdCorso = gestioneCorsoService.creaCorso(newCorso);
            log.info("Corso creato con successo. ID: {}", createdCorso.getId());

            return new ResponseEntity<>(createdCorso, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Errore durante la creazione del corso: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            log.error("Errore generico durante la creazione del corso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint per caricare un file PDF.
     *
     * @param name Nome del file
     * @param file File PDF da caricare
     * @return ResponseEntity con l'ID del file PDF caricato
     */
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
    public ResponseEntity<Corso> modificaCorso(
            @PathVariable Long id,
            @RequestHeader("Authorization") final String authorizationHeader,
            @RequestBody @Valid CorsoDTO corsoDTO) {
        try {
            log.info("Authorization Header ricevuto: {}", authorizationHeader);
            String token = authorizationHeader.replace("Bearer ", "");
            log.info("Token estratto: {}", token);

            String emailUtenteLoggato = jwtService.extractUsername(token);
            log.info("Email utente loggato estratta: {}", emailUtenteLoggato);

            Corso corso = corsoDAO.findById(id).orElse(null);
            if (corso == null) {
                log.warn("Corso non trovato per ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            if (!corso.getProprietario().getEmail().equals(emailUtenteLoggato)) {
                log.warn("Accesso non autorizzato per l'utente: {}", emailUtenteLoggato);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            corso.setTitolo(corsoDTO.getTitolo());
            corso.setLingua(corsoDTO.getLingua());
            corso.setCategoriaCorso(corsoDTO.getCategoria());
            corso.setDescrizione(corsoDTO.getDescrizione());
            corso.setPdf(corsoDTO.getPdf());

            Corso updatedCorso = gestioneCorsoService.modificaCorso(corso);
            return new ResponseEntity<>(updatedCorso, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Errore durante la modifica del corso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/elimina/{id}")
    public ResponseEntity<String> eliminaCorso(@PathVariable Long id) {
        try {
            Corso corso = corsoDAO.findById(id).orElse(null);
            if (corso == null) {
                log.warn("Corso non trovato per ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Corso non trovato.");
            }

            gestioneCorsoService.eliminaCorso(corso);
            return ResponseEntity.ok("Corso eliminato con successo.");
        } catch (Exception e) {
            log.error("Errore durante l'eliminazione del corso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'eliminazione del corso.");
        }
    }

    @GetMapping("/cerca/{id}")
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
    public ResponseEntity<List<Corso>> findAll() {
        try {
            List<Corso> corsi = corsoDAO.findAll();
            return ResponseEntity.ok(corsi);
        } catch (Exception e) {
            log.error("Errore durante la ricerca del corso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint per ottenere la lista di tutti i corsi pubblicati da un utente.
     * @param email
     * @return ResponseEntity con la lista di corsi di un certo utente o errore
     */
    @GetMapping("/listaCorsiUtente/{email}")
    public ResponseEntity<List<Corso>> findAllUtente(@PathVariable final String email) {
        try {
            List<Corso> corsi = corsoDAO.findByProprietario_Email(email);
            if (corsi == null || corsi.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(corsi);
        } catch (Exception e) {
            log.error("Errore durante la ricerca dei corsi", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint per ottenere la lista di tutti i corsi pubblicati da un utente.
     * @param email
     * @return ResponseEntity con la lista di corsi di un certo utente o errore
     */
    @GetMapping("/listaCorsiUtentePartecipazioni/{email}")
    public ResponseEntity<List<Corso>> findAllPartecipazioniUtente(@PathVariable final String email) {
        try {
            List<Corso> corsi = corsoDAO.findByProprietario_Email(email);
            if (corsi == null || corsi.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(corsi);
        } catch (Exception e) {
            log.error("Errore durante la ricerca dei corsi", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/download/{id}")
    public void downloadPDF(@PathVariable Long id, HttpServletResponse response) {
        try {
            Corso corso = corsoDAO.findById(id).orElse(null);
            if (corso == null || corso.getPdf() == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Corso o PDF non trovato");
                return;
            }

            PDFDoc pdfDoc = pdfService.getPdf(corso.getPdf());
            if (pdfDoc == null || pdfDoc.getPdf() == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "PDF non trovato");
                return;
            }

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + pdfDoc.getNomePdf());
            response.setContentLength(pdfDoc.getPdf().length);

            try (OutputStream outputStream = response.getOutputStream()) {
                outputStream.write(pdfDoc.getPdf());
            }
        } catch (Exception e) {
            log.error("Errore durante il download del PDF", e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il download del PDF");
            } catch (IOException ex) {
                log.error("Errore durante l'invio della risposta di errore", ex);
            }
        }
    }
}
