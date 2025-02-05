package com.project.bridgebackend.Model.Entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import jakarta.validation.constraints.Pattern;
import org.postgresql.util.PGobject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.hibernate.annotations.ColumnTransformer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.sql.SQLException;
import java.util.Arrays;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

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
@ToString
@Entity
@SuperBuilder
@Data
public class Utente implements Serializable, UserDetails {

    /**
     * Rappresenta l'email dell'utente registrato (chiave primaria).
     * */
    @Id
    @Column(name = "email")
    @NotNull
    @Email
    protected String email;

    /**
     * Nome dell'utente.
     * */
    @Column(name = "nome")
    @NotNull
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,30}$",
            message = "regexp per il nome non rispettata")
    private String nome;

    /**
     * Cognome dell'utente.
     * */
    @Column(name = "cognome")
    @NotNull
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,30}$",
            message = "regexp per il cognome non rispettata")
    private String cognome;

    /**
     * Password dell'utente.
     * */
    @Column(name = "password")
    @NotNull
    protected String password;

    /**
     * Nazionalità dell'utente.
     * */
    @Column(name = "nazionalita")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z\s]{5,30}$", message = "regexp per la nazionalità non rispettata")
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
    private String fotoProfilo;

    /**
     * Lingue parlate dall'utente.
     */
    @Column(name = "lingueParlate")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z ,]{5,30}$",
            message = "regexp per le lingue parlate non rispettate")
    private String lingueParlate;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "embeddingsJson", columnDefinition = "LONGTEXT")
    private String embeddingsJson;

    public void setEmbeddings(float[] values) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(values);
            System.out.println("🔍 Lunghezza JSON embeddings: " + json.length()); // Debug
            System.out.println("🔍 JSON embeddings: " + json); // Debug
            this.embeddingsJson = json;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Errore nella serializzazione degli embeddings", e);
        }
    }


    public float[] getEmbeddings() {
        try {
            if (this.embeddingsJson == null || this.embeddingsJson.isEmpty()) {
                return new float[0]; // Restituisce un array vuoto se non ci sono embeddings
            }
            System.out.println("🔍 JSON embeddings recuperato: " + this.embeddingsJson); // Debug
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(this.embeddingsJson, float[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Errore nella deserializzazione degli embeddings", e);
        }
    }

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
                  final String fotoProfilo,
                  final String skill,
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
        this.embeddingsJson = null;
    }

    public Utente(final String email,
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
                  final String embedding) {
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
        this.embeddingsJson = embedding;
    }
    /**
     * Metodo che ritorna una lista di ruoli.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }


}
