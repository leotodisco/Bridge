package com.project.bridgebackend.Model.Entity;

import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDate;


/**
 * @author Antonio Ceruso.
 * Creato il: 03/12/2024.
 * Questa è la classe relativa ad un Rifugiato.
 * */
@Entity
@Data
public class Rifugiato extends Utente{
    /*
     * Costruttore con tutti i campi dell'utente
     * @param email
     * @param nome
     * @param cognome
     * @param lingueParlate
     * @param fotoProfilo
     * @param skill
     * @param dataNascita
     * @param titoloDiStudio
     * @param role
     * @param gender
     * @param salt
     * @param nazionalita
     * @param password
     */
    public Rifugiato(String email, String nome, String cognome, String lingueParlate, Byte[] fotoProfilo, String skill, LocalDate dataNascita, TitoloDiStudio titoloDiStudio, Ruolo role, Gender gender, String salt, String nazionalita, String password) {
        super(email, nome, cognome, lingueParlate, fotoProfilo, skill, dataNascita, titoloDiStudio, role, gender, salt, nazionalita, password);
    }
    /**
     * costruttore vuoto.
     */
    public Rifugiato() {

    }
}
