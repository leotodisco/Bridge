package com.project.bridgebackend.Model.Entity;

/**
 * @author Vito Vernellati
 * @created 04/12/2024
 * @version 1.0
 * Entità per la gestione degli annunci di lavoro
 */

import jakarta.persistence.*;
import lombok.*;
import javax.validation.constraints.*;
import com.project.bridgebackend.Model.Entity.enumeration.*;

/**
 * Classe che rappresenta un annuncio di lavoro, estende la classe base Annuncio.
 */
@Entity
@Getter
@Setter
@Table(name = "lavoro")
@Inheritance(strategy = InheritanceType.JOINED)
public class Lavoro extends Annuncio {

    /**
     * La posizione lavorativa richiesta nell'annuncio.
     */
    @Column(name = "posizione_lavorativa", nullable = false, length = 100)
    @NotBlank(message = "La posizione lavorativa è obbligatoria")
    @Pattern(regexp = "^[A-zÀ-ù ‘]{2,100}$", message = "Formato posizione lavorativa non valido ")
    @Size(max = 100, message = "La posizione lavorativa non può superare i 100 caratteri ")
    private String posizioneLavorativa;

    /**
     * Il nome dell'azienda che offre il lavoro.
     */
    @Column(name = "nome_azienda", nullable = false, length = 100)
    @NotBlank(message = "Il nome dell'azienda è obbligatorio")
    @Pattern(regexp = "^[A-zÀ-ù0-9 ‘]{2,100}$", message = "Formato nome azienda non valido ")
    @Size(max = 100, message = "Il nome dell'azienda non può superare i 100 caratteri ")
    private String nomeAzienda;

    /**
     * L'orario di lavoro specificato nell'annuncio (ad esempio, 09:00-17:00).
     */
    @Column(name = "orario_lavoro", nullable = false, length = 50)
    @NotBlank(message = "L'orario di lavoro è obbligatorio")
    @Pattern(regexp = "^\\d{2}:\\d{2}-d{2}:\\d{2}$", message = "Formato orario di lavoro non valido ")
    private String orarioLavoro;

    /**
     * Il tipo di contratto offerto.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contratto", nullable = false, length = 50)
    @NotNull(message = "Il tipo di contratto è obbligatorio")
    @Pattern(regexp = "^[A-zÀ-ù0-9 ‘]{2,50}$", message = "Formato tipo di contratto non valido ")
    @Size(max = 50, message = "Il tipo di contratto non può superare i 50 caratteri ")
    private TipoContratto tipoContratto;

    /**
     * La retribuzione offerta per la posizione.
     */
    @Column(name = "retribuzione", nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "La retribuzione deve essere positiva")
    @Pattern(regexp = "^\\d{1,8}(\\.\\d{2})?$", message = "Formato retribuzione non valido ")
    private double retribuzione;

    /**
     * Il nome della sede di lavoro.
     */
    @Column(name = "nome_sede", nullable = false, length = 100)
    @NotBlank(message = "Il nome della sede è obbligatorio")
    @Pattern(regexp = "^[A-zÀ-ù ‘]{2,100}$", message = "Formato nome sede non valido ")
    @Size(max = 100, message = "Il nome della sede non può superare i 100 caratteri ")
    private String nomeSede;

    /**
     * Informazioni aggiuntive utili per il lavoro.
     */
    @Column(name = "info_utili", nullable = false, length = 500)
    @NotBlank(message = "Le info utili sono obbligatorie")
    @Pattern(regexp = "^[\\wÀ-ÿ\s,.!?\'\\-]{1,500}$", message = "Formato info utili non valido ")
    @Size(max = 500, message = "Le info utili non possono superare i 500 caratteri ")
    private String infoUtili;

    /**
     * Costruttore completo.
     */
    public Lavoro(String posizioneLavorativa, String nomeAzienda, String orarioLavoro, TipoContratto tipoContratto,
                  double retribuzione, String nomeSede, String infoUtili) {
        // Inizializza i campi ereditati da Annuncio
        super();

        // Inizializza i campi specifici di Lavoro
        this.posizioneLavorativa = posizioneLavorativa;
        this.nomeAzienda = nomeAzienda;
        this.orarioLavoro = orarioLavoro;
        this.tipoContratto = tipoContratto;
        this.retribuzione = retribuzione;
        this.nomeSede = nomeSede;
        this.infoUtili = infoUtili;
    }
}
