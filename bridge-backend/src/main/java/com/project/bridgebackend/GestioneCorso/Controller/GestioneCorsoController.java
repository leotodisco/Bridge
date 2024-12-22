package com.project.bridgebackend.GestioneCorso.Controller;

import com.project.bridgebackend.GestioneCorso.Service.GestioneCorsoService;
import com.project.bridgebackend.GestioneCorso.pdf.PDFDoc;
import com.project.bridgebackend.GestioneCorso.pdf.PDFService;
import com.project.bridgebackend.Model.Entity.Corso;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.dao.CorsoDAO;
import com.project.bridgebackend.Model.dao.FiguraSpecializzataDAO;
import com.project.bridgebackend.Model.dto.CorsoDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
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


    /**
     * logger per la stampa di warning o errori.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GestioneCorsoController.class);

    /**
     * Servizi per la gestione dei corsi.
     */
    @Autowired
    private GestioneCorsoService gestioneCorsoService;


    /**
     * Servizi per la gestione degli utenti figura specializzata.
     */
    @Autowired
    private FiguraSpecializzataDAO figuraSpecializzataDAO;

    /**
     * DAO per la gestione dei corsi.
     */
    @Autowired
    private CorsoDAO corsoDAO;

    /**
     * Service per la gestione.
     */
    @Autowired
    private PDFService pdfService;


    /**
     * Endpoint per creare un nuovo corso.
     *
     * @param corsoDTO il DTO contenente i dati del corso da creare
     * @return ResponseEntity con il corso creato o errore
     */
    @PostMapping("/crea")
    public ResponseEntity<Corso> creaCorso(@RequestBody @Valid final CorsoDTO corsoDTO) {
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

    /**
     * Endpoint per caricare un file PDF associato al corso.
     *
     * @param name il nome del file PDF
     * @param file il file PDF da caricare
     * @return ResponseEntity con il risultato dell'operazione
     */
    @PostMapping("/upload")
    @CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
    public ResponseEntity<String> uploadPDF(
            @RequestParam("nome") final String name,
            @RequestParam("pdf") final MultipartFile file) {
        try {
            PDFDoc pdf = pdfService.savePdf(name, file);
            return ResponseEntity.ok(pdf.getId());
        } catch (IOException e) {
            LOGGER.error("Errore durante il caricamento del PDF", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il caricamento del PDF");
        }
    }

    /**
     * Endpoint per modificare un corso esistente.
     *
     * @param id l'ID del corso da modificare
     * @param corsoDTO il DTO contenente i dati aggiornati del corso
     * @return ResponseEntity con il corso aggiornato o errore
     */
    @PostMapping("/modifica/{id}")
    @CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
    public ResponseEntity<Corso> modificaCorso(
            @PathVariable final Long id,
            @RequestBody @Valid final CorsoDTO corsoDTO) {
        try {
            // Trova il corso usando l'ID passato nell'URL
            Corso corso = corsoDAO.findById(id).orElse(null);
            if (corso == null) {
                LOGGER.warn("Corso non trovato per ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Trova il proprietario usando l'email dal DTO
            FiguraSpecializzata figuraSpecializzata = figuraSpecializzataDAO.findByEmail(corsoDTO.getProprietario());
            if (figuraSpecializzata == null) {
                LOGGER.warn("FiguraSpecializzata non trovata per email: {}", corsoDTO.getProprietario());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Controlla che il proprietario attuale sia quello corretto
            if (!corso.getProprietario().getEmail().equals(figuraSpecializzata.getEmail())) {
                LOGGER.warn("Il proprietario del corso non corrisponde. Corso: {}, FiguraSpecializzata: {}", corso.getProprietario(), figuraSpecializzata);
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
            LOGGER.error("Errore durante la modifica del corso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Gestione errori generici
        }
    }


    /**
     * Endpoint per eliminare un corso esistente.
     *
     * @param id l'ID del corso da eliminare
     * @return ResponseEntity con il risultato dell'operazione
     */
    @PostMapping("/elimina/{id}")
    @CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
    public ResponseEntity<String> eliminaCorso(
            @PathVariable final Long id) {
        try {
            Corso corso = corsoDAO.findById(id).orElse(null);
            if (corso == null) {
                LOGGER.warn("Corso non trovato per ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            gestioneCorsoService.eliminaCorso(corso);
            return ResponseEntity.ok("Corso eliminato con successo");
        } catch (Exception e) {
            LOGGER.error("Errore durante l'eliminazione del corso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint per cercare un corso esistente tramite il suo ID.
     *
     * @param id l'ID del corso da cercare
     * @return ResponseEntity con il corso trovato o errore
     */
    @GetMapping("/cerca/{id}")
    @CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
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
    @CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
    public ResponseEntity<List<Corso>> findAll() {
        try {
            List<Corso> corsi = corsoDAO.findAll();
            if (corsi == null) {
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
     * @param id l'ID del corso per il quale scaricare il PDF
     * @param response l'oggetto HttpServletResponse per inviare il file PDF
     */
    @GetMapping("/download/{id}")
    @CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
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
