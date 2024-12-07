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
 * Questa è la classe relativa ad un Volontario.
 * */
@Entity
@Data
public class Volontario extends Utente {
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
    public Volontario(final String email,
                      final String nome,
                      final String cognome,
                      final String lingueParlate,
                      final byte[] fotoProfilo,
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
    public Volontario() {
    }
}
