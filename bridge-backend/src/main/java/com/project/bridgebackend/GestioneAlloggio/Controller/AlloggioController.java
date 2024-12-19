package com.project.bridgebackend.GestioneAlloggio.Controller;

import com.project.bridgebackend.GestioneAlloggio.FotoAlloggio.FotoAlloggio;
import com.project.bridgebackend.GestioneAlloggio.FotoAlloggio.FotoAlloggioService;
import com.project.bridgebackend.GestioneAlloggio.Service.AlloggioService;
import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.dao.AlloggioDAO;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
import com.project.bridgebackend.Model.dao.UtenteDAO;
import com.project.bridgebackend.Model.dao.VolontarioDAO;
import com.project.bridgebackend.Model.dto.AlloggioDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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

    @Autowired
    private VolontarioDAO volontarioDAO;

    @Autowired
    private IndirizzoDAO indirizzoDAO;
    @Autowired
    private UtenteDAO utenteDAO;
    @Autowired
    private AlloggioDAO alloggioDAO;

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
            if (alloggio.getFoto() != null && !alloggio.getFoto().isEmpty()) {
                for (String foto : alloggio.getFoto()) {
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


    @PostMapping("/preferiti")
    public ResponseEntity<String> manifestazioneInteresse(@RequestBody Map<String, String> request) {
        // Recupera l'email del rifugiato dal token JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailRifugiato = authentication.getName(); // Email utente autenticato

        // Ottieni il titolo dell'alloggio dal corpo della richiesta
        String titoloAlloggio = request.get("titoloAlloggio");

        try {
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



    @GetMapping("/isFavorito")
    public ResponseEntity<Boolean> isFavorito(@RequestParam String email, @RequestParam String titolo) {
        Rifugiato rifugiato = (Rifugiato) utenteDAO.findByEmail(email);
        if (rifugiato == null) {
            throw new IllegalArgumentException("Rifugiato non trovato con l'email specificata.");
        }

        Optional<Alloggio> alloggioOptional = alloggioDAO.findByTitolo(titolo);
        if (alloggioOptional.isEmpty()) {
            throw new IllegalArgumentException("Alloggio non trovato con il titolo specificato.");
        }

        Alloggio alloggio = alloggioOptional.get();
        boolean isFavorito = alloggio.getListaCandidati().contains(rifugiato);

        return ResponseEntity.ok(isFavorito);
    }



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
     * Endpoint per ottenere i dettagli di un alloggio tramite il titolo
     * @param titolo titolo dell'alloggio da cercare
     * @return i dettagli dell'alloggio o un errore se non trovato
     */

    @GetMapping("/SingoloAlloggio/{titolo}")
    public Alloggio getAlloggioByTitolo(@PathVariable String titolo) throws IOException {
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