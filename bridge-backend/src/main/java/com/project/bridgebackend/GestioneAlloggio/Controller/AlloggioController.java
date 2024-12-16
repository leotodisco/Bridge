package com.project.bridgebackend.GestioneAlloggio.Controller;

import com.project.bridgebackend.GestioneAlloggio.FotoAlloggio.FotoAlloggioService;
import com.project.bridgebackend.GestioneAlloggio.Service.AlloggioService;
import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
import com.project.bridgebackend.Model.dao.VolontarioDAO;
import com.project.bridgebackend.Model.dto.AlloggioDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Controller per la gestione degli alloggi
 */
@RestController
@RequestMapping("/alloggi")
public class AlloggioController {

    @Autowired
    private AlloggioService alloggioService;
    @Autowired
    private FotoAlloggioService fotoAlloggioService;

    private static final Logger logger = LoggerFactory.getLogger(AlloggioController.class);
    @Autowired
    private VolontarioDAO volontarioDAO;

    @Autowired
    private IndirizzoDAO indirizzoDAO;

    /**
     * Aggiungi un nuovo alloggio nel sistema
     *
     * @param alloggio   l'alloggio da aggiungere
     * @return ResponseEntity con lo stato dell'operazione
     */

    @PostMapping("/aggiungi")
    public ResponseEntity<String> addAlloggio(@RequestBody AlloggioDTO alloggio) {
        try {
            /**
             *Verifico prima se sia un volontoria, in caso positivo può aggiungere l'alloggio, in caso negativo non fa nulla
             */
            //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            //if (authentication != null && authentication.getAuthorities().stream()
            //.anyMatch(auth -> auth.getAuthority().equals("Volontario"))) {
            // Lista per salvare gli ID delle foto
            List<String> fotoIds = new ArrayList<>();
            String fotoProfiloId = null;
            // Cicliamo su tutte le foto inviate
            if (alloggio.getFotos() != null && !alloggio.getFotos().isEmpty()) {
                for (String foto : alloggio.getFotos()) {
                    if (foto.startsWith("data:image/jpeg;base64,")){
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

    @PostMapping("/assegnazione")
    public ResponseEntity<String> assegnazioneAlloggio(@RequestParam String titolo, @RequestParam String emailRifugiato) {
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

    @PostMapping("/manifestazione-interesse")
    public ResponseEntity<String> manifestazioneInteresse(@RequestBody Rifugiato rifugiato, @RequestBody Alloggio alloggio) {
        try {
            boolean risultato = alloggioService.manifestazioneInteresse(rifugiato, alloggio);
            if (risultato) {
                return new ResponseEntity<>("Manifestazione di interesse avvenuta con successo.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Errore durante la manifestazione di interesse.", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/mostra")
    public ResponseEntity<List<Alloggio>> getAllAlloggi() {
        logger.info("Richiesta ricevuta per mostrare gli alloggi");
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
     * Endpoint per ottenere i dettagli di un alloggio tramite il titolo
     * @param titolo titolo dell'alloggio da cercare
     * @return i dettagli dell'alloggio o un errore se non trovato
     */

    @GetMapping("/SingoloAlloggio/{titolo}")
    public Alloggio getAlloggioByTitolo(@PathVariable String titolo) {
        Alloggio alloggio = alloggioService.getAlloggioByTitolo(titolo);
        if (alloggio == null) {
            //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return null;
        }
        // return new ResponseEntity<>(alloggio, HttpStatus.OK);
        return alloggio;
    }
}