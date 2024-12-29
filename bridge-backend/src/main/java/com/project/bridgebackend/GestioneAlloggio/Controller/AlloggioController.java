package com.project.bridgebackend.GestioneAlloggio.Controller;

import com.project.bridgebackend.GestioneAlloggio.FotoAlloggio.FotoAlloggio;
import com.project.bridgebackend.GestioneAlloggio.FotoAlloggio.FotoAlloggioService;
import com.project.bridgebackend.GestioneAlloggio.Service.AlloggioService;
import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.dao.VolontarioDAO;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
import com.project.bridgebackend.Model.dao.UtenteDAO;
import com.project.bridgebackend.Model.dao.AlloggioDAO;
import com.project.bridgebackend.Model.dao.RifugiatoDAO;
import com.project.bridgebackend.Model.dto.AlloggioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.io.IOException;
import java.util.*;


/**
 * Controller per la gestione degli alloggi.
 */
@RestController
@RequestMapping("/alloggi")
public class AlloggioController {

    /**
     * Service per la logica di gestione degli alloggi.
     */
    @Autowired
    private AlloggioService alloggioService;

    /**
     * Service per la logica di gestione delle foto alloggio.
     */
    @Autowired
    private FotoAlloggioService fotoAlloggioService;

    /**
     * Service per la logica di gestione degli utenti volontari.
     */
    @Autowired
    private VolontarioDAO volontarioDAO;

    /**
     * Service per la logica di gestione degli indirizzi.
     */
    @Autowired
    private IndirizzoDAO indirizzoDAO;

    /**
     * Service per la logica di gestione degli utenti.
     */
    @Autowired
    private UtenteDAO utenteDAO;

    /**
     * DAO per la gestione degli annunci di alloggio.
     */
    @Autowired
    private AlloggioDAO alloggioDAO;

    /**
     * DAO per la gestione degli utenti rifugiati.
     */
    @Autowired
    private RifugiatoDAO rifugiatoDAO;


    /**
     * Metodo per rimuovere interesse per un alloggio.
     * @param emailRifugiato email del rifugiato che ha rimosso l'interesse.
     * @param idAlloggio identificativo dell'alloggio a cui è stato rimosso l'interesse.
     * @return ResponseEntity con lo stato dell'operazione.
     */
    @PostMapping("/rimuoviInteresse")
    public ResponseEntity<String> rimuoviInteresse(@RequestParam String emailRifugiato, @RequestParam long idAlloggio) {
        try {
            Alloggio alloggio = alloggioDAO.findAlloggioById(idAlloggio);
            if (alloggio == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alloggio non trovato.");
            }

            Rifugiato rifugiato = rifugiatoDAO.findByEmail(emailRifugiato);
            if (rifugiato == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rifugiato non trovato.");
            }

            if (alloggio.getListaCandidati() == null || !alloggio.getListaCandidati().contains(rifugiato)) {
                return ResponseEntity.ok("Interesse non trovato per questo alloggio.");
            }

            // Rimuovi il rifugiato dalla lista dei candidati
            alloggio.getListaCandidati().remove(rifugiato);
            alloggioDAO.save(alloggio);

            return ResponseEntity.ok("Interesse rimosso con successo.");
        } catch (Exception e) {
            System.out.println("Errore interno del server: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno al server.");
        }
    }


    /**
     * Aggiungi un nuovo alloggio nel sistema.
     *
     * @param alloggio   l'alloggio da aggiungere.
     * @return ResponseEntity con lo stato dell'operazione.
     */

    @PostMapping("/aggiungi")
    public ResponseEntity<String> addAlloggio(
            @RequestBody final AlloggioDTO alloggio) {
        try {
            //Verifico prima se sia un volontario,
            // in caso positivo può aggiungere l'alloggio, in caso negativo non fa nulla

            //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            //if (authentication != null && authentication.getAuthorities().stream()
            //.anyMatch(auth -> auth.getAuthority().equals("Volontario"))) {
            // Lista per salvare gli ID delle foto
            List<String> fotoIds = new ArrayList<>();
            String fotoProfiloId = null;
            // Cicliamo su tutte le foto inviate
            if (alloggio.getFoto() != null && !alloggio.getFoto().isEmpty()) {
                for (String foto : alloggio.getFoto()) {
                    if (foto.startsWith("data:image/jpeg;base64,")) {
                        foto = foto.split(",")[1]; // Estrai solo la parte Base64 dopo la virgola
                        byte[] fotoData = Base64.getDecoder().decode(foto);
                        fotoProfiloId = fotoAlloggioService.saveIMG(foto, fotoData);
                    }
                    fotoIds.add(fotoProfiloId); // Aggiungiamo l'ID alla lista
                }
            }

            //indirizzo
            Indirizzo indirizzo = new Indirizzo();
            indirizzo.setCitta(alloggio.getIndirizzo().getCitta());
            indirizzo.setCap(alloggio.getIndirizzo().getCap());
            indirizzo.setVia(alloggio.getIndirizzo().getVia());
            indirizzo.setNumCivico(alloggio.getIndirizzo().getNumCivico());
            indirizzo.setProvincia(alloggio.getIndirizzo().getProvincia());

            long checkIndirizzo = alloggioService.salvaIndirizzoAlloggio(indirizzo);

            /*if (checkIndirizzo) {
                throw new RuntimeException("Indirizzo non trovato");
            }*/

            Volontario proprietario = volontarioDAO.findByEmail(alloggio.getEmailProprietario());
            if (proprietario == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }

            //alloggio
            Alloggio newalloggio = new Alloggio();
            newalloggio.setDescrizione(alloggio.getDescrizione());
            newalloggio.setMaxPersone(alloggio.getMaxPersone());
            newalloggio.setFoto(fotoIds);
            newalloggio.setProprietario(proprietario);
            newalloggio.setMetratura(alloggio.getMetratura());
            newalloggio.setServizi(alloggio.getServizi());
            newalloggio.setTitolo(alloggio.getTitolo());
            newalloggio.setIndirizzo(indirizzoDAO.getReferenceById(checkIndirizzo));
            System.out.println("alLOGGIO CREATO: " + newalloggio);


            // Rimuoviamo le foto dalla DTO, non ci servono più per il salvataggio dell'alloggio
            Alloggio createdAlloggio = alloggioService.addAlloggio(newalloggio);
            if (createdAlloggio != null) {
                return new ResponseEntity<>("Alloggio aggiunto con successo", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Errore nell'aggiunta dell'alloggio", HttpStatus.BAD_REQUEST);
            }
            //} //else {
            // return new ResponseEntity<>("Accesso negato: Solo i volontari possono aggiungere alloggi", HttpStatus.FORBIDDEN);
            //}
        } catch (Exception e) {
            return new ResponseEntity<>("Errore interno del server: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Assegna un alloggio a un rifugiato specifico.
     *
     * @param idAlloggio l'id dell'alloggio
     * @param emailRifugiato l'email del rifugiato
     * @return ResponseEntity con lo stato dell'operazione
     */
    @PostMapping("/assegnazione/{idAlloggio}")
    public ResponseEntity<?> assegnazioneAlloggio(@PathVariable long idAlloggio, @RequestParam String emailRifugiato) {
        try {
            if (idAlloggio < 1 || emailRifugiato == null || emailRifugiato.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dati mancanti.");
            }
            Alloggio a = alloggioService.assegnazioneAlloggio(idAlloggio, emailRifugiato);
            return ResponseEntity.ok(a); // Restituisci l'alloggio aggiornato
        } catch (IllegalArgumentException e) {
            // Se la lista dei rifugiati è vuota, restituire un errore 404 con un messaggio chiaro
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nessun rifugiato disponibile per l'assegnazione.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore durante l'assegnazione dell'alloggio.");
        }
    }


    /** Metodo che permette ad un rifugiato di manifesare interesse per un'alloggio
     * @param emailRifugiato l'email del rifugiato
     * @param idAlloggio l'id dell'alloggio al quale si manifesta interesse
     *
     * @return ResponseEntity con lo stato dell'operazione
     */

    @PostMapping("/interesse")
    public ResponseEntity<String> manifestaInteresse(@RequestParam String emailRifugiato, @RequestParam long idAlloggio) {
        try {
            Alloggio alloggio = alloggioDAO.findAlloggioById(idAlloggio);
            if (alloggio == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alloggio non trovato.");
            }

            // Aggiungi il rifugiato alla lista dei candidati
            Rifugiato rifugiato = rifugiatoDAO.findByEmail(emailRifugiato);
            if (rifugiato == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rifugiato non trovato.");
            }

            // Se la lista candidati è null o vuota, inizializzala
            if (alloggio.getListaCandidati() == null) {
                alloggio.setListaCandidati(new ArrayList<>());
            }

            // Verifica se il rifugiato ha già manifestato interesse
            if (alloggio.getListaCandidati().contains(rifugiato)) {
                return ResponseEntity.ok("Hai già manifestato interesse per questo alloggio");
            }

            // Aggiungi il rifugiato alla lista dei candidati
            alloggio.getListaCandidati().add(rifugiato);

            alloggioService.sendEmailVolontario("Hai ricevuto una nuova manifestazione", "mariozurolo00@gmail.com");
            alloggioDAO.save(alloggio); // Salva l'alloggio aggiornato nel database

            return ResponseEntity.ok("Interesse manifestato con successo.");
        } catch (Exception e) {
            System.out.println("Errore interno del server: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno al server.");
        }
    }

    /**
     * Verifica se un rifugiato ha aggiunto un alloggio ai preferiti.
     *
     * @param email  l'email del rifugiato
     * @param idAlloggio l'id dell'alloggio
     * @return ResponseEntity con valore booleano che indica se è favorito
     */
    @GetMapping("/isFavorito")
    public ResponseEntity<Boolean> isFavorito(@RequestParam String email,
                                              @RequestParam long idAlloggio) {
        try {
            // Verifica se l'alloggio esiste
            Alloggio alloggio = alloggioDAO.findAlloggioById(idAlloggio);
            if (alloggio == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false); // Alloggio non trovato
            }

            // Verifica se l'email del rifugiato è presente nella lista dei candidati
            boolean isFavorito = alloggio.getListaCandidati().stream()
                    .anyMatch(candidato -> candidato.getEmail().equals(email));

            // Restituisce true se l'alloggio è nei preferiti, altrimenti false
            return ResponseEntity.ok(isFavorito);
        } catch (Exception e) {
            // Gestisce eventuali eccezioni generiche
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false);
        }
    }


    /**
     * Recupera tutti gli alloggi disponibili.
     *
     * @return ResponseEntity contenente la lista degli alloggi
     */
    @GetMapping("/mostra")
    public ResponseEntity<List<Alloggio>> getAllAlloggi() {
        try {
            // Otteniamo tutti gli alloggi dal servizio
            List<Alloggio> alloggi = alloggioService.getAllAlloggio();

            // Restituiamo la lista con lo stato OK
            System.out.println(ResponseEntity.ok(alloggi.size()));
            return new ResponseEntity<>(alloggi, HttpStatus.OK);
        } catch (Exception e) {
            // In caso di errore, restituiamo uno stato di errore con il messaggio
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Ottiene i dettagli di un alloggio specifico tramite il titolo.
     *
     * @param titolo il titolo dell'alloggio da cercare
     * @return dettagli dell'alloggio o null se non trovato
     * @throws IOException se si verifica un errore durante il recupero delle immagini
     */
    @GetMapping("/SingoloAlloggio/{titolo}")
    public Alloggio getAlloggioByTitolo(@PathVariable final String titolo) throws IOException {
        Alloggio alloggio = alloggioService.getAlloggioByTitolo(titolo);
        if (alloggio == null) {
            return null;
        }

        List<String> fotoBase64 = new ArrayList<>();
        if (alloggio.getFoto() != null) {
            for (String fotoId : alloggio.getFoto()) {
                FotoAlloggio fotoAlloggio = fotoAlloggioService.getIMG(fotoId);
                if (fotoAlloggio != null) {
                    // Converti l'immagine in base64
                    String base64Image = Base64.getEncoder().encodeToString(fotoAlloggio.getData());
                    fotoBase64.add(base64Image);
                }
            }
        }

        alloggio.setFoto(fotoBase64);
        return alloggio;
    }

    @GetMapping("/alloggiByEmail/{email}")
    public ResponseEntity<List<Alloggio>> getAlloggiByEmail(@PathVariable String email) {
        try {
            List<Alloggio> alloggi = alloggioService.getAllAlloggiByEmail(email);

            if (alloggi.isEmpty() || alloggi == null) {
                System.out.println("Nessun alloggio trovato");
                return ResponseEntity.ok(Collections.emptyList());
            }

            return ResponseEntity.ok(alloggi);
        } catch (IllegalArgumentException e) {
            // Specifica l'errore per alloggi non trovati
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            // Gestisce altri tipi di errori generali
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/interessati/{idAlloggio}")
    public ResponseEntity<List<Rifugiato>> getInteressati(@PathVariable long idAlloggio) {
        try {
            List<Rifugiato> interessati = alloggioService.getInteressati(idAlloggio);
            return ResponseEntity.ok(interessati);
        } catch (Exception e) {
            // Log dell'errore lato server
            System.err.println("Errore durante il recupero dei rifugiati interessati: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping("/alloggiPreferitiUtente/{email}")
    public List<Alloggio> getAllAlloggiByRifugiatoEmail(@PathVariable String email) {
        return alloggioService.getAllAlloggiByRifugiatoEmail(email);
    }

}
