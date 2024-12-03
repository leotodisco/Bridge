package com.project.bridgebackend.Model.Entity;

import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author Benedetta Colella.
 * Creato il: 03/12/2024.
 * Questa è la classe relativa ad un Admin.
 * */

@Entity
@Data
public class Admin extends Utente{
    /**
     * Costruttore con tutti i campi dell'utente
     * @param email
     * @param nome
     * @param nazionalita
     * @param titoloDiStudio
     * @param lingueParlate
     * @param role
     * @param salt
     * @param password
     * @param gender
     * @param dataNascita
     * @param cognome
     * @param skill
     * @param fotoProfilo
     */

    public Admin(String email, String nome, String cognome, String lingueParlate, Byte[] fotoProfilo, String skill, LocalDate dataNascita, TitoloDiStudio titoloDiStudio, Ruolo role, Gender gender, String salt, String nazionalita, String password) {
        super(email, nome, cognome, lingueParlate, fotoProfilo, skill, dataNascita, titoloDiStudio, role, gender, salt, nazionalita, password);
    }

    /**
     * costruttore vuoto.
     */
    public Admin() {
    }
}
