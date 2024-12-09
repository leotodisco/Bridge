package com.project.bridgebackend.Model.dto;

import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.enumeration.TipoContratto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

import java.util.List;

/**
 * DTO (Data Transfer Object) per la gestione dei dati di un annuncio di lavoro.
 * Questo oggetto serve a trasferire i dati tra i livelli del sistema, mantenendo
 * una separazione chiara tra le entità del database e i dati ricevuti/inviati tramite API.
 *
 * In questo DTO vengono applicate regole di validazione per garantire la consistenza
 * dei dati forniti.
 *
 * @author Vito Vernellati
 * @created 08/12/2024
 * @version 1.0
 */

@Data
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LavoroDTO {

    /** Campi di annuncio */

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
     * <p>Nota: Questo campo potrebbe non essere necessario, da valutare.</p>
     */
    @NotNull(message = "La tipologia è obbligatoria (true o false)")
    private Boolean tipologia;

    /**
     * Indirizzo di riferimento per l'annuncio.
     * Campo obbligatorio e validato.
     */
    @Valid
    @NotNull(message = "Avere un indirizzo di riferimento è obbligatorio")
    private Indirizzo indirizzo;

    /**
     * Proprietario dell'annuncio.
     * Campo obbligatorio e validato.
     */
    @NotBlank(message = "Il proprietario è obbligatorio")
    private String proprietario;

    /**
     * Numero massimo di candidature accettate per l'annuncio.
     * Campo obbligatorio.
     */
    @NotNull(message = "Il numero massimo di candidature è obbligatorio")
    @Min(value = 1, message = "Il numero massimo di candidature deve essere almeno 1")
    private Integer maxCandidature;

    /**
     * Lista dei candidati che hanno applicato all'annuncio.
     * Ogni email nella lista deve essere non vuota e avere un formato valido.
     */
    private List<@NotBlank(message = "L'email del candidato non può essere vuota")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Email del candidato non valida")
            String> candidati;

    /** Campi specifici di Lavoro */

    /**
     * Posizione lavorativa richiesta nell'annuncio.
     * Deve essere una stringa non vuota, di lunghezza compresa tra 2 e 100 caratteri.
     */
    @NotBlank(message = "La posizione lavorativa è obbligatoria")
    @Pattern(regexp = "^[A-zÀ-ù ‘]{2,100}$", message = "Formato posizione lavorativa non valido")
    @Size(max = 100, message = "La posizione lavorativa non può superare i 100 caratteri")
    private String posizioneLavorativa;

    /**
     * Il nome dell'azienda che offre il lavoro.
     * Deve essere una stringa non vuota, di lunghezza compresa tra 2 e 100 caratteri.
     */
    @NotBlank(message = "Il nome dell'azienda è obbligatorio")
    @Pattern(regexp = "^[A-zÀ-ù0-9 ‘]{2,100}$", message = "Formato nome azienda non valido")
    @Size(max = 100, message = "Il nome dell'azienda non può superare i 100 caratteri")
    private String nomeAzienda;

    /**
     * L'orario di lavoro specificato nell'annuncio (ad esempio, 09:00-17:00).
     * Deve seguire il formato 'HH:mm-HH:mm'.
     */
    @NotBlank(message = "L'orario di lavoro è obbligatorio")
    @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", message = "Formato orario di lavoro non valido")
    private String orarioLavoro;

    /**
     * Il tipo di contratto offerto.
     * Deve essere un valore valido della enumerazione {@link TipoContratto}.
     * Obbligatorio.
     */
    @NotNull(message = "Il tipo di contratto è obbligatorio")
    private TipoContratto tipoContratto;

    /**
     * La retribuzione offerta per la posizione.
     * Deve essere un valore positivo.
     */
    @NotNull(message = "La retribuzione è obbligatoria")
    @DecimalMin(value = "0.0", inclusive = false, message = "La retribuzione deve essere positiva")
    private Double retribuzione;

    /**
     * Il nome della sede di lavoro.
     * Deve essere una stringa non vuota, di lunghezza compresa tra 2 e 100 caratteri.
     */
    @NotBlank(message = "Il nome della sede è obbligatorio")
    @Pattern(regexp = "^[A-zÀ-ù ‘]{2,100}$", message = "Formato nome sede non valido")
    @Size(max = 100, message = "Il nome della sede non può superare i 100 caratteri")
    private String nomeSede;

    /**
     * Informazioni aggiuntive utili per il lavoro.
     * Deve essere una stringa non vuota con lunghezza massima di 500 caratteri.
     */
    @NotBlank(message = "Le info utili sono obbligatorie")
    @Pattern(regexp = "^[\\wÀ-ÿ\\s,.!?\\'\\-]{1,500}$", message = "Formato info utili non valido")
    @Size(max = 500, message = "Le info utili non possono superare i 500 caratteri")
    private String infoUtili;
}
