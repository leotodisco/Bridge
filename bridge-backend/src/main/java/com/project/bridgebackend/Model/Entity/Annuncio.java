package com.project.bridgebackend.Model.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;


/**
 *
 * @author Geraldine Montella.
 * Creato il: 03/12/2024.
 * Questa Java Persistence Entity per un annuncio,
 * la inheritance è settata a JOINED perciò Ogni entità ha la propria tabella,
 * e le tabelle sono unite tramite chiavi primarie.
 */

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Annuncio {

    /**
     * ID univoco per ogni annuncio.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Riferimento all'utente proprietario dell'annuncio.
     * È una relazione @ManyToOne, in cui più annunci possono appartenere allo stesso utente,
     * il fetch lazy è stato aggiunto per evitare che i dati vengano carricati,
     * immediatamente appena l'entità principale viene caricata, ma solo quando,
     * richiesti nel codice.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietario_email", referencedColumnName = "email", nullable = false)
    private Utente proprietario;

    /**
     * Indica la tipologia dell'annuncio (es. privato/commerciale).
     * È un campo obbligatorio, rappresentato da un valore booleano (true/false).
     * La colonna nel database è chiamata tipologia.
     */
    @Column(name = "tipologia", nullable = false)
    @NotNull
    private Boolean tipologia;

    /**
     * Titolo dell'annuncio, che descrive brevemente il contenuto.
     * È un campo obbligatorio, con una lunghezza massima di 100 caratteri.
     * La colonna nel database è chiamata titolo.
     */
    @Column(name = "titolo", nullable = false)
    @Size(max = 100)
    @NotNull
    private String titolo;

    /**
     * Indica se l'annuncio è attualmente disponibile.
     * È un campo obbligatorio rappresentato da un valore booleano (true/false).
     * La colonna nel database è chiamata disponibilita.
     */
    @Column(name = "disponibilita", nullable = false)
    @NotNull
    private Boolean disponibilita;

    /**
     * Riferimento all'entità Indirizzo, che rappresenta l'indirizzo associato all'annuncio.
     * È una relazione @OneToOne, in cui più annunci possono condividere lo stesso indirizzo.
     * Il fetch lazy è stato aggiunto per evitare che i dati vengano carricati
     * immediatamente appena l'entità principale viene caricata, ma solo quando
     * richiesti nel codice
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "indirizzo_id", nullable = false)
    private Indirizzo indirizzo;

    /**
     * Numero massimo di candidature che l'annuncio può accettare.
     * È un campo obbligatorio con un valore intero.
     * La colonna nel database è chiamata max_candidature.
     *
     * si deve consentire ad almeno un rifugiato di candidarsi
     */
    @Column(name = "max_candidature", nullable = false)
    @NotNull
    @Min(1)
    private int maxCandidature;

    /**
     * Lista di candidati (di tipo Rifugiato) che hanno applicato per l'annuncio.
     * È una relazione @OneToMany, in cui un annuncio può essere associato a più candidati.
     * La relazione è mappata dall'attributo annuncio nell'entità Rifugiato.
     */
    @ElementCollection
    @CollectionTable(
            name = "annuncio_rifugiati",
            joinColumns = @JoinColumn(name = "annuncio_id")
    )
    @Column(name = "rifugiato_email", nullable = false)
    private List<String> candidati;
}
