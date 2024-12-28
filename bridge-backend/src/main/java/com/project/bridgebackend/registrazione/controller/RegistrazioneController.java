package com.project.bridgebackend.registrazione.controller;

import com.project.bridgebackend.Model.Entity.Admin;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import com.project.bridgebackend.Model.dto.UtenteDTO;
import com.project.bridgebackend.fotoProfilo.FotoProfiloService;
import com.project.bridgebackend.registrazione.service.RegistrazioneService;
import com.project.bridgebackend.util.AuthenticationRequest;
import com.project.bridgebackend.util.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.Base64;

/**
 * @author Antonio Ceruso.
 * Data creazione: 05/12/2024.
 * Classe controller che implementa i metodi per la registrazione degli utenti.
 * */
@RestController
@CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*")
@RequestMapping(path = "authentication")
public class RegistrazioneController {
    /**
     * Variabile che si riferisce al service di registrazione.
     * */
    @Autowired
    private RegistrazioneService registrazioneService;

    /**
     * Variabile che si riferisce al service del la gestione,
     * delle foto.
     * */
    @Autowired
    private FotoProfiloService fotoser;


    /**
     * Metodo per la registrazione di un utente in base al ruolo.
     * @param utente contiene le informazioni dell'utente che,
     *               vuol registrarsi.
     *
     * @return stringa contenente la risposta dell'operazione,
     * se è andata a buon fine o meno.
     * */
    @PostMapping(value = "/registrazioneUtente")
    public ResponseEntity<String> registrazioneUtente(@RequestBody final UtenteDTO utente)
            throws Exception {
        try {
        //System.out.println("Ricevuto DTO: " + utente.toString());
        String nome = utente.getNomeUtente();
        String cognome = utente.getCognomeUtente();
        String email = utente.getEmailUtente();
        String pass = utente.getPasswordUtente();

        String regexpPassword =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])"
                        + "[A-Za-z\\d@$!%*?&]{8,16}$";

        if (!pass.matches(regexpPassword)) {
            throw new Exception("La password non rispetta "
                    + "l'espressione regolare");
        }
        String password = utente.getPasswordUtente();
        String confermaPW = utente.getConfermaPWUtente();
        String nazionalita = utente.getNazionalitaUtente();
        String lingueParlate = utente.getLingueParlateUtente();
        Gender genere = utente.getGenderUtente();
        Ruolo ruolo = utente.getRuoloUtente();
        LocalDate dataNascita = utente.getDataNascitaUtente();
        String skill = utente.getSkillUtente();
        TitoloDiStudio titoloDiStudio = utente
                .getTitoloDiStudioUtente();

            // Gestione della foto del profilo (Base64 -> byte[])
            String fotoProfiloId = null;

            if (utente.getFotoUtente() != null && !utente.getFotoUtente().isEmpty()) {
                String base64Image = utente.getFotoUtente();

                // Verifica se contiene il prefisso 'data:image/jpeg;base64,' e rimuovilo
                if (base64Image.startsWith("data:image/jpeg;base64,")) {
                    base64Image = base64Image.split(",")[1]; // Estrai solo la parte Base64 dopo la virgola
                    byte[] fotoData = Base64.getDecoder().decode(base64Image);
                    fotoProfiloId = fotoser.saveIMG(nome, fotoData);

                } else {
                    System.err.println("Formato immagine non valido: la stringa Base64 non contiene il prefisso corretto.");
                }
            }
            switch (ruolo) {
            case Admin:
                     Admin a = new Admin(email,
                        nome,
                        cognome,
                        lingueParlate,
                        fotoProfiloId,
                        skill,
                        dataNascita,
                        titoloDiStudio,
                        ruolo,
                        genere,
                        nazionalita,
                        password);
                registrazioneService
                        .registraAdmin(a, confermaPW);
                break;

            case FiguraSpecializzata:
                String disponibilita = utente.getDisponibilitaUtente();
                FiguraSpecializzata fs = new FiguraSpecializzata(email,
                        nome,
                        cognome,
                        lingueParlate,
                        fotoProfiloId,
                        skill,
                        dataNascita,
                        titoloDiStudio,
                        ruolo,
                        genere,
                        nazionalita,
                        password,
                        disponibilita);
                registrazioneService
                        .registraFiguraSpecializzata(fs, confermaPW);
                break;

            case Volontario:
                Volontario v = new Volontario(email,
                        nome,
                        cognome,
                        lingueParlate,
                        fotoProfiloId,
                        skill,
                        dataNascita,
                        titoloDiStudio,
                        ruolo,
                        genere,
                        nazionalita,
                        password);

                registrazioneService
                        .registraVolontario(v, confermaPW);
                break;

            case Rifugiato:
                Rifugiato r = new Rifugiato(email,
                        nome,
                        cognome,
                        lingueParlate,
                        fotoProfiloId,
                        skill,
                        dataNascita,
                        titoloDiStudio,
                        ruolo,
                        genere,
                        nazionalita,
                        password);
                registrazioneService
                        .registraRifugiato(r, confermaPW);
                break;

                default:
                    throw new IllegalArgumentException("Ruolo non riconosciuto: " + ruolo);

        }

        return ResponseEntity.ok("Registrazione avvenuta con successo.");
    } catch (Exception e) {
        System.err.println("Errore durante la registrazione: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la registrazione: " + e.getMessage());
    }
}


    /**
     * Metodo per il login.
     * @param req richiesta per il login.
     * @return response.
     */
    @PostMapping(value = "/login")
    public AuthenticationResponse login(@RequestBody
                                        final AuthenticationRequest req)
            throws Exception {
        return registrazioneService.login(req);
    }

    /**
     * Metodo per il logout.
     * @return response.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // Risponde al client confermando il logout.
        return ResponseEntity.ok("Logout successful");
    }

}
