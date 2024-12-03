package com.project.bridgebackend.Model.Entity;

import com.project.bridgebackend.Model.Entity.enumeration.Lingua;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;


/**
 * @author Alessia De Filippo
 * Creato il: 03/12/2024
 * è la classe relativa all'entità Evento
 * I campi sono: id, nome, data, ora,
 * lingueParlate, descrizione, luogo,
 * organizzatore, maxPartecipanti e listaCandidati
 **/

@Entity
@ToString
@SuperBuilder
@Getter
@Setter
@Table(name = "Evento")
public class Evento implements Serializable {

    /**
     * Campo relativo all'id dell'evento
     * Generato automaticamente
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Campo relativo al nome dell'evento
     **/
    @NotBlank
    @Column(name = "nome", nullable = false)
    private String nome;

    /**
     * Campo relativo alla data dell'evento
     **/

    @NotBlank
    @Column(name = "data", nullable = false)
    private LocalDate data;

    /**
     * Campo relativo all'ora dell'evento
     **/
    @NotBlank
    @Column(name = "ora", nullable = false)
    private LocalTime ora;

    /**
     * Campo relativo alla lingua dell'evento
     **/
    @NotBlank
    @Column(name = "lingueParlate", nullable = false)
    private Lingua lingueParlate;

    /**
     * Campo relativo alla descrizione dell'evento
     **/
    @NotBlank
    @Column(name = "descrizione", nullable = false)
    private String descrizione;

    /**
     * Campo relativo al luogo dell'evento
     * è chiave esterna di Indirizzo
     **/
    @ManyToOne
    @JoinColumn(name = "luogo", referencedColumnName = "id")
    private Indirizzo luogo;

    /**
     * Campo relativo all'organizzatore dell'evento
     * è chiave esterna di Volontario
     **/
    @NotBlank
    @ManyToOne
    @JoinColumn(name = "organizzatore", referencedColumnName = "email")
    private Volontario organizzatore;

    /**
     * Campo relativo al numero massimo di partecipanti dell'evento
     **/
    @NotBlank
    @Column(name = "maxPartecipanti", nullable = false)
    private int maxPartecipanti;

    /**
     * Campo relativo alla lista di partecipanti dell'evento
     * è chiave esterna di Rifugiato
     **/
    @NotBlank
    @ManyToMany
    @JoinTable(name = "evento_rifugiato",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "rifugiato_email"))
    private List<Rifugiato> listaPartecipanti;

    /**
     * Costruttore vuoto
     **/
    public Evento() {}
}
