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
public class Lavoro extends Annuncio {

    @Column(name = "posizione_lavorativa", nullable = false, length = 100)
    @NotBlank(message = "La posizione lavorativa è obbligatoria")
    @Size(max = 100, message = "La posizione lavorativa non può superare i 100 caratteri")
    private String posizioneLavorativa;

    @Column(name = "nome_azienda", nullable = false, length = 100)
    @NotBlank(message = "Il nome dell'azienda è obbligatorio")
    @Size(max = 100, message = "Il nome dell'azienda non può superare i 100 caratteri")
    private String nomeAzienda;

    @Column(name = "orario_lavoro", nullable = false, length = 50)
    @NotBlank(message = "L'orario di lavoro è obbligatorio")
    @Size(max = 50, message = "L'orario di lavoro non può superare i 50 caratteri")
    private String orarioLavoro;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contratto", nullable = false, length = 50)
    @NotNull(message = "Il tipo di contratto è obbligatorio")
    private TipoContratto tipoContratto;

    @Column(name = "retribuzione", nullable = false, precision = 10)
    @DecimalMin(value = "0.0", inclusive = false, message = "La retribuzione deve essere positiva")
    private double retribuzione;

    @Column(name = "nome_sede", nullable = false, length = 100)
    @NotBlank(message = "Il nome della sede è obbligatorio")
    @Size(max = 100, message = "Il nome della sede non può superare i 100 caratteri")
    private String nomeSede;

    @Column(name = "info_utili", nullable = false, length = 500)
    @NotBlank(message = "Le info utili sono obbligatorie")
    @Size(max = 500, message = "Le info utili non possono superare i 500 caratteri")
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

    public Lavoro() {
    }
}
