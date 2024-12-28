package com.project.bridgebackend.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.bridgebackend.Model.Entity.enumeration.Lingua;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Alessia De Filippo.
 * Creato il: 03/12/2024.
 * È la classe relativa all'entità Evento.
 * I campi sono: id, nome, data, ora,
 * lingueParlate, descrizione, luogo,
 * organizzatore, maxPartecipanti e listaCandidati.
 **/

@Entity
@ToString
@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "evento")
public class Evento implements Serializable {

    /**
     * Costante lunghezza minima nome.
     */
    private static final int MIN_NAME_LENGTH = 3;

    /**
     * Costante lunghezza massima nome.
     */
    private static final int MAX_NAME_LENGTH = 100;

    /**
     * Costante lunghezza minima descrizione.
     */
    private static final int MIN_DESCRIPTION_LENGTH = 1;

    /**
     * Costante lunghezza massima descrizione.
     */
    private static final int MAX_DESCRIPTION_LENGTH = 1000;

    /**
     * Campo relativo all'id dell'evento.
     * Generato automaticamente.
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Campo relativo al nome dell'evento.
     **/
    @NotBlank(message = "Il nome dell'evento non può essere vuoto")
    @Pattern(regexp = "^[A-Za-z0-0-ÿ .,'-]{3,100}$",
            message = "Il nome contiene caratteri non validi")
    @Size(min = MIN_NAME_LENGTH,
          max = MAX_NAME_LENGTH,
          message = "Il nome deve essere tra 3 e 100 caratteri")
    @Column(name = "nome",
            nullable = false,
            length = MAX_NAME_LENGTH)
    private String nome;

    /**
     * Campo relativo alla data dell'evento.
     **/

    @NotNull(message = "La data dell'evento è obbligatoria")
    @FutureOrPresent(message = "La data non può essere nel passato")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data", nullable = false)
    private LocalDate data;

    /**
     * Campo relativo all'ora dell'evento.
     **/
    @NotNull(message = "L'ora è obbligatoria")
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "ora", nullable = false)
    private LocalTime ora;

    /**
     * Campo relativo alle lingue parlate durante l'evento.
     **/
    @NotNull(message = "La lingue sono obbligatorie")
    @Enumerated(EnumType.STRING)
    @Column(name = "linguaParlata", nullable = false)
    private Lingua linguaParlata;

    /**
     * Campo relativo alla descrizione dell'evento.
     **/
    @NotBlank(message = "La descrizione non può essere vuota")
    @Size(min = MIN_DESCRIPTION_LENGTH,
          max = MAX_DESCRIPTION_LENGTH,
          message = "La descrizione deve essere tra 1 e 1000 caratteri")
    @Column(name = "descrizione",
            nullable = false,
            length = MAX_DESCRIPTION_LENGTH)
    private String descrizione;

    /**
     * Campo relativo al luogo dell'evento.
     * È chiave esterna di Indirizzo.
     **/
    @NotNull(message = "Il luogo è obbligatorio")
    @ManyToOne
    @JoinColumn(name = "luogo", referencedColumnName = "id")
    private Indirizzo luogo;

    /**
     * Campo relativo all'organizzatore dell'evento.
     * È chiave esterna di Volontario.
     **/
    @NotNull(message = "L'organizzatore è obbligatorio")
    @ManyToOne
    @JoinColumn(name = "organizzatore", referencedColumnName = "email")
    private Volontario organizzatore;

    /**
     * Campo relativo al numero massimo di partecipanti dell'evento.
     **/
    @NotNull(message = "Il numero massimo di partecipanti è obbligatorio")
    @Positive(message = "Il numero massimo di partecipanti deve essere positivo")
    @Column(name = "maxPartecipanti",
            nullable = false)
    private int maxPartecipanti;

    /**
     * Campo relativo alla lista di partecipanti dell'evento.
     * è chiave esterna di Rifugiato.
     **/
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "evento_lista_partecipanti",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "rifugiato_email"))
    private List<Rifugiato> listaPartecipanti;

    /**
     * Costruttore vuoto.
     **/
    public Evento() {
    }
}
