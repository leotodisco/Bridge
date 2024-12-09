package com.project.bridgebackend.registrazione.controller;

import com.project.bridgebackend.Model.Entity.*;
import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import com.project.bridgebackend.Model.dto.UtenteDTO;
import com.project.bridgebackend.registrazione.service.RegistrazioneService;
import com.project.bridgebackend.util.AuthenticationRequest;
import com.project.bridgebackend.util.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


import java.time.LocalDate;

/**
 * @author Antonio Ceruso.
 * Data creazione: 05/12/2024.
 * Classe controller che implementa i metodi per la registrazione degli utenti.
 * */
@RestController
@CrossOrigin
@RequestMapping(path = "authentication")
public class RegistrazioneController {
    /**
     * Variabile che si riferisce al service di registrazione.
     * */
    @Autowired
    private RegistrazioneService registrazioneService;

    /**
     * Metodo per la cifratura della password.
     * @param password
     * @return la password cifrata come stringa.
     * */
    public String safePassword(final String password) {
        BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
        String pwCifrata = pwEncoder.encode(password);
        return pwCifrata;
    }

    /**
     * Metodo per la registrazione di un utente in base al ruolo.
     * @param utente
     * */
    @PostMapping(value = "/registrazioneUtente")
    public void registrazioneUtente(@RequestBody final UtenteDTO utente)
            throws Exception {
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
        String password = safePassword(utente.getPasswordUtente());
        utente.setPasswordUtente(password);
        String nazionalita = utente.getNazionalitaUtente();
        String lingueParlate = utente.getLingueParlateUtente();
        Gender genere = utente.getGenderUtente();
        Ruolo ruolo = utente.getRuoloUtente();
        LocalDate dataNascita = utente.getDataNascitaUtente();
        String Skill = utente.getSkillUtente();
        TitoloDiStudio titoloDiStudio = utente
                .getTitoloDiStudioUtente();
        byte[] fotoProfilo = utente.getFotoUtente();
        switch (ruolo){
            case Admin:
                     Admin a = new Admin(email,
                        nome,
                        cognome,
                        lingueParlate,
                        fotoProfilo,
                        Skill,
                        dataNascita,
                        titoloDiStudio,
                        ruolo,
                        genere,
                        nazionalita,
                        password);
                registrazioneService
                        .registraAdmin(a, utente.getPasswordUtente());
                break;

            case FiguraSpecializzata:
                String disponibilita = utente.getDisponibilitaUtente();
                FiguraSpecializzata fs = new FiguraSpecializzata(email,
                        nome,
                        cognome,
                        lingueParlate,
                        fotoProfilo,
                        Skill,
                        dataNascita,
                        titoloDiStudio,
                        ruolo,
                        genere,
                        nazionalita,
                        password,
                        disponibilita);
                registrazioneService
                        .registraFiguraSpecializzata(fs, utente.getPasswordUtente());
                break;

            case Volontario:
                Volontario v = new Volontario(email,
                        nome,
                        cognome,
                        lingueParlate,
                        fotoProfilo,
                        Skill,
                        dataNascita,
                        titoloDiStudio,
                        ruolo,
                        genere,
                        nazionalita,
                        password);

                registrazioneService
                        .registraVolontario(v, utente.getPasswordUtente());
                break;

            case Rifugiato:
                Rifugiato r = new Rifugiato(email,
                        nome,
                        cognome,
                        lingueParlate,
                        fotoProfilo,
                        Skill,
                        dataNascita,
                        titoloDiStudio,
                        ruolo,
                        genere,
                        nazionalita,
                        password);
                registrazioneService
                        .registraRifugiato(r, utente.getPasswordUtente());
                break;
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
}
