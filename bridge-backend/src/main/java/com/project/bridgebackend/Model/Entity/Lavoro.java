package com.project.bridgebackend.Model.Entity;

/**
 * @author Vito Vernellati
 * @created 04/12/2024
 * @version 1.0
 * Entità per la gestione degli annunci di lavoro
 */

import jakarta.persistence.*; // Per le annotazioni JPA
import javax.validation.constraints.*; // Per le annotazioni di validazione
import lombok.*; // Per Lombok
import com.project.bridgebackend.Model.Entity.enumeration.TipoContratto;
import com.project.bridgebackend.Model.Entity.Indirizzo;

@Entity
@Table(name = "Lavoro")
public class Lavoro {

    /**
     * ID incrementale (Genera automaticamente il valore della chiave primaria)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Posizione Lavorativa
     */
    @Column(nullable = false, length = 100)
    @NotBlank(message = "La posizione lavorativa è obbligatoria")
    @Size(max = 100, message = "La posizione lavorativa non può superare i 100 caratteri")
    private String posizioneLavorativa;

    /**
     * Nome Azienda
     */
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Il nome dell'azienda è obbligatorio")
    @Size(max = 100, message = "Il nome dell'azienda non può superare i 100 caratteri")
    private String nomeAzienda;

    /**
     * Orario di Lavoro
     */
    @Column(nullable = false, length = 50)
    @NotBlank(message = "L'orario di lavoro è obbligatorio")
    @Size(max = 50, message = "L'orario di lavoro non può superare i 50 caratteri")
    private String orarioLavoro;

    /**
     * Tipo di Contratto
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @NotNull(message = "Il tipo di contratto è obbligatorio")
    private TipoContratto tipoContratto;

    /**
     * Retribuzione
     */
    @Column(nullable = false, precision = 10, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false, message = "La retribuzione deve essere positiva")
    private double retribuzione;

    /**
     * Nome Sede
     */
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Il nome della sede è obbligatorio")
    @Size(max = 100, message = "Il nome della sede non può superare i 100 caratteri")
    private String nomeSede;

    /**
     * Info Utili
     */
    @Column(nullable = false, length = 500)
    @NotBlank(message = "Le info utili sono obbligatorie")
    @Size(max = 500, message = "Le info utili non possono superare i 500 caratteri")
    private String infoUtili;

    /**
     * Indirizzo
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "indirizzo_id", nullable = false)
    @NotNull(message = "L'indirizzo è obbligatorio")
    private Indirizzo indirizzo;

    /**
     * Costruttore
     */
    public Lavoro () {}
}
