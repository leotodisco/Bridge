package com.project.bridgebackend.Model.Entity;

import com.project.bridgebackend.Model.Entity.enumeration.TipoConsulenza;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author Geraldine Montella.
 * Creato il: 03/12/2024.
 * Questa Java Persistence Entity per una consulenza,
 *
 * eredita altri attributi dall'entity Annuncio
 */
@Entity
@Getter
@Setter
@Table(name = "consulenza")
public class Consulenza extends Annuncio {

    /**
    * Tipo identifica la tipologia di consulenza
    * che si vuole proporre
    * */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoConsulenza tipo;

    /**
    * Orari di disponibilità indicano la fascia oraria
    * in cui il consulente è disponbile
    * todo: to verify if string is a valid option
     */
    @Column(name = "orari_disponibili", nullable = false, length = 500)
    private String orariDisponibili;

    /**
    * Descrizione è una stringa atta a discriminare come la
    * consulenza mira ad aiutare il rifugiato
    */
    @NotBlank(message = "la descrizione non può essere vuota")
    @Column(name = "descrizione", nullable = false, length = 1000)
    private String descrizione;

    /**
     * Numero di telefono per contattare il consulente.
     * Valida il formato secondo il modello italiano.
     */
    @NotBlank(message = "Il numero non può essere vuoto")
    @Pattern(
            regexp = "^((00|\\+)39[\\. ]??)??3\\d{2}[\\. ]??\\d{6,7}$",
            message = "Numero di telefono non valido. Deve seguire il formato italiano."
    )
    @Column(name = "numero", nullable = false, length = 15)
    private String numero;

    /**
     * Costruttore vuoto richiesto da JPA.
     */
    public Consulenza() {
        super();
    }

    /**
     * Costruttore completo per l'inizializzazione di un oggetto Consulenza.
     *
     * @param proprietario     il proprietario dell'annuncio
     * @param tipologia        specifica se è un annuncio di consulenza o altro
     * @param titolo           titolo dell'annuncio
     * @param disponibilita    disponibilità del consulente
     * @param indirizzo        indirizzo associato alla consulenza
     * @param maxCandidature   massimo numero di candidature accettate
     * @param candidati        lista dei candidati
     * @param orariDisponibili fasce orarie in cui il consulente è disponibile
     * @param descrizione      descrizione dettagliata della consulenza
     * @param numero           numero di contatto del consulente
     * @param tipo             tipo specifico di consulenza
     */
    public Consulenza(FiguraSpecializzata proprietario, Boolean tipologia, String titolo, Boolean disponibilita, Indirizzo indirizzo, int maxCandidature, List<String> candidati, String orariDisponibili, String descrizione, String numero, TipoConsulenza tipo) {
        super(proprietario, tipologia, titolo, disponibilita, indirizzo, maxCandidature, candidati);
        this.orariDisponibili = orariDisponibili;
        this.descrizione = descrizione;
        this.numero = numero;
        this.tipo = tipo;
    }
}
