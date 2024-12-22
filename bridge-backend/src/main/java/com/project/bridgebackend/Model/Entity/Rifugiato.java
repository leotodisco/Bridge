package com.project.bridgebackend.Model.Entity;

import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;


/**
 * @author Antonio Ceruso.
 * Creato il: 03/12/2024.
 * Questa è la classe relativa ad un Rifugiato.
 * */
@Entity
@Data
public class Rifugiato extends Utente {

    @ManyToOne
    @JoinColumn(name = "alloggio_id", nullable = true) // nullable=true se la relazione è opzionale
    private Alloggio alloggio;
    /**
     * Costruttore con tutti i campi dell'utente.
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
     * @param nazionalita
     * @param password
     */
    public Rifugiato(final String email,
                     final String nome,
                     final String cognome,
                     final String lingueParlate,
                     final String  fotoProfilo,
                     final String skill,
                     final LocalDate dataNascita,
                     final TitoloDiStudio titoloDiStudio,
                     final Ruolo role,
                     final Gender gender,
                     final String nazionalita,
                     final String password) {
        super(email, nome, cognome,
                lingueParlate, fotoProfilo, skill,
                dataNascita, titoloDiStudio, role,
                gender, nazionalita, password);
    }
    /**
     * costruttore vuoto.
     */
    public Rifugiato() {
    }
}
