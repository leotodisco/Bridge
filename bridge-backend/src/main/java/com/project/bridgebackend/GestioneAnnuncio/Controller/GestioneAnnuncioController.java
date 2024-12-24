package com.project.bridgebackend.GestioneAnnuncio.Controller;

import com.project.bridgebackend.GestioneAnnuncio.Service.GestioneAnnuncioService;
import com.project.bridgebackend.GestioneCorso.Controller.GestioneCorsoController;
import com.project.bridgebackend.Model.Entity.Consulenza;
import com.project.bridgebackend.Model.Entity.Lavoro;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Utente;
import com.project.bridgebackend.Model.dao.ConsulenzaDAO;
import com.project.bridgebackend.Model.dao.FiguraSpecializzataDAO;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
import com.project.bridgebackend.Model.dao.VolontarioDAO;
import com.project.bridgebackend.Model.dto.ConsulenzaDTO;
import com.project.bridgebackend.Model.dto.LavoroDTO;
import com.project.bridgebackend.util.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
public class GestioneAnnuncioController {

    /**
    * logger per la stampa di warning o errori.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GestioneCorsoController.class);


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

    /**
     * Servizi per l'estrazione delle informazioni dal token.
     */
    @Autowired
    private JwtService jwtService;

    /**
     * DAO per accedere ai dati delle consulenze.
     */
    @Autowired
    private ConsulenzaDAO consulenzaDAO;


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
     * Metodo per ottenere una consulenza specifica dal DB.
     * @param id della consulenza che si vuole visualizzare.
     * @return ResponseEntity contenente la consulenza.
     */
    @GetMapping("/view_consulenze/retrive/{id}")
    public ResponseEntity<Consulenza> getConsulenzaById(
            @PathVariable final long id) {
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
    public ResponseEntity<List<Consulenza>> getConsulenzeByProprietario(
            @PathVariable("id") final String proprietarioId) {
        // Retrieve the FiguraSpecializzata (which is a subtype of Utente)
        Utente proprietario = figuraSpecializzataDAO.findByEmail(proprietarioId);

        // Retrieve all Consulenza entities for the specific proprietario
        List<Consulenza> consulenze = gestioneAnnuncioService.getConsulenzeByProprietario(proprietario);

        return ResponseEntity.ok(consulenze);
    }
    /**
     * Metodo per attuare modifiche su una specifica consulenza.
     * @param idConsulenza è l'id della modifica che si vuole effettuare.
     * @param aggiornamenti è un hashmap di stinga oggetto per il,
     *                      passaggio delle informazioni modificate,
     *                      nella consulenza e degli oggetti,
     *                      relativamente coinvolti (es. indirizzo).
     * @param authorizationHeader Stringa idi autorizzazione,
     *                            in modo da poter verificare,
     *                            se l'utente attualmente loggato,
     *                            può effettivamente attuare modifiche.
     *
     * @return ResponseEntity contenente la consulenza modificata
     */

    @PostMapping("/modifica_consulenza/{idConsulenza}")
    public ResponseEntity<?> modificaConsulenza(@PathVariable final long idConsulenza,
                                                @RequestBody final HashMap<String, Object> aggiornamenti,
                                                @RequestHeader("Authorization") final String authorizationHeader) {
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

    @PostMapping("/manifestazione-interesse/{idConsulenza}")
    public ResponseEntity<?> manifestaInteresse(
            @PathVariable final long idConsulenza,
            @RequestHeader("Authorization") final String authorizationHeader) {
        // Estrai il token JWT dall'header Authorization
        String token = authorizationHeader.replace("Bearer ", "");
        //per estrarre l'email dal token
        String emailUtenteLoggato = jwtService.extractUsername(token);
        if (emailUtenteLoggato == null) {
            return ResponseEntity.badRequest().body("Token non valido o email non trovata.");
        }

        try {
            gestioneAnnuncioService.interesseConsulenza(idConsulenza, emailUtenteLoggato);
            return ResponseEntity.ok("Interesse manifestato con successo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verifica-candidato/{idConsulenza}")
    public ResponseEntity<?> checkInteresse(
            @PathVariable final long idConsulenza,
            @RequestHeader("Authorization") String token) {
        try {
            // Verifica che il token non sia nullo o vuoto
            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Token non fornito o vuoto.");
            }

            // Recupera la consulenza tramite DAO
            Consulenza c = consulenzaDAO.findConsulenzaById(idConsulenza);
            if (c == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consulenza non trovata.");
            }

            // Estrae l'email dell'utente dal token
            String emailUtenteLoggato = jwtService.extractUsername(token);
            if (emailUtenteLoggato == null || emailUtenteLoggato.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Token non valido o email non trovata.");
            }

            // Verifica se l'utente è candidato per questa consulenza
            boolean isFavorito = c.getCandidati().contains(emailUtenteLoggato);

            // Restituisce lo stato
            return ResponseEntity.ok(isFavorito);
        } catch (IllegalArgumentException e) {
            // Specifica errore legato ad argomenti non validi
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Errore nei parametri forniti: " + e.getMessage());
        } catch (Exception e) {
            // Gestisce errori generici
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'elaborazione della richiesta: " + e.getMessage());
        }
    }

    /**
     * Metodo per attuare modifiche su una specifica consulenza.
     * @param id è l'id della consulenza che si vuole eliminare.
     *
     * @param authorizationHeader Stringa di autorizzazione,
     *                            in modo da poter verificare,
     *                            se l'utente attualmente loggato,
     *                            può effettivamente attuare modifiche.
     *
     * @return ResponseEntity contenente la stringa se l'operazione,
     * è andata a buon fine o meno.
     */
    @DeleteMapping("/eliminaConsulenza/{id}")
    public ResponseEntity<String> eliminaConsulenza(@PathVariable final Long id,
                                                    @RequestHeader("Authorization") final String authorizationHeader) {
        try {
            // Estrai il token JWT dall'header Authorization
            String token = authorizationHeader.replace("Bearer ", "");
            // Estrai l'email dell'utente loggato dal token
            String emailUtenteLoggato = jwtService.extractUsername(token);

            // Recupera la consulenza esistente
            Consulenza consulenzaEsistente = gestioneAnnuncioService.getConsulenze(id);

            // Verifica che l'utente loggato sia il proprietario della consulenza
            if (!consulenzaEsistente.getProprietario().getEmail().equals(emailUtenteLoggato)) {
                // Se l'utente non è il proprietario, restituisci un errore 403
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Non sei autorizzato a eliminare questa consulenza.");
            }

            // Se l'utente è il proprietario, procedi con l'eliminazione
            gestioneAnnuncioService.eliminaConsulenza(id);

            return ResponseEntity.ok("Consulenza eliminata con successo.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'eliminazione della consulenza: " + e.getMessage());
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
    public ResponseEntity<Lavoro> getLavoroById(@PathVariable final long id) {
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
    public ResponseEntity<List<Lavoro>> getLavoriByProprietario(
            @PathVariable("id") final String proprietarioId) {
        Utente proprietario = volontarioDAO.findByEmail(proprietarioId);
        List<Lavoro> lavori = gestioneAnnuncioService.getLavoriByProprietario(proprietario);
        return ResponseEntity.ok(lavori);
    }

    /**
     * Metodo per la modifica di un annuncio di lavoro esistente.
     * Questo metodo consente di aggiornare i dettagli di un,
     * annuncio di lavoro specificato,
     * tramite l'ID, utilizzando una mappa di aggiornamenti e un token,
     * di autorizzazione.
     *
     * @param id L'ID dell'annuncio di lavoro da modificare.
     * @param aggiornamenti La mappa contenente i dati da aggiornare,
     *                      nell'annuncio di lavoro.
     *                      Le chiavi della mappa corrispondono ai campi,
     *                      dell'annuncio di lavoro,
     *                      e i valori sono i nuovi dati da applicare.
     * @param authorizationHeader L'intestazione di autorizzazione che contiene,
     *                           il token JWT per verificare l'identità dell'utente.
     *
     * @return Una risposta HTTP che può essere:
     *         - 200 OK se l'annuncio di lavoro è stato modificato con successo,
     *         - 400 BAD REQUEST se la mappa degli aggiornamenti è nulla o vuota,
     *         - 403 FORBIDDEN se l'utente non è autorizzato a modificare l'annuncio di lavoro,
     *         - 500 INTERNAL SERVER ERROR in caso di errore generico durante l'operazione.
     */
    @PostMapping("/modifica_lavoro/{id}")
    public ResponseEntity<?> modificaLavoro(@PathVariable final long id,
                                            @RequestBody final HashMap<String, Object> aggiornamenti,
                                            @RequestHeader("Authorization") final String authorizationHeader) {
        try {
            LOGGER.info("Richiesta di modifica per l'annuncio ID: {}", id);
            LOGGER.info("Dati aggiornamenti: {}", aggiornamenti);

            if (aggiornamenti == null || aggiornamenti.isEmpty()) {
                LOGGER.error("La mappa degli aggiornamenti è vuota o nulla.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nessun dato fornito per la modifica.");
            }

            String token = authorizationHeader.replace("Bearer ", "");
            String emailUtenteLoggato = jwtService.extractUsername(token);

            Lavoro lavoroEsistente = gestioneAnnuncioService.getLavori(id);
            if (!isAuthorized(emailUtenteLoggato, lavoroEsistente)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Non sei autorizzato a modificare questo annuncio di lavoro.");
            }

            Lavoro lavoroAggiornato = gestioneAnnuncioService.modificaAnnuncioLavoro(id, aggiornamenti);

            return ResponseEntity.ok(lavoroAggiornato);

        } catch (IllegalArgumentException e) {
            LOGGER.error("Errore durante la modifica dell'annuncio: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Errore generico durante la modifica: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'aggiornamento del lavoro: " + e.getMessage());
        }
    }

    /**
     * Metodo per eliminare un annuncio di lavoro esistente.
     * Questo metodo permette di eliminare un annuncio di lavoro specificato tramite l'ID,
     * previa verifica dell'autorizzazione dell'utente tramite token JWT.
     *
     * @param id L'ID dell'annuncio di lavoro da eliminare.
     * @param authorizationHeader L'intestazione di autorizzazione che contiene il token JWT
     *                            per verificare l'identità dell'utente.
     *
     * @return Una risposta HTTP che può essere:
     *         - 200 OK con un messaggio di successo se l'annuncio di lavoro è stato eliminato con successo,
     *         - 403 FORBIDDEN se l'utente non è autorizzato a eliminare l'annuncio di lavoro,
     *         - 400 BAD REQUEST se si verifica un errore legato ai dati forniti,
     *         - 500 INTERNAL SERVER ERROR in caso di errore generico durante l'operazione.
     */

    @DeleteMapping("/elimina_lavoro/{id}")
    public ResponseEntity<?> eliminaLavoro(@PathVariable final long id,
                                           @RequestHeader("Authorization") final String authorizationHeader) {
        try {
            LOGGER.info("Richiesta di eliminazione per l'annuncio ID: {}", id);

            String token = authorizationHeader.replace("Bearer ", "");
            String emailUtenteLoggato = jwtService.extractUsername(token);

            Lavoro lavoroEsistente = gestioneAnnuncioService.getLavori(id);
            if (!isAuthorized(emailUtenteLoggato, lavoroEsistente)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Non sei autorizzato a eliminare questo annuncio di lavoro.");
            }

            gestioneAnnuncioService.eliminaAnnuncioLavoro(id);
            //gestioneAnnuncioService.eliminaLavoro(id);
            return ResponseEntity.ok("Annuncio di lavoro eliminato con successo.");

        } catch (IllegalArgumentException e) {
            LOGGER.error("Errore durante l'eliminazione dell'annuncio: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Errore generico durante l'eliminazione: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'eliminazione del lavoro: " + e.getMessage());
        }
    }
    /**
     * Verifica se l'utente loggato è autorizzato a modificare o eliminare un annuncio di lavoro.
     * L'autorizzazione è basata sul fatto che l'utente loggato (identificato tramite email)
     * deve essere il proprietario dell'annuncio di lavoro specificato.
     *
     * @param emailUtenteLoggato L'email dell'utente attualmente loggato nel sistema, estratta dal token JWT.
     * @param lavoroEsistente L'annuncio di lavoro da verificare per l'autorizzazione.
     *
     * @return {@code true} se l'utente loggato è il proprietario dell'annuncio di lavoro,
     *         {@code false} se l'utente non è autorizzato ad accedere all'annuncio.
     */

    private boolean isAuthorized(final String emailUtenteLoggato,
                                 final Lavoro lavoroEsistente) {
        return lavoroEsistente.getProprietario().getEmail().equalsIgnoreCase(emailUtenteLoggato);
    }


    /**
     * Metodo per ottenere una lista di cinque lavori casuali dal database.
     * Questo metodo è utile per visualizzare lavori casuali nella homepage dell'applicazione.
     *
     * @return ResponseEntity contenente la lista di lavori casuali.
     */

    @GetMapping("/random")
    public ResponseEntity<List<Lavoro>> getRandomJobs() {
        List<Lavoro> lavori = gestioneAnnuncioService.getRandomLavori();
        return new ResponseEntity<>(lavori, HttpStatus.OK);
    }


}
