package com.project.bridgebackend.Model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "annuncio")
/**
 *
 * @author Geraldine Montella.
 * Creato il: 03/12/2024.
 * Questa Java Persistence Entity per un annuncio,
 *
 */
public class Annuncio {

    /**
     * ID univoco per ogni annuncio.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Riferimento all'utente proprietario dell'annuncio.
     * È una relazione `@ManyToOne`, in cui più annunci possono appartenere allo stesso utente
     */
    @ManyToOne
    @JoinColumn(name = "proprietario_email", referencedColumnName = "email", nullable = false)
    private Utente proprietario;

    /**
     * Indica la tipologia dell'annuncio (es. privato/commerciale).
     * È un campo obbligatorio, rappresentato da un valore booleano (true/false).
     * La colonna nel database è chiamata `tipologia`.
     */
    @Column(name = "tipologia", nullable = false)
    private Boolean tipologia;

    /**
     * Titolo dell'annuncio, che descrive brevemente il contenuto.
     * È un campo obbligatorio, con una lunghezza massima di 100 caratteri.
     * La colonna nel database è chiamata `titolo`.
     */
    @Column(name = "titolo", nullable = false, length = 100)
    private String titolo;

    /**
     * Indica se l'annuncio è attualmente disponibile.
     * È un campo obbligatorio rappresentato da un valore booleano (true/false).
     * La colonna nel database è chiamata `disponibilita`.
     */
    @Column(name = "disponibilita", nullable = false)
    private Boolean disponibilita;

    /**
     * Riferimento all'entità `Indirizzo`, che rappresenta l'indirizzo associato all'annuncio.
     * È una relazione `@OneToOne`, in cui più annunci possono condividere lo stesso indirizzo.
     */
    @OneToOne
    @JoinColumn(name = "indirizzo_id", nullable = false)
    private Indirizzo indirizzo;

    /**
     * Numero massimo di candidature che l'annuncio può accettare.
     * È un campo obbligatorio con un valore intero.
     * La colonna nel database è chiamata `max_candidature`.
     */
    @Column(name = "max_candidature", nullable = false)
    private int maxCandidature;

    /**
     * Lista di candidati (di tipo `Rifugiato`) che hanno applicato per l'annuncio.
     * È una relazione `@OneToMany`, in cui un annuncio può essere associato a più candidati.
     * La relazione è mappata dall'attributo `annuncio` nell'entità `Rifugiato`.
     */
    @OneToMany(mappedBy = "annuncio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rifugiato> candidati;
}
