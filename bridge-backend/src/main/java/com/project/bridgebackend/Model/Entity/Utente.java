package com.project.bridgebackend.Model.Entity;

import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Antonio Ceruso.
 * Creati il: 03/12/2024.
 * Questa è la classe relativa ad un UtenteRegistrato.
 * I suoi campi sono:
 * email.
 * nome.
 * titoloDiStudio.
 * nazionalita.
 * lingueParlate.
 * role.
 * password.
 * gender.
 * dataNascita.
 * cognome.
 * Skill.
 * fotoProfilo.
 * */
@Entity
@SuperBuilder
@Data
public class Utente implements Serializable {

    /**
     * Rappresenta l'email dell'utente registrato (chiave primaria).
     * */
    @Id
    @Column(name = "email")
    @NotNull
    @Pattern(regexp = "/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$/",
            message = "regexp per l'email non rispettata")
    protected String email;

    /**
     * Nome dell'utente.
     * */
    @Column(name = "nome")
    @NotNull
    @Pattern(regexp = "/^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,30}$/",
            message = "regexp per il nome non rispettata")
    private String nome;

    /**
     * Cognome dell'utente.
     * */
    @Column(name = "cognome")
    @NotNull
    @Pattern(regexp = "/^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,30}$/",
            message = "regexp per il cognome non rispettata")
    private String cognome;

    /**
     * Password dell'utente.
     * */
    @Column(name = "password")
    @NotNull
    @Pattern(regexp = "/^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$",
            message = "regexp per la password non rispettata")
    protected String password;

    /**
     * Nazionalità dell'utente.
     * */
    @Column(name = "nazionalita")
    @NotNull
    @Pattern(regexp = "/^[a-zA-Z\\s]{5,30}$/\n", message = "regexp per la nazionalità non rispettata")
    private String nazionalita;


    /**
     * Genere dell'utente.
     * */
    @Column(name = "genere")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;

    /**
     * Ruolo dell'utente nell'applicazione.
     * */
    @Column(name = "ruolo")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Ruolo role;

    /**
     * Titolo di studio posseduto dall'utente.
     * */
    @Column(name = "titoloDiStudio")
    @NotNull
    @Enumerated(EnumType.STRING)
    private TitoloDiStudio titoloDiStudio;

    /**
     * Data di nascita dell'utente.
     * */
    @Column(name = "dataNascita")
    @NotNull
    @Past
    private LocalDate dataNascita;

    /**
     * Skill o competenze dell'utente.
     * */
    @Column(name = "skill")
    private String skill;

    /**
     * Foto profilo dell'utente (in formato Byte[]).
     * */
    @Column(name = "fotoProfilo")
    @NotNull
    private byte[] fotoProfilo;

    /**
     * Lingue parlate dall'utente.
     */
    @Column(name = "lingueParlate")
    @NotNull
    @Pattern(regexp = "/^[a-zA-Z\\s]{5,30}$/\n",
            message = "regexp per le lingue parlate non rispettate")
    private String lingueParlate;

    /**
     * Costruttore vuoto per utente.
     * */
    public Utente() {
    }

    /**
     * Costruttore con tutti i campi dell'utente.
     * @param email rappresenta l'email dell'utente.
     * @param nome rappresenta il nome dell'utente.
     * @param cognome rappresenta il cognome dell'utente.
     * @param lingueParlate rappresenta le lingue parlate dall'utente.
     * @param fotoProfilo rappresenta la foto profilo dell'utente.
     * @param skill rappresenta le skill dell'utente.
     * @param dataNascita rappresenta la data di nascita dell'utente.
     * @param titoloDiStudio rappresenta il titolo di studio
     * @param role rappresenta il ruolo dell'utente nell'applicazione.
     * @param gender rappresenta il genere dell'utente.
     * @param nazionalita rappresenta la nazionalità dell'utente.
     * @param password rappresenta la password dell'utente.
     */
    public Utente(final String email,
                  final String nome,
                  final String cognome,
                  final String lingueParlate,
                  final byte[] fotoProfilo, String skill,
                  final LocalDate dataNascita,
                  final TitoloDiStudio titoloDiStudio,
                  final Ruolo role,
                  final Gender gender,
                  final String nazionalita,
                  final String password) {
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.lingueParlate = lingueParlate;
        this.fotoProfilo = fotoProfilo;
        this.skill = skill;
        this.dataNascita = dataNascita;
        this.titoloDiStudio = titoloDiStudio;
        this.role = role;
        this.gender = gender;
        this.nazionalita = nazionalita;
        this.password = password;
    }
}