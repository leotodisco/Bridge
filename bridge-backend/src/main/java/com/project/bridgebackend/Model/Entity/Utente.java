package com.project.bridgebackend.Model.Entity;

import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;

/*
 * @author Antonio Ceruso
 * Creati il: 03/12/2024
 * Questa è la classe relativa ad un UtenteRegistrato
 * I suoi campi sono:
 * email
 * nome
 * titoloDiStudio
 * nazionalita
 * lingueParlate
 * role
 * salt
 * password
 * gender
 * dataNascita
 * cognome
 * Skill
 * fotoProfilo
 * */
@Entity
@SuperBuilder
@Data
public class Utente implements Serializable {

    /*
     * Rappresenta l'email dell'utente registrato (chiave primaria)
     * */
    @Id
    @Column(name = "email")
    @NonNull
    protected String email;

    /*
     * Nome dell'utente
     * */
    @Column(name = "nome")
    @NonNull
    private String nome;

    /*
     * Cognome dell'utente
     * */
    @Column(name = "cognome")
    @NonNull
    private String cognome;

    /*
     * Password dell'utente
     * */
    @Column(name = "password")
    @NonNull
    protected String password;

    /*
     * Nazionalità dell'utente
     * */
    @Column(name = "nazionalita")
    @NonNull
    private String nazionalita;

    /*
     * Salt utilizzato per la sicurezza della password
     * */
    @Column(name = "salt")
    @NonNull
    protected String Salt;

    /*
     * Genere dell'utente
     * */
    @Column(name = "genere")
    @Enumerated(EnumType.STRING)
    @NonNull
    private Gender gender;

    /*
     * Ruolo dell'utente nell'applicazione
     * */
    @Column(name = "ruolo")
    @Enumerated(EnumType.STRING)
    @NonNull
    private Ruolo role;

    /*
     * Titolo di studio posseduto dall'utente
     * */
    @Column(name = "titoloDiStudio")
    @NonNull
    @Enumerated(EnumType.STRING)
    private TitoloDiStudio titoloDiStudio;

    /*
     * Data di nascita dell'utente
     * */
    @Column(name = "dataNascita")
    @NonNull
    private LocalDate dataNascita;

    /*
     * Skill o competenze dell'utente
     * */
    @Column(name = "skill")
    @NonNull
    private String skill;

    /*
     * Foto profilo dell'utente (in formato Byte[])
     * */
    @Column(name = "fotoProfilo")
    @NonNull
    private Byte[] fotoProfilo;

    /*
     * Lingue parlate dall'utente
     * */
    @Column(name = "lingueParlate")
    @NonNull
    private String lingueParlate;

    /*
     * Costruttore vuoto per utente
     * */
    public Utente() {
    }

    /*
     * Costruttore con tutti i campi dell'utente
     * @param email rappresenta l'email dell'utente
     * @param nome rappresenta il nome dell'utente
     * @param cognome rappresenta il cognome dell'utente
     * @param lingueParlate rappresenta le lingue parlate dall'utente
     * @param fotoProfilo rappresenta la foto profilo dell'utente
     * @param skill rappresenta le skill dell'utente
     * @param dataNascita rappresenta la data di nascita dell'utente
     * @param titoloDiStudio rappresenta il titolo di studio posseduto dall'utente
     * @param role rappresenta il ruolo dell'utente nell'applicazione
     * @param gender rappresenta il genere dell'utente
     * @param salt rappresenta il salt utilizzato per la gestione sicura della password
     * @param nazionalita rappresenta la nazionalità dell'utente
     * @param password rappresenta la password dell'utente
     */
    public Utente(String email, String nome, String cognome, String lingueParlate, Byte[] fotoProfilo, String skill, LocalDate dataNascita, TitoloDiStudio titoloDiStudio, Ruolo role, Gender gender, String salt, String nazionalita, String password) {
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
        Salt = salt;
        this.nazionalita = nazionalita;
        this.password = password;
    }
}
