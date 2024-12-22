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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;


/**
 * @author Alessia De Filippo.
 * Creato il: 06/12/2024.
 *
 * Controller per la gestione degli eventi.
 * Fornisce API REST per la creazione, il recupero,
 * la modifica e la cancellazione degli eventi.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
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

    /**
     * DAO per la gestione delle operazioni sui rifugiati.
     */
    @Autowired
    private RifugiatoDAO rifugiatoDAO;

    /**
     * Crea un nuovo evento.
     *
     * @param eventoDTO DTO che contiene i dati dell'evento da creare.
     * @return L'evento creato, incapsulato in una risposta HTTP con codice 201 (CREATED).
     */
    @PostMapping("/crea")
    public ResponseEntity<Evento> creaEvento(
            @Valid @RequestBody final EventoDTO eventoDTO) {


         // Recupero dati dell'indirizzo dell'evento.
         // Salvataggio in Indirizzo

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
     * Recupera un evento dato il suo ID.
     *
     * @param id Identificativo dell'evento da recuperare.
     * @return DTO dell'evento recuperato, o codice 404 (NOT FOUND) se non trovato.
     */

    @GetMapping("/retrieve/{id}")
    public ResponseEntity<EventoDTO> getEventoById(
            @PathVariable final long id) {
        Optional<Evento> evento = gestioneEventoService.getEventoById(id);
        if (evento.isPresent()) {
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
     * Iscrive un partecipante a un evento.
     *
     * @param id Identificativo dell'evento.
     * @param emailPartecipante Email del partecipante da iscrivere.
     * @return Evento aggiornato con il partecipante aggiunto, o un errore se il partecipante o l'evento non esiste.
     */
    @PostMapping("/{id}/iscrivi")
    public ResponseEntity<?> iscriviPartenope(
            @PathVariable final long id,
            @RequestParam final String emailPartecipante) {

        // Verifica che l'evento esista
        Evento evento = gestioneEventoService.getEventoById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento non trovato"));

        // Verifica che il rifugiato esista
        Rifugiato rifugiato = rifugiatoDAO.findByEmail(emailPartecipante);
        if (rifugiato == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rifugiato non trovato");
        }

        // Iscrivi il partecipante
        gestioneEventoService.iscrizioneEvento(id, emailPartecipante);

        return new ResponseEntity<>(evento, HttpStatus.OK);
    }


    /**
     * Rimuove l'iscrizione di un partecipante da un evento.
     *
     * @param id Identificativo dell'evento.
     * @param emailPartecipante Email del partecipante da disiscrivere.
     * @return L'evento aggiornato senza il partecipante, o un errore se i dati non sono validi.
     */
    @DeleteMapping("/{id}/disiscrivi")
    public ResponseEntity<Evento> disiscriviPartecipante(
            @PathVariable final long id,
            @RequestParam final String emailPartecipante) {
        try {
            System.out.println("Disiscrivi partecipante: "
                    + id + " " + emailPartecipante);
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
     * Recupera tutti gli eventi disponibili.
     *
     * @return Lista di tutti gli eventi presenti nel database.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Evento>> getAllEventi() {
        List<Evento> eventi = gestioneEventoService.getAllEventi();
        return new ResponseEntity<>(eventi, HttpStatus.OK);
    }


    /**
     * Recupera tutti gli eventi pubblicati da un volontario.
     *
     * @param email Email del volontario.
     * @return Lista degli eventi pubblicati dal volontario, o codice 404 (NOT FOUND) se non trovati.
     */
    @GetMapping("/pubblicati")
    public ResponseEntity<List<Evento>> getCorsiPubblicatiByVolontario(
            @RequestParam("email") final String email) {
        System.out.println("Email ricevuta: " + email);

        // Recupera il volontario tramite email
        Volontario volontario = volontarioDAO.findByEmail(email);
        System.out.println("Volontario trovato: " + volontario);
        if (volontario == null) {
            System.out.println("Volontario non trovato");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        //Recupera i corsi pubblicati
        List<Evento> eventi = gestioneEventoService.getEventiByVolontario(email);
        eventi.forEach(System.out::println);
        if (eventi.isEmpty()) {
            System.out.println("Nessun evento trovato per questo volontario");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(eventi, HttpStatus.OK);
    }

    /**
     * Verifica se un partecipante è iscritto a un evento.
     *
     * @param id Identificativo dell'evento.
     * @param emailPartecipante Email del partecipante.
     * @return `true` se il partecipante è iscritto, `false` altrimenti.
     */
    @GetMapping("/{id}/iscrizione")
    public ResponseEntity<Boolean> verificaIscrizione(
            @PathVariable final long id,
            @RequestParam final String emailPartecipante) {

        try {
            // Trova l'evento con i partecipanti
            Evento evento = gestioneEventoService.trovaEventoConPartecipanti(id);
            if (evento == null) {
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }

            // Trova il partecipante tramite email
            Rifugiato partecipante = rifugiatoDAO.findByEmail(emailPartecipante);
            if (partecipante == null) {
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }

            // Verifica se il partecipante è iscritto all'evento
            boolean isIscritto = evento.getListaPartecipanti().contains(partecipante);
            return new ResponseEntity<>(isIscritto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Recupera eventi casuali per la home page.
     *
     * @return Lista di eventi casuali.
     */
    @GetMapping("/random")
    public ResponseEntity<List<Evento>> getRandomEvents() {
        List<Evento> eventi = gestioneEventoService.getRandomEvents();
        return new ResponseEntity<>(eventi, HttpStatus.OK);
    }
}
