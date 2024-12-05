package com.project.bridgebackend.Model.dto;

import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import lombok.Data;

import java.time.LocalDate;
/**
 * @author Antonio Ceruso.
 * Creato il: 04/12/2024.
 * Classe DTO che serve a passare solo i dati realmente necessari al frontend.
 */
@Data
public class UtenteDTO {
    /**
     * Parametro email dell'Utente
     * */
    private String emailUtente;
    /**
     * Parametro nome dell'Utente
     */
    private String nomeUtente;

    /**
     * Parametro cognome dell'Utente
     */
    private String cognomeUtente;

    /**
     * Parametro nazionalità dell'Utente
     */
    private String nazionalitaUtente;

    /**
     * Parametro data di nascita dell'Utente
     */
    private LocalDate dataNascitaUtente;

    /**
     * Parametro genere dell'Utente
     */
    private Gender genderUtente;

    /**
     * Parametro titolo di studio dell'Utente
     */
    private TitoloDiStudio titoloDiStudioUtente;

    /**
     * Parametro password dell'Utente
     */
    private String passwordUtente;

    /**
     * Parametro competenze dell'Utente
     */
    private String skillUtente;

    /**
     * Parametro foto dell'Utente
     */
    private byte[] fotoUtente;

    /**
     * Parametro disponibilità dell'Utente
     */
    private String disponibilitaUtente;

    /**
     * Parametro ruolo dell'Utente
     */
    private Ruolo ruoloUtente;

    /**
     * Parametro lingue parlate dell'Utente
     */
    private String lingueParlateUtente;

    /**
     * Parametro salt password dell'Utente
     */
    private String saltUtente;
}
