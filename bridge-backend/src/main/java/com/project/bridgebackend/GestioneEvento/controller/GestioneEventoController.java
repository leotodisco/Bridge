package com.project.bridgebackend.GestioneEvento.controller;

import com.project.bridgebackend.GestioneEvento.service.GestioneEventoService;
import com.project.bridgebackend.Model.Entity.Evento;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.dao.VolontarioDAO;
import com.project.bridgebackend.Model.dto.EventoDTO;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Alessia De Filippo.
 * Creato il: 06/12/2024.
 * Controller per la gestione degli eventi.
 */
@RestController
@RequestMapping("/eventi")
public class GestioneEventoController {

    /**
     * Service per la gestione degli eventi.
     */
    @Autowired
    private GestioneEventoService gestioneEventoService;

    /**
     * DAO per la gestione degli indirizzi.
     */
    @Autowired
    private IndirizzoDAO indirizzoDAO;

    /**
     * DAO per la gestione dei volontari.
     */
    @Autowired
    private VolontarioDAO volontarioDAO;

    /**
     * Metodo per la creazione di un evento.
     * @param eventoDTO evento da creare.
     * @return evento creato.
     */
    @PostMapping("/crea")
    public ResponseEntity<Evento> creaEvento(
            @Valid @RequestBody final EventoDTO eventoDTO) {

        /*
         * Recupero dati dell'indirizzo dell'evento.
         * Salvataggio in Indirizzo
         */
        Indirizzo indirizzo = new Indirizzo();
        indirizzo.setVia(
                eventoDTO.getLuogo().getVia());
        indirizzo.setNumCivico(
                eventoDTO.getLuogo().getNumCivico());
        indirizzo.setCap(
                eventoDTO.getLuogo().getCap());
        indirizzo.setCitta(
                eventoDTO.getLuogo().getCitta());
        indirizzo.setProvincia(
                eventoDTO.getLuogo().getProvincia());

        //Salvataggio indirizzo in DB
        long idIndirizzo = gestioneEventoService
                .salvaIndirizzoEvento(indirizzo);

        //Recupero volontario organizzatore dell'evento
        Volontario volontario = volontarioDAO
                .findByEmail(eventoDTO.getOrganizzatore().getEmail());
        //Controllo se il volontario esiste
        if (volontario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        //Creazione entity Evento da DTO
        Evento evento = new Evento();
        evento.setNome(eventoDTO.getNome());
        evento.setData(eventoDTO.getData());
        evento.setOra(eventoDTO.getOra());
        evento.setLingueParlate(eventoDTO.getLingueParlate());
        evento.setDescrizione(eventoDTO.getDescrizione());
        evento.setLuogo(indirizzoDAO.getReferenceById(idIndirizzo));
        evento.setOrganizzatore(volontario);

        //Salvataggio evento in DB
        Evento createdEvent = gestioneEventoService.insertEvento(evento);

        //Risposta con evento creato
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }
}
