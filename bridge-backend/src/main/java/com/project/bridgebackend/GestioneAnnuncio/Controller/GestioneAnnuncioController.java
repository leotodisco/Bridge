package com.project.bridgebackend.GestioneAnnuncio.Controller;

import com.project.bridgebackend.GestioneAnnuncio.Service.GestioneAnnuncioService;
import com.project.bridgebackend.GestioneCorso.Controller.GestioneCorsoController;
import com.project.bridgebackend.Model.Entity.Consulenza;
import com.project.bridgebackend.Model.Entity.Lavoro;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Utente;

import com.project.bridgebackend.Model.dao.*;
import com.project.bridgebackend.Model.dto.ConsulenzaDTO;
import com.project.bridgebackend.Model.dto.LavoroDTO;

import com.project.bridgebackend.util.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;


/**
 * @author Geraldine Montella, Vito Vernellati.
 * Creato il: 03/12/2024.
 *
 * Controller per la gestione degli annunci.
 * Questo controller gestisce le richieste HTTP legate alla creazione di annunci.
 * di consulenza, includendo validazione e interazione con il database.
 */

@RestController
@RequestMapping("/api/annunci")
public class GestioneAnnuncioController {

    /**
    * logger per la stampa di warning o errori
     */
    private static final Logger log = LoggerFactory.getLogger(GestioneCorsoController.class);

    /**
     * DAO per accedere ai dati dell' utente.
     */

    @Autowired
    private UtenteDAO utenteDAO;

    /**
     * Service per la logica di gestione degli annunci.
     */
    @Autowired
    private GestioneAnnuncioService gestioneAnnuncioService;

    /**
     * DAO per accedere ai dati delle figure specializzate.
     */
    @Autowired
    private FiguraSpecializzataDAO figuraSpecializzataDAO;

    /**
     * DAO per accedere ai dati dei volontari.
     */
    @Autowired
    private VolontarioDAO volontarioDAO;

    /**
     * DAO per accedere ai dati degli indirizzi.
     */
    @Autowired
    private IndirizzoDAO indirizzoDAO;
    @Autowired
    private ConsulenzaDAO consulenzaDAO;

    @Autowired
    private JwtService jwtService;


    /**
     * Metodo per creare una nuova consulenza.
     * Questo metodo riceve un oggetto DTO contenente i dati della consulenza,
     * valida l'input, e crea le entità necessarie nel database.
     *
     * @param consulenzaDTO DTO contenente i dati della consulenza.
     *                      Include informazioni sull'indirizzo,
     *                      il proprietario e altri dettagli rilevanti.
     *                      Semplifica la gestione e validazione dei dati passati con JSON.
     * @return ResponseEntity contenente l'entità `Consulenza` appena creata.
     *         Oppure un errore se le operazioni non vanno a buon fine.
     */
    @PostMapping("/creaConsulenza")
    public ResponseEntity<Consulenza> creaConsulenza(@Valid @RequestBody final ConsulenzaDTO consulenzaDTO) {

        /*
         * Prendendo le informazioni dal DTO di consulenza,
         * genera anche l'entity indirizzo in modo da poterne,
         * salvare il contenuto nel db.
         */
        Indirizzo indirizzo = new Indirizzo();
        indirizzo.setVia(consulenzaDTO.getIndirizzo().getVia());
        indirizzo.setCitta(consulenzaDTO.getIndirizzo().getCitta());
        indirizzo.setCap(consulenzaDTO.getIndirizzo().getCap());
        indirizzo.setProvincia(consulenzaDTO.getIndirizzo().getProvincia());
        indirizzo.setNumCivico(consulenzaDTO.getIndirizzo().getNumCivico());

        //si occupa di salvare l'indirizzo nel database.
        Long indirizzoId = gestioneAnnuncioService.salvaIndirizzoConsulenza(indirizzo);

        //Creazione dell'entità Consulenza a partire dai dati del DTO.
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

        // Recupero della figura specializzata proprietaria della consulenza.
        FiguraSpecializzata figuraSpecializzata =
                figuraSpecializzataDAO.findByEmail(consulenzaDTO.getProprietario());
        if (figuraSpecializzata == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        //Associa la figura specializzata come proprietario della consulenza.
        consulenza.setProprietario(figuraSpecializzata);

        //Salvataggio della consulenza nel database.
        Consulenza nuovaConsulenza = gestioneAnnuncioService.inserimentoConsulenza(consulenza);

        return new ResponseEntity<>(nuovaConsulenza, HttpStatus.CREATED);
    }


    /**
     * Metodo per creare un nuovo annuncio di lavoro.
     * Questo metodo riceve un oggetto DTO contenente i dati del lavoro,
     * valida l'input, e crea le entità necessarie nel database.
     *
     * @param lavoroDTO DTO contenente i dati del lavoro.
     *                  Include informazioni sull'indirizzo,
     *                  il proprietario e altri dettagli rilevanti.
     *                  Semplifica la gestione e validazione dei dati passati con JSON.
     * @return ResponseEntity contenente l'entità `Lavoro` appena creata.
     *         Oppure un errore se le operazioni non vanno a buon fine.
     */
    @PostMapping("/creaLavoro")
    public ResponseEntity<Lavoro> creaLavoro(@Valid @RequestBody final LavoroDTO lavoroDTO) {

        /*
         * Creazione dell'entità Indirizzo a partire dai dati del DTO di lavoro.
         */
        Indirizzo indirizzo = new Indirizzo();
        indirizzo.setVia(lavoroDTO.getIndirizzo().getVia());
        indirizzo.setCitta(lavoroDTO.getIndirizzo().getCitta());
        indirizzo.setCap(lavoroDTO.getIndirizzo().getCap());
        indirizzo.setProvincia(lavoroDTO.getIndirizzo().getProvincia());
        indirizzo.setNumCivico(lavoroDTO.getIndirizzo().getNumCivico());

        // Salvataggio dell'indirizzo nel database.
        Long indirizzoId = gestioneAnnuncioService.salvaIndirizzoLavoro(indirizzo);

        // Recupero del proprietario dell'annuncio come Utente.
        Volontario volontario = volontarioDAO.findByEmail(lavoroDTO.getProprietario());
        if (volontario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Creazione dell'entità Lavoro a partire dai dati del DTO.
        Lavoro lavoro = new Lavoro();
        lavoro.setTitolo(lavoroDTO.getTitolo());
        lavoro.setDisponibilita(lavoroDTO.getDisponibilita());
        lavoro.setIndirizzo(indirizzoDAO.getReferenceById(indirizzoId));
        lavoro.setMaxCandidature(lavoroDTO.getMaxCandidature());
        lavoro.setCandidati(lavoroDTO.getCandidati());
        lavoro.setTipologia(true);

        // Impostazione dei campi specifici di Lavoro.
        lavoro.setPosizioneLavorativa(lavoroDTO.getPosizioneLavorativa());
        lavoro.setNomeAzienda(lavoroDTO.getNomeAzienda());
        lavoro.setOrarioLavoro(lavoroDTO.getOrarioLavoro());
        lavoro.setTipoContratto(lavoroDTO.getTipoContratto());
        lavoro.setRetribuzione(lavoroDTO.getRetribuzione());
        lavoro.setNomeSede(lavoroDTO.getNomeSede());
        lavoro.setInfoUtili(lavoroDTO.getInfoUtili());

        // Impostazione del proprietario.
        lavoro.setProprietario(volontario);

        // Salvataggio del lavoro nel database.
        Lavoro nuovoLavoro = gestioneAnnuncioService.inserimentoLavoro(lavoro);

        return new ResponseEntity<>(nuovoLavoro, HttpStatus.CREATED);
    }

    /**
     * Metodo per ottenere tutte le consulenze presenti nel database.
     *
     * @return ResponseEntity contenente la lista di tutte le consulenze.
     */
    @GetMapping("/view_consulenze")
    public ResponseEntity<List<Consulenza>> getAllConsulenze() {
        List<Consulenza> consulenze = gestioneAnnuncioService.getAllConsulenze();
        System.out.println(consulenze);
        return ResponseEntity.ok(consulenze);
    }

    /**
     * Metodo per ottenere una consulenza specifica dal DB
     *
     * @return ResponseEntity contenente la consulenza
     */
    @GetMapping("/view_consulenze/retrive/{id}")
    public ResponseEntity<Consulenza> getConsulenzaById(@PathVariable long id) {
        Consulenza consulenza = gestioneAnnuncioService.getConsulenze(id);
        System.out.println(consulenza);
        return ResponseEntity.ok(consulenza);
    }

    /**
     * Metodo per ottenere tutte le consulenze di un proprietario specifico.
     *
     * @param proprietarioId ID del proprietario delle consulenze.
     * @return ResponseEntity contenente la lista delle consulenze del proprietario specificato.
     */
    @PostMapping("/view_consulenze/proprietario/{id}")
    public ResponseEntity<List<Consulenza>> getConsulenzeByProprietario(@PathVariable("id") String proprietarioId) {
        // Retrieve the FiguraSpecializzata (which is a subtype of Utente)
        Utente proprietario = figuraSpecializzataDAO.findByEmail(proprietarioId);

        // Retrieve all Consulenza entities for the specific proprietario
        List<Consulenza> consulenze = gestioneAnnuncioService.getConsulenzeByProprietario(proprietario);

        return ResponseEntity.ok(consulenze);
    }


    @PostMapping("/modifica_consulenza/{idConsulenza}")
    public ResponseEntity<?> modificaConsulenza(@PathVariable long idConsulenza,
                                                @RequestBody HashMap<String, Object> aggiornamenti,
    @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Estrai il token JWT dall'header Authorization
            String token = authorizationHeader.replace("Bearer ", "");
            //per estrarre l'email dal token
            String emailUtenteLoggato = jwtService.extractUsername(token);

            // Verifica se l'utente è il proprietario dell'annuncio
            Consulenza consulenzaEsistente = gestioneAnnuncioService.getConsulenze(idConsulenza);
            if (!consulenzaEsistente.getProprietario().getEmail().equals(emailUtenteLoggato)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Non sei autorizzato a modificare questa consulenza.");
            }

            // Invoca il metodo del servizio per modificare la consulenza
            Consulenza consulenzaAggiornata = gestioneAnnuncioService.modificaAnnuncioConsulenza(idConsulenza, aggiornamenti);

            // Restituisce la consulenza aggiornata con codice HTTP 200
            return ResponseEntity.ok(consulenzaAggiornata);

        } catch (IllegalArgumentException e) {
            // Gestisce errori come consulenza non trovata o campi non validi
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            // Gestisce errori generici
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'aggiornamento della consulenza: " + e.getMessage());
        }
    }


    /**
     * Metodo per ottenere tutti gli annunci di lavoro presenti nel database.
     *
     * @return ResponseEntity contenente la lista di tutti gli annunci di lavoro.
     */
    @GetMapping("/view_lavori")
    public ResponseEntity<List<Lavoro>> getAllLavori() {
        List<Lavoro> lavori = gestioneAnnuncioService.getAllLavori();
        System.out.println(lavori);
        return ResponseEntity.ok(lavori); // Ritorna lista vuota con HTTP 200
    }


    /**
     * Metodo per ottenere un annuncio di lavoro specifico dal DB tramite il suo ID.
     *
     * @param id ID dell'annuncio di lavoro.
     * @return ResponseEntity contenente l'annuncio di lavoro specificato.
     */
    @GetMapping("/view_lavori/retrieve/{id}")
    public ResponseEntity<Lavoro> getLavoroById(@PathVariable long id) {
        Lavoro lavoro = gestioneAnnuncioService.getLavori(id);
        System.out.println(lavoro);
        return ResponseEntity.ok(lavoro);
    }

    /**
     * Metodo per ottenere tutte gli annunci di lavoro di uno specifico volontario.
     *
     * @param proprietarioId ID del proprietario degli annunci di lavoro.
     * @return ResponseEntity contenente la lista degli annunci di lavoro del proprietario specificato.
     */
    @PostMapping("/view_lavori/proprietario/{id}")
    public ResponseEntity<List<Lavoro>> getLavoriByProprietario(@PathVariable("id") String proprietarioId) {
        Utente proprietario = volontarioDAO.findByEmail(proprietarioId);
        List<Lavoro> lavori = gestioneAnnuncioService.getLavoriByProprietario(proprietario);
        return ResponseEntity.ok(lavori);
    }

    @PostMapping("/modifica_lavoro/{id}")
    public ResponseEntity<?> modificaLavoro(@PathVariable long id,
                                           @RequestBody HashMap<String, Object> aggiornamenti,
                                           @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Estrai il token JWT dall'header Authorization
            String token = authorizationHeader.replace("Bearer ", "");
            //per estrarre l'email dal token
            String emailUtenteLoggato = jwtService.extractUsername(token);

            // Verifica se l'utente è il proprietario dell'annuncio
            Lavoro lavoroEsistente = gestioneAnnuncioService.getLavori(id);
            if (!lavoroEsistente.getProprietario().getEmail().equals(emailUtenteLoggato)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Non sei autorizzato a modificare questo annuncio di lavoro.");
            }

            // Invoca il metodo del servizio per modificare il lavoro
            Lavoro lavoroAggiornato = gestioneAnnuncioService.modificaAnnuncioLavoro(id, aggiornamenti);

            // Restituisce il lavoro aggiornato con codice HTTP 200
            return ResponseEntity.ok(lavoroAggiornato);

        } catch (IllegalArgumentException e) {
            // Gestisce errori come lavoro non trovato o campi non validi
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            // Gestisce errori generici
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'aggiornamento del lavoro: " + e.getMessage());
        }
    }

}
