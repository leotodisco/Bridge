package com.project.bridgebackend.Model.Entity;

import com.project.bridgebackend.Model.Entity.enumeration.CategoriaCorso;
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
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * @author [Biagio]
 * Creato il: [03/12/2024].
 * Classe che rappresenta un Corso.
 * I campi sono: id, descrizione, categoria del corso,
 * titolo, pdf, lingua, proprietario.
 */
@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "corso")
public class Corso implements Serializable {

    /**
     * Lunghezza massima della descrizione del corso.
     */
    private static final int DESCRIZIONE_MAX_LENGTH = 500;

    /**
     * Lunghezza massima del titolo del corso.
     */
    private static final int TITOLO_MAX_LENGTH = 50;

    /**
     * Lunghezza minima del titolo del corso.
     */
    private static final int TITOLO_MIN_LENGTH = 3;

    /**
     * Lunghezza minima della descrizione del corso.
     */
    private static final int DESCRIZIONE_MIN_LENGTH = 3;

    /**
     * ID univoco del corso.
     * Viene generato automaticamente al momento della persistenza.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Categoria a cui il corso appartiene,
     * rappresentato da un enum {@link CategoriaCorso}.
     * La categoria del corso descrive il tipo di contenuto o,
     * l'area disciplinare.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_corso")
    private CategoriaCorso categoriaCorso;

    /**
     * Descrizione del corso. Può contenere fino a 500 caratteri.
     * La descrizione fornisce una panoramica del contenuto del corso.
     */
    @Column(name = "descrizione", length = DESCRIZIONE_MAX_LENGTH)
    private String descrizione;

    /**
     * Lingua in cui è disponibile il corso,
     * rappresentata da un enum {@link Lingua}.
     * La lingua del corso è utilizzata per determinare la lingua,
     * di erogazione.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "lingua")
    private Lingua lingua;

    /**
     * Nome del file PDF associato al corso,
     * che potrebbe contenere il materiale didattico.
     * Il campo è opzionale.
     */
    @Column(name = "pdf")
    private String pdf;

    /**
     * Titolo del corso. Deve essere compreso tra 3 e 50 caratteri.
     * Il titolo è utilizzato per identificare il corso nel sistema.
     */
    @Column(name = "titolo", length = TITOLO_MAX_LENGTH)
    private String titolo;

    /**
     * Proprietario del corso,
     * rappresentato dall'entità {@link FiguraSpecializzata}.
     * Ogni corso ha un proprietario,
     * che può essere una figura specializzata nel settore.
     * Il campo è obbligatorio.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario")
    private FiguraSpecializzata proprietario;
}
