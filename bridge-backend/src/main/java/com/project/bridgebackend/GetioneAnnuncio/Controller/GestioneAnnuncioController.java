package com.project.bridgebackend.GetioneAnnuncio.Controller;


import com.project.bridgebackend.GetioneAnnuncio.Service.GestioneAnnuncioService;
import com.project.bridgebackend.Model.Entity.*;
import com.project.bridgebackend.Model.dao.FiguraSpecializzataDAO;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
import com.project.bridgebackend.Model.dto.ConsulenzaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import javax.validation.Valid;


/**
 * @author Geraldine Montella.
 * Creato il: 03/12/2024.

 * Controller per la gestione degli annunci.
 * Questo controller gestisce le richieste HTTP legate alla creazione di annunci
 * di consulenza, includendo validazione e interazione con il database.
 */

@RestController
@RequestMapping("/api/annunci")
public class GestioneAnnuncioController {

    /**
     * Service per la logica di gestione degli annunci.
     */
    @Autowired
    private GestioneAnnuncioService gestioneAnnuncioService;

    /**
     * DAO per accedere ai dati degli utenti-> figure specializzate
     */
    @Autowired
    private FiguraSpecializzataDAO figuraSpecializzataDAO;

    /**
     * DAO per accedere ai dati degli indirizzi.
     */
    @Autowired
    private IndirizzoDAO indirizzoDAO;

    /**
     * Metodo per creare una nuova consulenza.
     * Questo metodo riceve un oggetto DTO contenente i dati della consulenza,
     * valida l'input, e crea le entità necessarie nel database.
     *
     * @param consulenzaDTO DTO contenente i dati della consulenza.
     *                      Include informazioni sull'indirizzo, il proprietario,
     *                      e altri dettagli rilevanti.
     *                      semplifica la gestione e validazione dei dati passati con JSON
     * @return ResponseEntity contenente l'entità `Consulenza` appena creata
     *         oppure un errore se le operazioni non vanno a buon fine.
     */
    @PostMapping
    public ResponseEntity<Consulenza> creaConsulenza(@Valid @RequestBody ConsulenzaDTO consulenzaDTO) {

        /*
         * Prendendo le informazioni dal DTO di consulenza
         * genera anche l'entity indirizzo in modo da poterne
         * salvare il contenuto nel db
         */
        Indirizzo indirizzo = new Indirizzo();
        indirizzo.setVia(consulenzaDTO.getIndirizzo().getVia());
        indirizzo.setCitta(consulenzaDTO.getIndirizzo().getCitta());
        indirizzo.setCap(consulenzaDTO.getIndirizzo().getCap());
        indirizzo.setProvincia(consulenzaDTO.getIndirizzo().getProvincia());
        indirizzo.setNumCivico(consulenzaDTO.getIndirizzo().getNumCivico());
        System.out.println(indirizzo.getNumCivico());

        /*
         * si occupa di salvare l'indirizzo nel database
         */
        Long indirizzoId = gestioneAnnuncioService.salvaIndirizzoConsulenza(indirizzo);

        /*
         *Creazione dell'entità Consulenza a partire dai dati del DTO.
        */
        Consulenza consulenza = new Consulenza();
        consulenza.setTitolo(consulenzaDTO.getTitolo());
        consulenza.setDescrizione(consulenzaDTO.getDescrizione());
        consulenza.setDisponibilita(consulenzaDTO.getDisponibilita());
        consulenza.setMaxCandidature(consulenzaDTO.getMaxCandidature());
        consulenza.setCandidati(consulenzaDTO.getCandidati());
        consulenza.setOrariDisponibili(consulenzaDTO.getOrariDisponibili());
        consulenza.setNumero(consulenzaDTO.getNumero());
        consulenza.setTipo(consulenzaDTO.getTipo());
        consulenza.setTipologia(true);
        consulenza.setIndirizzo(indirizzoDAO.getReferenceById(indirizzoId));

        /*
         * Recupero della figura specializzata proprietaria della consulenza.
         */
        FiguraSpecializzata figuraSpecializzata = figuraSpecializzataDAO.findByEmail(consulenzaDTO.getProprietario());
        if (figuraSpecializzata == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        /*
         * Associa la figura specializzata come proprietario della consulenza.
         */
        consulenza.setProprietario(figuraSpecializzata);

        /*
         * Salvataggio della consulenza nel database.
         */
        Consulenza nuovaConsulenza = gestioneAnnuncioService.inserimentoConsulenza(consulenza);

        return new ResponseEntity<>(nuovaConsulenza, HttpStatus.CREATED);
    }

}
