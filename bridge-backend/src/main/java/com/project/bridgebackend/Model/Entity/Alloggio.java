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

import java.util.*;// Genera automaticamente metodi getter, setter, toString, equals e hashCode.

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * Annotazione Lombok per ridurre il codice boilerplate, automatizzando getter, setter e altri metodi.
 */

@Data

/**
 * Annotazione JPA per dichiarare che questa classe rappresenta un'entità del database.
 */

@Entity
@Table(name = "Alloggio")
public class Alloggio {

    /**
     * Annotazione per identificare il campo 'id' come chiave primaria.
     */
    @Id
    /**
     *  Genera automaticamente il valore della chiave primaria (ad esempio, un ID incrementale).
     */
    @GeneratedValue()
    private long id;

    /**
     *  Campo per la metratura dell'alloggio (es. dimensione in metri quadrati).
     */

    @Column(nullable = false)
    @Max(value = 99999, message = "La metratura deve essere al massimo 99999")
    @Min(value = 1, message = "La metratura deve essere almeno di 1")
    private int metratura;

    /**
     *  Campo per il numero massimo di persone che l'alloggio può ospitare.
     */
    @Column(nullable = false)
    @Min(value = 1, message = "deve esserci almeno una persona")
    @Max(value = 2, message = "devono esserci al massimo 99 persone")
    private int maxPersone;

    /**
     *  Campo per una descrizione testuale dell'alloggio (es. caratteristiche, ubicazione, ecc.).
     */
    @Column(nullable = false, length = 400)
    @Size(max = 400, message = "la descrizione non può superare i 400 caratteri")
    private String descrizione;

    /**
     *  Campo che rappresenta il proprietario dell'alloggio.
     */
    @Column(nullable = false)
    @ManyToOne
    private Utente proprietario;

    /**
     *  Lista di rifugiati candidati per l'alloggio.
     */
    @Column(nullable = false)
    @OneToMany
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
    @CollectionTable(name = "byte_list", joinColumns = @JoinColumn(name = "entity_id"))
    @Column(name = "byte_value")
    @NonNull
    private List<Byte> byteList;

    /**
     *  Costruttore predefinito (senza argomenti) per inizializzare l'oggetto.
     */
    public Alloggio() {

    }
}
