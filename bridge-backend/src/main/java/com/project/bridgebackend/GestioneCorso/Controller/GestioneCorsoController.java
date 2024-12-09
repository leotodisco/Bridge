package com.project.bridgebackend.GestioneCorso.Controller;

import com.project.bridgebackend.GestioneCorso.Service.GestioneCorsoService;
import com.project.bridgebackend.GestioneCorso.pdf.PDFDoc;
import com.project.bridgebackend.GestioneCorso.pdf.PDFService;
import com.project.bridgebackend.Model.Entity.Corso;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
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
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class GestioneCorsoController {

    private static final Logger log = LoggerFactory.getLogger(GestioneCorsoController.class);

    @Autowired
    private GestioneCorsoService gestioneCorsoService;
    @Autowired
    private FiguraSpecializzataDAO figuraSpecializzataDAO;

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
    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    public ResponseEntity<String> uploadPDF(@RequestParam("nome") String name, @RequestParam("pdf") MultipartFile file) {
        try {
            PDFDoc pdf = pdfService.savePdf(name, file);
            return ResponseEntity.ok(pdf.getId());
        } catch (IOException e) {
            log.error("Errore durante il caricamento del PDF", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il caricamento del PDF");
        }
    }

}