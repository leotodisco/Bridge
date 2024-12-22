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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;


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
     * @param titolo         il titolo dell'alloggio
     * @param emailRifugiato l'email del rifugiato
     * @return ResponseEntity con lo stato dell'operazione
     */
    @PostMapping("/assegnazione")
    public ResponseEntity<String> assegnazioneAlloggio(
            @RequestParam final String titolo,
            @RequestParam final String emailRifugiato) {
        try {
            // Recuperiamo l'alloggio in base al titolo
            Alloggio alloggio = alloggioService.assegnazioneAlloggio(titolo, emailRifugiato);

            // Verifica che il rifugiato scelto faccia parte della lista dei candidati
            List<Rifugiato> candidati = alloggio.getListaCandidati();
            boolean rifugiatoScelto = candidati.stream().anyMatch(r -> r.getEmail().equals(emailRifugiato));

            if (!rifugiatoScelto) {
                return new ResponseEntity<>("Il rifugiato scelto non ha manifestato interesse per questo alloggio.", HttpStatus.BAD_REQUEST);
            }

            // Se il rifugiato ha manifestato interesse, viene assegnato l'alloggio
            alloggio.getListaCandidati().clear();  // Rimuoviamo gli altri candidati
            alloggio.getListaCandidati().add(candidati.stream().filter(r -> r.getEmail().equals(emailRifugiato)).findFirst().get()); // Aggiungiamo solo il rifugiato scelto

            alloggioService.addAlloggio(alloggio); // Salviamo l'alloggio aggiornato
            return new ResponseEntity<>("Alloggio assegnato correttamente a " + emailRifugiato, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Permette a un rifugiato di manifestare interesse per un alloggio.
     *
     * @param request mappa contenente email del rifugiato e titolo dell'alloggio
     * @return ResponseEntity con lo stato dell'operazione
     */
    @PostMapping("/preferiti")
    public ResponseEntity<String> manifestazioneInteresse(
            @RequestBody final Map<String, String> request) {
        try {
            String emailRifugiato = request.get("emailRifugiato");
            String titoloAlloggio = request.get("titoloAlloggio");

            if (emailRifugiato == null) {
                return ResponseEntity.badRequest().body("email mancata");
            }
            if (titoloAlloggio == null) {
                return ResponseEntity.badRequest().body("Titolo mancanto");
            }

            boolean successo = alloggioService.manifestazioneInteresse(emailRifugiato, titoloAlloggio);
            if (successo) {
                return ResponseEntity.ok("Manifestazione di interesse avvenuta con successo.");
            } else {
                return ResponseEntity.badRequest().body("Il rifugiato ha già manifestato interesse per questo alloggio.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Si è verificato un errore imprevisto.");
        }
    }

    /**
     * Verifica se un rifugiato ha aggiunto un alloggio ai preferiti.
     *
     * @param email  l'email del rifugiato
     * @param titolo il titolo dell'alloggio
     * @return ResponseEntity con valore booleano che indica se è favorito
     */
    @GetMapping("/isFavorito")
    public ResponseEntity<Boolean> isFavorito(
            @RequestParam final String email,
            @RequestParam final String titolo) {
        try {
            Rifugiato rifugiato = rifugiatoDAO.findByEmail(email);
            if (rifugiato == null) {
                throw new IllegalArgumentException("Rifugiato non trovato con l'email specificata.");
            }
            System.out.println("Rifugiato trovato: " + rifugiato);

            Optional<Alloggio> alloggioOptional = alloggioDAO.findByTitolo(titolo);
            if (alloggioOptional.isEmpty()) {
                throw new IllegalArgumentException("Alloggio non trovato con il titolo specificato.");
            }
            Alloggio alloggio = alloggioOptional.get();
            System.out.println("Alloggio trovato: " + alloggio);

            boolean isFavorito = alloggio.getListaCandidati().contains(rifugiato);
            System.out.println("Il rifugiato è favorito: " + isFavorito);

            return ResponseEntity.ok(isFavorito);
        } catch (IllegalArgumentException e) {
            System.err.println("Errore: " + e.getMessage());
            return ResponseEntity.badRequest().body(false);
        } catch (Exception e) {
            System.err.println("Errore imprevisto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
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
}
