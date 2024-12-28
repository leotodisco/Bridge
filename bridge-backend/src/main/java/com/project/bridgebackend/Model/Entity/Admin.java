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
 * Questa è la classe relativa a un Admin.
 * */

@Entity
@Data
public class Admin extends Utente {
    /**
     * Costruttore con tutti i campi dell'utente.
     * @param email
     * @param nome
     * @param nazionalita
     * @param titoloDiStudio
     * @param lingueParlate
     * @param role
     * @param password
     * @param gender
     * @param dataNascita
     * @param cognome
     * @param skill
     * @param fotoProfilo
     */

    public Admin(final String email,
                 final String nome,
                 final String cognome,
                 final String lingueParlate,
                 final String fotoProfilo,
                 final String skill,
                 final LocalDate dataNascita,
                 final TitoloDiStudio titoloDiStudio,
                 final Ruolo role,
                 final Gender gender,
                 final String nazionalita,
                 final String password) {
        super(email, nome, cognome, lingueParlate, fotoProfilo,
                skill, dataNascita, titoloDiStudio, role, gender,
                nazionalita, password);
    }

    /**
     * Costruttore vuoto.
     */
    public Admin() {
    }
}
