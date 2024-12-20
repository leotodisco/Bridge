package com.project.bridgebackend.Model.Entity;

/**
 * @author: Mario Zurolo
 * created: 3/12/24
 * entity per la gestione degli alloggi
 */

import com.project.bridgebackend.Model.Entity.enumeration.Servizi;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;


@Data
@Entity
@Table(name = "alloggio")
public class Alloggio implements Serializable {

    /**
     * Annotazione per identificare il campo 'id' come chiave primaria.
     */
    /**
     *  Genera automaticamente il valore della chiave primaria (ad esempio, un ID incrementale).
     */
    @Id
    @GeneratedValue()
    private long id;

    /**
     *  Campo per la metratura dell'alloggio (es. dimensione in metri quadrati).
     */

    @Column(nullable = false)
    //@Max(value = 99999, message = "La metratura deve essere al massimo 99999")
    //@Min(value = 1, message = "La metratura deve essere almeno di 1")
    private int metratura;

    /**
     *  Campo per il numero massimo di persone che l'alloggio può ospitare.
     */
    @Column(nullable = false)
    //@Min(value = 1, message = "deve esserci almeno una persona")
    //@Max(value = 2, message = "devono esserci al massimo 99 persone")
    private int maxPersone;

    /**
     *  Campo per una descrizione testuale dell'alloggio (es. caratteristiche, ubicazione, ecc.).
     */
    @Column(nullable = false, length = 400)
    //@Size(max = 400, message = "la descrizione non può superare i 400 caratteri")
    private String descrizione;

    /**
     *  Campo che rappresenta il proprietario dell'alloggio.
     *
     */

    @JoinColumn(name = "proprietario", referencedColumnName = "email", nullable = false)
    @ManyToOne()
    private Utente proprietario;

    /**
     *  Lista di rifugiati candidati per l'alloggio.
     */
    @Column()
    @OneToMany()
    private List<Rifugiato> listaCandidati;

    /**
     *  Campo che rappresenta i servizi offerti dall'alloggio
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Servizi servizi;

    /**
     *  campo per le foto dell'alloggio
     */
    @ElementCollection
    //@CollectionTable(name = "alloggio_foto", joinColumns = @JoinColumn(name = "alloggio_id"))
    @Column(name = "foto_url")
    //@NotNull
    //@Size(min = 1, max = 3, message = "Devi fornire almeno 1 foto e massimo 3.")
    private List<String> foto;

    @Column(nullable = false, unique = true)
    private String titolo;

    @OneToOne()
    @JoinColumn(name = "indirizzo", referencedColumnName = "id", nullable = false)
    private Indirizzo indirizzo;

    /**
     *  Costruttore predefinito (senza argomenti) per inizializzare l'oggetto.
     */
    public Alloggio() {

    }
}
