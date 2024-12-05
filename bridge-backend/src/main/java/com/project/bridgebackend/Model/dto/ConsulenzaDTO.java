package com.project.bridgebackend.Model.dto;

import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.enumeration.TipoConsulenza;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

/**
 * @author Geraldine Montella.
 * Creato il: 05/12/2024.

 * DTO (Data Transfer Object) per la gestione dei dati di una consulenza.
 * Questo oggetto serve a trasferire i dati tra i livelli del sistema, mantenendo
 * una separazione chiara tra le entità del database e i dati ricevuti/inviati tramite API.

 * In questo DTO vengono applicate regole di validazione per garantire la consistenza
 * dei dati forniti.
 */

@Data
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ConsulenzaDTO {
    /** Campi di annuncio
    /**
     * Titolo dell'annuncio.
     * Deve essere una stringa non vuota, di lunghezza compresa tra 5 e 100 caratteri.
     */
    @NotBlank(message = "Il titolo non può essere vuoto")
    @Size(min = 5, max = 100, message = "Il titolo deve essere tra 5 e 100 caratteri")
    private String titolo;

    /**
     * Disponibilità del servizio.
     * Indica se il servizio è attualmente disponibile (true) o meno (false).
     * Obbligatorio.
     */
    @NotNull(message = "La disponibilità è obbligatoria")
    private Boolean disponibilita;

    /**
     * Tipologia dell'annuncio.
     * True per indicare una consulenza, false per indicare un lavoro.
     * todo: Questo campo potrebbe non essere necessario, da valutare.
     */
    @NotNull(message = "La tipologia è obbligatoria (true o false)")
    private Boolean tipologia;

    /**
     * Indirizzo di riferimento per la consulenza.
     * Campo obbligatorio e validato.
     */
    @Valid
    @NotNull(message = "avere un indirizzo di riferimento è obbligatorio")
    private Indirizzo indirizzo;

    /**
     * Indirizzo di riferimento per la consulenza.
     * Campo obbligatorio e validato.
     */
    @Valid
    @NotNull(message = "il proprietario è obbligatorio")
    private String proprietario;

    /**
     * Numero massimo di candidature accettate per l'annuncio.
     * Campo obbligatorio.
     */
    @NotNull(message = "Il numero massimo di candidature è obbligatorio")
    private Integer maxCandidature;

    /**
     * Lista dei candidati che hanno applicato all'annuncio.
     * Ogni email nella lista deve essere non vuota.
     */
    private List<@NotBlank(message = "l'email del candidato non può essere vuoto") String> candidati;

    /** Campi specifici di Consulenza
    /**
     * Tipo di consulenza offerta.
     * Deve essere un valore valido della enumerazione `TipoConsulenza`.
     */

    @NotNull(message = "Il tipo di consulenza è obbligatorio")
    private TipoConsulenza tipo;

    /**
     * Orari disponibili per la consulenza.
     * Formato previsto: 'Giorno hh:mm-hh:mm'. Esempio: 'Lunedì 10:00-12:00'.
     * Più fasce orarie possono essere separate da virgole.

     * todo: da rivedere se tale pattern è applicabile si potrebbe decidere di lasciare una semplice stringa
     */
    @Pattern(
            regexp = "^(\\w+\\s+\\d{2}:\\d{2}-\\d{2}:\\d{2})(,\\s*\\w+\\s+\\d{2}:\\d{2}-\\d{2}:\\d{2})*$",
            message = "Il formato degli orari deve essere 'Giorno hh:mm-hh:mm'. Esempio: 'Lunedì 10:00-12:00'"
    )
    private String orariDisponibili;

    /**
     * Descrizione dettagliata dell'annuncio.
     * Deve essere una stringa non vuota con lunghezza massima di 500 caratteri.
     */
    @NotBlank(message = "La descrizione non può essere vuota")
    @Min(2)
    @Size(max = 500, message = "la descrizione non può superare i 500 caratteri")
    private String descrizione;


    /**
     * Numero di telefono del proprietario dell'annuncio.
     * Deve essere nel formato italiano (+39 o 0039 opzionali).
     */
    @NotBlank(message = "Il numero di telefono non può essere vuoto")
    @Pattern(
            regexp = "^((00|\\+)39[\\. ]??)??3\\d{2}[\\. ]??\\d{6,7}$",
            message = "Numero di telefono non valido. Deve seguire il formato italiano."
    )
    private String numero;

}
