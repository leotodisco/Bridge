package com.project.bridgebackend.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.bridgebackend.Model.Entity.enumeration.Lingua;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


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
@Table(name = "evento")
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
    @NotBlank(message = "Il nome dell'evento non può essere vuoto")
    @Pattern(regexp = "^[A-Za-z0-0-ÿ .,'-]{3,100}$", message = "Il nome contiene caratteri non validi")
    @Size(min = 3, max = 100, message = "Il nome dell'evento deve essere tra 3 e 100 caratteri")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    /**
     * Campo relativo alla data dell'evento
     **/

    @NotNull(message = "La data dell'evento è obbligatoria")
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])\\/(0[1-9]|1[0-2])\\/(19[0-9]{2}|20[0-2][0-9])$")
    @FutureOrPresent(message = "La data dell'evento non può essere nel passato")
    @Column(name = "data", nullable = false)
    private LocalDate data;

    /**
     * Campo relativo all'ora dell'evento
     **/
    @NotNull(message = "L'ora dell'evento è obbligatoria")
    @Pattern(regexp = "^\\d{2}:\\d{2}$")
    @Column(name = "ora", nullable = false)
    private LocalTime ora;

    /**
     * Campo relativo alle lingue parlate durante l'evento
     **/
    @NotNull(message = "La lingue dell'evento sono obbligatorie")
    @ElementCollection(targetClass = Lingua.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "evento_lingue_parlate", joinColumns = @JoinColumn(name = "evento_id"))
    @Column(name = "lingueParlate", nullable = false)
    private List<Lingua> lingueParlate;

    /**
     * Campo relativo alla descrizione dell'evento
     **/
    @NotBlank(message = "La descrizione dell'evento non può essere vuota")
    @Pattern(regexp = "^[A-Za-zÀ-ÿ0-9]{2,500}$")
    @Size(min = 1, max = 1000, message = "La descrizione dell'evento deve essere tra 1 e 1000 caratteri")
    @Column(name = "descrizione", nullable = false, length = 1000)
    private String descrizione;

    /**
     * Campo relativo al luogo dell'evento
     * è chiave esterna di Indirizzo
     **/
    @NotNull(message = "Il luogo dell'evento è obbligatorio")
    @ManyToOne
    @JoinColumn(name = "luogo", referencedColumnName = "id")
    private Indirizzo luogo;

    /**
     * Campo relativo all'organizzatore dell'evento
     * è chiave esterna di Volontario
     **/
    @NotNull(message = "L'organizzatore dell'evento è obbligatorio")
    @ManyToOne
    @JoinColumn(name = "organizzatore", referencedColumnName = "email")
    private Volontario organizzatore;

    /**
     * Campo relativo al numero massimo di partecipanti dell'evento
     **/
    @Min(value = 0, message = "Il numero massimo di partecipanti non può essere negativo")
    @NotNull(message = "Il numero massimo di partecipanti è obbligatorio")
    @Min(value = 1, message = "Deve esserci almeno un partecipante")
    @Column(name = "maxPartecipanti", nullable = false)
    private int maxPartecipanti;

    /**
     * Campo relativo alla lista di partecipanti dell'evento
     * è chiave esterna di Rifugiato
     **/
    @JsonIgnore //Ignora la proprietà listaPartecipanti quando viene serializzata in JSON
    @ManyToMany
    @JoinTable(name = "evento_lista_partecipanti",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "rifugiato_email"))
    private List<Rifugiato> listaPartecipanti;

    /**
     * Costruttore vuoto
     **/
    public Evento() {}
}
