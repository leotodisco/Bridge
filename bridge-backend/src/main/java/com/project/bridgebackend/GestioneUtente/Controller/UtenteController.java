package com.project.bridgebackend.GestioneUtente.Controller;

import com.project.bridgebackend.GestioneUtente.Service.UtenteService;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Utente;
import com.project.bridgebackend.Model.dto.UtenteDTO;
import com.project.bridgebackend.fotoProfilo.FotoProfilo;
import com.project.bridgebackend.fotoProfilo.FotoProfiloService;
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
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

/**
 * @author Antonio Ceruso
 * Data creazione: 12/12/2024
 * Classe controller che implementa i metodi per l'area personale utente
 * */
@RestController
@CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
@RequestMapping(path = "areaPersonale")
public class UtenteController {

    /**
     * Service per la gestione degli utenti.
     */
    @Autowired
    private UtenteService utenteService;

    /**
     * Service per la gestione delle foto profilo.
     */
    @Autowired
    private FotoProfiloService fotoProfiloService;

    /**
     * Metodo per eliminare un utente specifico in base all'email.
     *
     * @param email L'email dell'utente da eliminare.
     * @return Una risposta HTTP che indica il risultato dell'operazione.
     */
    @DeleteMapping("/elimina/{email}")
    public ResponseEntity<String> eliminaUtente(
            @PathVariable("email") final String email) {
        try {

            utenteService.eliminaUtente(email);
            return ResponseEntity.ok("Utente con email " + email + " eliminato con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'eliminazione dell'utente: " + e.getMessage());
        }
    }

    /**
     * Metodo per recuperare i dati di un utente in formato DTO.
     *
     * @param email L'email dell'utente da recuperare.
     * @return Un oggetto {@link UtenteDTO} contenente i dati dell'utente.
     */
    @GetMapping("/DatiUtente/{email}")
    public UtenteDTO retrieveDateUtente(
            @PathVariable("email") final String email) {
        try {
            Utente u = utenteService.getUtente(email);
            UtenteDTO dto = new UtenteDTO();
            dto.setPasswordUtente(u.getPassword());
            dto.setCognomeUtente(u.getCognome());
            dto.setEmailUtente(u.getEmail());
            dto.setFotoUtente(u.getFotoProfilo());
            dto.setNomeUtente(u.getNome());
            dto.setNazionalitaUtente(u.getNazionalita());
            dto.setDataNascitaUtente(u.getDataNascita());
            dto.setGenderUtente(u.getGender());
            dto.setTitoloDiStudioUtente(u.getTitoloDiStudio());
            dto.setLingueParlateUtente(u.getLingueParlate());
            dto.setSkillUtente(u.getSkill());
            dto.setRuoloUtente(u.getRole());
            if (u instanceof FiguraSpecializzata) {
                FiguraSpecializzata fs = (FiguraSpecializzata) u;
                dto.setDisponibilitaUtente(fs.getDisponibilita());
            }
            System.out.println(dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo per recuperare la foto profilo di un utente in formato Base64.
     *
     * @param email L'email dell'utente di cui recuperare la foto.
     * @return La foto profilo codificata in Base64.
     * @throws IOException Se la foto non viene trovata o si verifica,
     * un errore di I/O.
     */
    @GetMapping("/DatiFotoUtente/{email}")
    public String retrieveFotoUtente(
            @PathVariable("email") final String email) throws IOException {
        Utente u = utenteService.getUtente(email);
        FotoProfilo fp = utenteService.getFotoUtente(u.getEmail());
        if (fp != null && fp.getData() != null) {
            return Base64.getEncoder().encodeToString(fp.getData());
        } else {
            throw new IOException("Foto non trovata per l'utente: " + email);
        }
    }

    /**
     * Metodo per modificare la foto profilo di un utente.
     *
     * @param email L'email dell'utente.
     * @param image L'immagine da aggiornare come foto profilo.
     * @return Una risposta HTTP che indica il risultato dell'operazione.
     * @throws IOException Se si verifica un errore durante l'elaborazione,
     * del file.
     */
    @PostMapping("/modificaFotoUtente/{email}")
    public ResponseEntity<String> editFotoUtente(
            @PathVariable("email") final String email,
            @RequestParam("image") final MultipartFile image) throws IOException {

        try {
            // Verifica se il file è stato ricevuto correttamente
            if (image.isEmpty()) {
                return ResponseEntity.badRequest().body("Nessuna immagine ricevuta");
            }

            System.out.println("File ricevuto: " + image.getOriginalFilename());
            byte[] bytes = image.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            // Chiamata al servizio per modificare la foto
            utenteService.modificaFotoUtente(email, base64Image);

            return ResponseEntity.ok("Foto profilo aggiornata con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'aggiornamento della foto profilo: " + e.getMessage());
        }
    }


    /**
     * Metodo per modificare la password di un utente.
     *
     * @param password La nuova password da impostare.
     * @param email L'email dell'utente.
     * @return Una risposta HTTP che indica il risultato dell'operazione.
     * @throws IOException Se si verifica un errore durante l'aggiornamento,
     * della password.
     */
    @PostMapping("modificaPassword/{email}")
    public ResponseEntity<String> editPassword(
            @RequestBody final String password,
            @PathVariable final String email) throws IOException {
        System.out.println("yyy");
        try {
            utenteService.modificaPassword(email, password);
            return ResponseEntity.ok("Password aggiornata con successo");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'aggiornamento dell'utente: " + e.getMessage());
        }
    }

    /**
     * Metodo per modificare i dati di un utente.
     *
     * @param aggiornamenti Una mappa con i dati da aggiornare.
     * @param email L'email dell'utente.
     * @return L'oggetto {@link Utente} aggiornato.
     * @throws IOException Se si verifica un errore durante l'aggiornamento.
     */

    @PostMapping("/modificaUtente/{email}")
    public ResponseEntity<?> editUtente(
            @RequestBody final HashMap<String, Object> aggiornamenti,
            @PathVariable final String email) throws IOException {
        try {
            // Recupera l'utente dal servizio
            Utente u = utenteService.getUtente(email);

            if (u instanceof FiguraSpecializzata) {
                // Se l'utente è una FiguraSpecializzata, aggiorna la disponibilità
                if (aggiornamenti.containsKey("disponibilitaUtente")) {
                    String disponibilita = (String) aggiornamenti.get("disponibilitaUtente");
                    FiguraSpecializzata fs = (FiguraSpecializzata) u;
                    fs.setDisponibilita(disponibilita);  // Aggiorna la disponibilità solo per FiguraSpecializzata
                    aggiornamenti.remove("disponibilitaUtente");  // Rimuovi la disponibilità dalla mappa
                }
            } else {
                // Se l'utente non è una FiguraSpecializzata, non aggiorniamo la disponibilità
                aggiornamenti.remove("disponibilitaUtente");
            }

            // Aggiorna i restanti dati dell'utente
            Utente updatedUser = utenteService.modificaUtente(email, aggiornamenti);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'aggiornamento dell'utente: " + e.getMessage());
        }
    }

}
