package com.project.bridgebackend.Model.Entity;

import com.project.bridgebackend.Model.Entity.enumeration.Servizi;
import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * @author: Mario Zurolo.
 * Creato in data 3/12/24.
 * Entity per la gestione degli alloggi.
 * Contiene tutte le informazioni relative agli alloggi gestiti.
 */
@Data
@Entity
@Table(name = "alloggio")
public class Alloggio implements Serializable {

    /**
     * Identificatore unico dell'alloggio.
     * Viene generato automaticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Metratura dell'alloggio, espressa in metri quadrati.
     * Non può essere null e deve avere un valore positivo.
     */
    @Column(nullable = false)
    //@Max(value = 99999, message = "La metratura deve essere al massimo 99999")
    //@Min(value = 1, message = "La metratura deve essere almeno di 1")
    private int metratura;

    /**
     * Numero massimo di persone che l'alloggio può ospitare.
     * Non può essere null e deve essere un valore positivo.
     */
    @Column(nullable = false)
    //@Min(value = 1, message = "deve esserci almeno una persona")
    //@Max(value = 2, message = "devono esserci al massimo 99 persone")
    private int maxPersone;

    /**
     * Descrizione testuale dell'alloggio.
     * La descrizione può contenere fino a 400 caratteri.
     */
    @Column(nullable = false, length = 400)
    //@Size(max = 400, message = "la descrizione non può superare i 400 caratteri")
    private String descrizione;

    /**
     * Riferimento al proprietario dell'alloggio.
     * La chiave di riferimento è l'email dell'utente proprietario.
     * Non può essere null.
     */
    @JoinColumn(name = "proprietario", referencedColumnName = "email", nullable = false)
    @ManyToOne()
    private Utente proprietario;

    /**
     * Lista dei rifugiati che sono stati candidati per l'alloggio.
     * La lista può contenere uno o più rifugiati.
     */
    @ManyToMany()
    private List<Rifugiato> listaCandidati = new ArrayList<>();

    /**
     * Servizi offerti dall'alloggio, rappresentati tramite un tipo enumerato.
     * Il campo non può essere null.
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Servizi servizi;

    /**
     * Foto dell'alloggio. Ogni foto è rappresentata tramite un URL.
     * Il campo può contenere un massimo di 3 foto.
     */
    @ElementCollection
    //@CollectionTable(name = "alloggio_foto", joinColumns = @JoinColumn(name = "alloggio_id"))
    @Column(name = "foto_url")
    //@NotNull
    //@Size(min = 1, max = 3, message = "Devi fornire almeno 1 foto e massimo 3.")
    private List<String> foto;

    /**
     * Titolo univoco dell'alloggio.
     * Il campo deve essere unico e non può essere null.
     */
    @Column(nullable = false, unique = true)
    private String titolo;

    /**
     * Indirizzo associato all'alloggio.
     * L'indirizzo è una relazione uno a uno con l'entità Indirizzo.
     * Non può essere null.
     */
    @OneToOne()
    @JoinColumn(name = "indirizzo", referencedColumnName = "id", nullable = false)
    private Indirizzo indirizzo;

    /**
     * Riferimento al rifugiato assegnato a questo alloggio.
     * La chiave di riferimento è l'email del rifugiato.
     * Può essere null, poiché l'alloggio potrebbe non essere assegnato.
     */
    @JoinColumn(name = "rifugiato_assegnato", referencedColumnName = "email", nullable = true)
    @ManyToOne
    private Rifugiato assegnatoA;

    /**
     * Costruttore predefinito (senza argomenti) per inizializzare,
     * un oggetto Alloggio.
     * Questo costruttore viene utilizzato da JPA durante il caricamento,
     * dell'entità dal database.
     */
    public Alloggio() {
    }

    @Override
    public String toString() {
        return "Alloggio{" +
                "id=" + id +
                ", metratura=" + metratura +
                ", maxPersone=" + maxPersone +
                ", descrizione='" + descrizione + '\'' +
                ", proprietario=" + proprietario +
                ", listaCandidati=" + listaCandidati +
                ", servizi=" + servizi +
                ", foto=" + foto +
                ", titolo='" + titolo + '\'' +
                ", indirizzo=" + indirizzo +
                ", assegnatoA=" + assegnatoA +
                '}';
    }
}
