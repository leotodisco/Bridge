package com.project.bridgebackend.GestioneUtente.Controller;

import com.project.bridgebackend.GestioneUtente.Service.UtenteService;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Utente;
import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import com.project.bridgebackend.Model.dto.UtenteDTO;
import com.project.bridgebackend.fotoProfilo.FotoProfilo;
import com.project.bridgebackend.fotoProfilo.FotoProfiloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
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
    @Autowired
    private UtenteService utenteService;

    @Autowired
    private FotoProfiloService fotoProfiloService;
    @DeleteMapping("/elimina/{email}")
    public ResponseEntity<String> eliminaUtente(@PathVariable("email") String email) {
        try {

            utenteService.eliminaUtente(email);
            return ResponseEntity.ok("Utente con email " + email + " eliminato con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'eliminazione dell'utente: " + e.getMessage());
        }
    }

    @GetMapping("/DatiUtente/{email}")
    public UtenteDTO retrieveDateUtente(@PathVariable("email") String email) {
        try{
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
            if(u instanceof FiguraSpecializzata){
                FiguraSpecializzata fs = (FiguraSpecializzata) u;
                dto.setDisponibilitaUtente(fs.getDisponibilita());
            }
            System.out.println(dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/DatiFotoUtente/{email}")
    public String retrieveFotoUtente(@PathVariable("email") String email) throws IOException {
        Utente u = utenteService.getUtente(email);
        FotoProfilo fp = utenteService.getFotoUtente(u.getEmail());
        if (fp != null && fp.getData() != null) {
            return Base64.getEncoder().encodeToString(fp.getData());
        } else {
            throw new IOException("Foto non trovata per l'utente: " + email);
        }
    }

    @PostMapping("/modificaFotoUtente/{email}")
    public ResponseEntity<String> editFotoUtente(
            @PathVariable("email") String email,
            @RequestParam("image") MultipartFile image) throws IOException {

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


    @PostMapping("modificaPassword/{email}")
    public ResponseEntity<String> editPassword(@RequestBody String password,
                                               @PathVariable String email) throws IOException{
        try {

            utenteService.modificaPassword(email,password);
            return ResponseEntity.ok("Password aggiornata con successo");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'aggiornamento dell'utente: " + e.getMessage());
        }
    }

    @PostMapping("/modificaUtente/{email}")
    public ResponseEntity<?> editUtente(@RequestBody HashMap<String, Object> aggiornamenti,
                                             @PathVariable String email
                                                ) throws IOException{
        try {
            System.out.println(aggiornamenti);
            Utente u = utenteService.modificaUtente(email,aggiornamenti);

            return ResponseEntity.ok(u);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'aggiornamento dell'utente: " + e.getMessage());
        }
    }
}
