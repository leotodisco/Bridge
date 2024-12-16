package com.project.bridgebackend.GestioneEvento.controller;

import com.project.bridgebackend.GestioneEvento.service.GestioneEventoService;
import com.project.bridgebackend.Model.Entity.Evento;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.dao.RifugiatoDAO;
import com.project.bridgebackend.Model.dao.VolontarioDAO;
import com.project.bridgebackend.Model.dto.EventoDTO;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;


/**
 * @author Alessia De Filippo.
 * Creato il: 06/12/2024.
 * Controller per la gestione degli eventi.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
@RequestMapping("api/eventi")
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

    @Autowired
    private RifugiatoDAO rifugiatoDAO;

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        //Creazione entity Evento da DTO
        Evento evento = new Evento();
        evento.setNome(eventoDTO.getNome());
        evento.setData(LocalDate.parse(eventoDTO.getData()));
        evento.setOra(eventoDTO.getOra());
        evento.setLinguaParlata(eventoDTO.getLinguaParlata());
        evento.setDescrizione(eventoDTO.getDescrizione());
        evento.setLuogo(indirizzoDAO.getReferenceById(idIndirizzo));
        evento.setOrganizzatore(volontario);
        evento.setMaxPartecipanti(eventoDTO.getMaxPartecipanti());

        //Salvataggio evento in DB
        Evento createdEvent = gestioneEventoService.insertEvento(evento);

        //Risposta con evento creato
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    /**
     * Metodo per il fetch di un evento
     */
    @GetMapping("/retrieve/{id}")
    public ResponseEntity<EventoDTO> getEventoById(@PathVariable long id) {
        Optional<Evento> evento = gestioneEventoService.getEventoById(id);
        if(evento.isPresent()) {
            //Converto l'entity Evento in DTO
            EventoDTO eventoDTO = new EventoDTO();
            eventoDTO.setId(
                    evento.get().getId());
            eventoDTO.setNome(
                    evento.get().getNome());
            eventoDTO.setData(
                    evento.get().getData().toString());
            eventoDTO.setOra(
                    evento.get().getOra());
            eventoDTO.setLinguaParlata(
                    evento.get().getLinguaParlata());
            eventoDTO.setDescrizione(
                    evento.get().getDescrizione());
            eventoDTO.setLuogo(
                    evento.get().getLuogo());
            eventoDTO.setOrganizzatore(
                    evento.get().getOrganizzatore());
            eventoDTO.setMaxPartecipanti(
                    evento.get().getMaxPartecipanti());
            return new ResponseEntity<>(
                    eventoDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Metodo per l'iscrizione di un partecipante a un evento.
     * @param id
     * @param emailPartecipante
     * @return
     */
    @PostMapping("/{id}/iscrivi")
    public ResponseEntity<?> iscriviPartenope(
            @PathVariable long id,
            @RequestParam String emailPartecipante) {

        // Verifica che l'evento esista
        Evento evento = gestioneEventoService.getEventoById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento non trovato"));

        // Verifica che il rifugiato esista
        Rifugiato rifugiato = rifugiatoDAO.findByEmail(emailPartecipante);
        if(rifugiato == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rifugiato non trovato");
        }

        // Iscrivi il partecipante
        gestioneEventoService.iscrizioneEvento(id, emailPartecipante);

        return new ResponseEntity<>(evento, HttpStatus.OK);
    }


    /**
     * Metodo per la disiscrizione di un partecipante a un evento.
     * @param id identificativo dell'evento.
     * @param emailPartecipante email del partecipante.
     * @return evento aggiornato.
     */
    @DeleteMapping("/{id}/disiscrivi")
    public ResponseEntity<Evento> disiscriviPartecipante(
            @PathVariable long id, @RequestParam String emailPartecipante) {
        try {
            System.out.println("Disiscrivi partecipante: " + id + " " + emailPartecipante);
            Evento eventoAggiornato = gestioneEventoService.disiscrizioneEvento(id, emailPartecipante);
            System.out.println("Evento aggiornato: " + eventoAggiornato);
            return new ResponseEntity<>(eventoAggiornato, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            System.err.println("Errore: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // 400 se dati non validi
        } catch (Exception e) {
            System.err.println("Errore generico: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // 500 se errore interno
        }
    }


    /**
     * Metodo per il fetch di tutti gli eventi.
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity<List<Evento>> getAllEventi() {
        List<Evento> eventi = gestioneEventoService.getAllEventi();
        return new ResponseEntity<>(eventi, HttpStatus.OK);
    }

    /**
     * Metodo per il fetch degli eventi pubblicati da un volontario.
     * @param email email del volontario.
     * @return evento pubblicato.
     */
    @GetMapping("/pubblicati")
    public ResponseEntity<List<Evento>> getCorsiPubblicatiByVolontario(
            @RequestParam("email") String email) {
        System.out.println("Email ricevuta: " + email);

        // Recupera il volontario tramite email
        Volontario volontario = volontarioDAO.findByEmail(email);
        System.out.println("Volontario trovato: " + volontario);
        if(volontario == null) {
            System.out.println("Volontario non trovato");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        //Recupera i corsi pubblicati
        List<Evento> eventi = gestioneEventoService.getEventiByVolontario(email);
        eventi.forEach(System.out::println);
        if(eventi.isEmpty()) {
            System.out.println("Nessun evento trovato per questo volontario");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(eventi, HttpStatus.OK);
    }

    @GetMapping("/{id}/iscrizione")
    public ResponseEntity<Boolean> verificaIscrizione(
            @PathVariable long id,
            @RequestParam String emailPartecipante) {

        try {
            Evento evento = gestioneEventoService.trovaEventoConPartecipanti(id);
            Rifugiato partecipante = rifugiatoDAO.findByEmail(emailPartecipante);
            if (partecipante == null) {
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }

            boolean isIscritto = evento.getListaPartecipanti().contains(partecipante);
            return new ResponseEntity<>(isIscritto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
