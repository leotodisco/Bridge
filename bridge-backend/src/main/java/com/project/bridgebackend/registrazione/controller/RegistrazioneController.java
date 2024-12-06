package com.project.bridgebackend.registrazione.controller;

import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import com.project.bridgebackend.Model.dto.UtenteDTO;
import com.project.bridgebackend.registrazione.service.RegistrazioneService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping(path = "authentication")
public class RegistrazioneController {
    @Autowired
    private RegistrazioneService registrazioneService;

    @PostMapping(value = "/registrazioneVolontario")
    public void registrazione(@RequestBody UtenteDTO volontario) throws Exception {
            String nome = volontario.getNomeUtente();
            String cognome = volontario.getCognomeUtente();
            String email = volontario.getEmailUtente();
            String password = volontario.getPasswordUtente();
            String salt = volontario.getSaltUtente();
            String nazionalita = volontario.getNazionalitaUtente();
            String lingueParlate = volontario.getLingueParlateUtente();
            Gender genere = volontario.getGenderUtente();
            Ruolo ruolo = volontario.getRuoloUtente();
            LocalDate dataNascita = volontario.getDataNascitaUtente();
            String Skill = volontario.getSkillUtente();
            TitoloDiStudio titoloDiStudio = volontario.getTitoloDiStudioUtente();
            byte[] fotoProfilo = volontario.getFotoUtente();

            Volontario v = new Volontario(email, nome, cognome, lingueParlate, fotoProfilo, Skill, dataNascita, titoloDiStudio, ruolo, genere, salt, nazionalita, password);

            registrazioneService.registraVolontario(v, volontario.getPasswordUtente());
    }

    @PostMapping(value = "/registrazioneRifugiato")
    public void registrazioneRifugiato(@RequestBody UtenteDTO rifugiato) throws Exception {
        String nome = rifugiato.getNomeUtente();
        String cognome = rifugiato.getCognomeUtente();
        String email = rifugiato.getEmailUtente();
        String password = rifugiato.getPasswordUtente();
        String salt = rifugiato.getSaltUtente();
        String nazionalita = rifugiato.getNazionalitaUtente();
        String lingueParlate = rifugiato.getLingueParlateUtente();
        Gender genere = rifugiato.getGenderUtente();
        Ruolo ruolo = rifugiato.getRuoloUtente();
        LocalDate dataNascita = rifugiato.getDataNascitaUtente();
        String Skill = rifugiato.getSkillUtente();
        TitoloDiStudio titoloDiStudio = rifugiato.getTitoloDiStudioUtente();
        byte[] fotoProfilo = rifugiato.getFotoUtente();

        Rifugiato r = new Rifugiato(email, nome, cognome, lingueParlate, fotoProfilo, Skill, dataNascita, titoloDiStudio, ruolo, genere, salt, nazionalita, password);

        registrazioneService.registraRifugiato(r, rifugiato.getPasswordUtente());
    }

}
