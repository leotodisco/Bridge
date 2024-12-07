package com.project.bridgebackend.registrazione.controller;

import com.project.bridgebackend.Model.Entity.Admin;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import com.project.bridgebackend.Model.dto.UtenteDTO;
import com.project.bridgebackend.registrazione.service.RegistrazioneService;
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
     * Metodo per la registrazione di un Volontario.
     * @param volontario
     * */
    @PostMapping(value = "/registrazioneVolontario")
    public void registrazione(@RequestBody final UtenteDTO volontario)
            throws Exception {
        String nome = volontario.getNomeUtente();
        String cognome = volontario.getCognomeUtente();
        String email = volontario.getEmailUtente();
        String password = safePassword(volontario.getPasswordUtente());
        volontario.setPasswordUtente(password);
        String nazionalita = volontario.getNazionalitaUtente();
        String lingueParlate = volontario.getLingueParlateUtente();
        Gender genere = volontario.getGenderUtente();
        Ruolo ruolo = volontario.getRuoloUtente();
        LocalDate dataNascita = volontario.getDataNascitaUtente();
        String Skill = volontario.getSkillUtente();
        TitoloDiStudio titoloDiStudio =
                volontario.getTitoloDiStudioUtente();
        byte[] fotoProfilo = volontario.getFotoUtente();

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
                .registraVolontario(v, volontario.getPasswordUtente());
    }

    /**
     * Metodo per la registrazione di un Rifugiato.
     * @param rifugiato
     * */
    @PostMapping(value = "/registrazioneRifugiato")
    public void registrazioneRifugiato(@RequestBody final UtenteDTO rifugiato)
            throws Exception {
        String nome = rifugiato.getNomeUtente();
        String cognome = rifugiato.getCognomeUtente();
        String email = rifugiato.getEmailUtente();
        String password = safePassword(rifugiato.getPasswordUtente());
        rifugiato.setPasswordUtente(password);
        String nazionalita = rifugiato.getNazionalitaUtente();
        String lingueParlate = rifugiato.getLingueParlateUtente();
        Gender genere = rifugiato.getGenderUtente();
        Ruolo ruolo = rifugiato.getRuoloUtente();
        LocalDate dataNascita = rifugiato.getDataNascitaUtente();
        String Skill = rifugiato.getSkillUtente();
        TitoloDiStudio titoloDiStudio = rifugiato
                .getTitoloDiStudioUtente();
        byte[] fotoProfilo = rifugiato.getFotoUtente();

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
                .registraRifugiato(r, rifugiato.getPasswordUtente());
    }

    /**
     * Metodo per la registrazione di una Figura Specializzata.
     * @param figspec
     * */
    @PostMapping(value = "/registrazioneFiguraSpecializzata")
    public void registrazioneFiguraSpecializzata(@RequestBody
                                                 final UtenteDTO figspec)
            throws Exception {
        String nome = figspec.getNomeUtente();
        String cognome = figspec.getCognomeUtente();
        String email = figspec.getEmailUtente();
        String password = safePassword(figspec.getPasswordUtente());
        figspec.setPasswordUtente(password);
        String nazionalita = figspec.getNazionalitaUtente();
        String lingueParlate = figspec.getLingueParlateUtente();
        Gender genere = figspec.getGenderUtente();
        Ruolo ruolo = figspec.getRuoloUtente();
        LocalDate dataNascita = figspec.getDataNascitaUtente();
        String Skill = figspec.getSkillUtente();
        TitoloDiStudio titoloDiStudio = figspec
                .getTitoloDiStudioUtente();
        byte[] fotoProfilo = figspec.getFotoUtente();
        String disponibilita = figspec.getDisponibilitaUtente();
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
                .registraFiguraSpecializzata(fs, figspec.getPasswordUtente());
    }

    /**
     * Metodo per la registrazione di un admin.
     * @param admin
     * */
    @PostMapping(value = "/registrazioneAdmin")
    public void registrazioneAdmin(@RequestBody final UtenteDTO admin)
            throws Exception {
        String nome = admin.getNomeUtente();
        String cognome = admin.getCognomeUtente();
        String email = admin.getEmailUtente();
        String password = safePassword(admin.getPasswordUtente());
        admin.setPasswordUtente(password);
        String nazionalita = admin.getNazionalitaUtente();
        String lingueParlate = admin.getLingueParlateUtente();
        Gender genere = admin.getGenderUtente();
        Ruolo ruolo = admin.getRuoloUtente();
        LocalDate dataNascita = admin.getDataNascitaUtente();
        String Skill = admin.getSkillUtente();
        TitoloDiStudio titoloDiStudio = admin
                .getTitoloDiStudioUtente();
        byte[] fotoProfilo = admin.getFotoUtente();
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
                .registraAdmin(a, admin.getPasswordUtente());
    }
}
