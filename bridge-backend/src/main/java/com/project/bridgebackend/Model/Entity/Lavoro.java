package com.project.bridgebackend.Model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.project.bridgebackend.Model.Entity.enumeration.TipoContratto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Classe che rappresenta un annuncio di lavoro, estende la classe base Annuncio.
 *
 * @author Vito Vernellati.
 * Creato il 04/12/2024.
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
    @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", message = "Formato orario di lavoro non valido ")
    private String orarioLavoro;

    /**
     * Il tipo di contratto offerto.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contratto", nullable = false, length = 50)
    @NotNull(message = "Il tipo di contratto è obbligatorio")
    private TipoContratto tipoContratto;


    /**
     * La retribuzione offerta per la posizione.
     */
    @Column(name = "retribuzione", nullable = false)
    @DecimalMin(value = "0.01", inclusive = true, message = "La retribuzione deve essere positiva e maggiore di 0")
    @NotNull(message = "La retribuzione è obbligatoria")
    private Double retribuzione;

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
    @Pattern(regexp = "^[\\wÀ-ÿ\\s,.!?@\\'\\-]{1,500}$", message = "Formato info utili non valido ")
    @Size(max = 500, message = "Le info utili non possono superare i 500 caratteri ")
    private String infoUtili;


    /**
     * Lista di candidati (di tipo Rifugiato) che hanno applicato per l'annuncio.
     * È una relazione @OneToMany, in cui un annuncio può essere associato a più candidati.
     * La relazione è mappata dall'attributo annuncio nell'entità Rifugiato.
     */
    @ElementCollection
    @CollectionTable(
            name = "lavoro_rifugiati",
            joinColumns = @JoinColumn(name = "lavoro_id")
    )
    @Column(name = "rifugiato_email", nullable = false)
    private List<String> candidati;

    /**
     * Costruttore completo.
     *
     * @param posizioneLavorativa la posizione lavorativa richiesta nell'annuncio
     * @param nomeAzienda il nome dell'azienda che offre il lavoro
     * @param orarioLavoro l'orario di lavoro specificato nell'annuncio
     * @param tipoContratto il tipo di contratto offerto
     * @param retribuzione la retribuzione offerta per la posizione
     * @param nomeSede il nome della sede di lavoro
     * @param infoUtili informazioni aggiuntive utili per il lavoro
     */
    public Lavoro(final String posizioneLavorativa, final String nomeAzienda, final String orarioLavoro, final TipoContratto tipoContratto,
                  final double retribuzione, final String nomeSede, final String infoUtili) {
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

    /**
     * Costruttore di default.
     */
    public Lavoro() {
        super();
    }
}
