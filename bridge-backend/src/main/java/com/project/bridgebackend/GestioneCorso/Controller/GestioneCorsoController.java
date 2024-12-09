package com.project.bridgebackend.GestioneCorso.Controller;

import com.project.bridgebackend.GestioneCorso.Service.GestioneCorsoService;
import com.project.bridgebackend.Model.Entity.Corso;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.dao.FiguraSpecializzataDAO;
import com.project.bridgebackend.Model.dto.CorsoDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Biagio Gallo.
 * Creato il: 06/12/2024.
 * Controller per la gestione dei corsi.
 */
@RestController
@RequestMapping("/api/corsi")
public class GestioneCorsoController {

    @Autowired
    private GestioneCorsoService gestioneCorsoService;
    @Autowired
    private FiguraSpecializzataDAO figuraSpecializzataDAO;

    // Endpoint per creare un nuovo corso
    @PostMapping("/crea")
    public ResponseEntity<Corso> creaCorso(@Valid @RequestBody CorsoDTO corsoDTO) {
        Corso newCorso = new Corso();
        newCorso.setTitolo(corsoDTO.getTitolo());
        System.out.println("Titolo: " + newCorso.getTitolo());
        newCorso.setLingua(corsoDTO.getLingua());
        System.out.println("Lingua: " + newCorso.getLingua());
        newCorso.setCategoriaCorso(corsoDTO.getCategoriaCorso());
        System.out.println("Categoria: " + newCorso.getCategoriaCorso());
        newCorso.setDescrizione(corsoDTO.getDescrizione());
        System.out.println("Descrizione: " + newCorso.getDescrizione());
        newCorso.setPdf(corsoDTO.getPdf());
        System.out.println("PDF: " + newCorso.getPdf());

        FiguraSpecializzata figuraSpecializzata = figuraSpecializzataDAO.findByEmail(corsoDTO.getProprietario().getEmail());
        newCorso.setProprietario(figuraSpecializzata);
        System.out.println("Proprietario: " + figuraSpecializzata);

        if (figuraSpecializzata == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Corso createdCorso = gestioneCorsoService.creaCorso(newCorso);
        System.out.println("corso creato: " + createdCorso);
        return new ResponseEntity<>(createdCorso, HttpStatus.CREATED);
    }

}