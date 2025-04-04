package com.project.bridgebackend.Model.Entity;

import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
/**
 * @author Antonio Ceruso.
 * Creato il: 03/12/2024.
 * Questa è la classe relativa ad una Figura Specializzata.
 * */
@Entity
@Data
public class FiguraSpecializzata extends Utente {
    /**
     * Disponibilità orarie della Figura Specializzata.
     * */
    @Pattern(regexp = "^[A-Za-z0-9 ì,.:;-]+$", message = "Regexp per la disponibilita' non rispettata")
    private String disponibilita;
    /**
     * costruttore vuoto.
     */
    public FiguraSpecializzata() {
    }

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
     * @param disponibilita
     */
    public FiguraSpecializzata(final String email,
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
                               final String password,
                               final String disponibilita) {
        super(email, nome, cognome,
                lingueParlate, fotoProfilo, skill,
                dataNascita, titoloDiStudio, role,
                gender, nazionalita, password);
        this.disponibilita = disponibilita;
    }
}
