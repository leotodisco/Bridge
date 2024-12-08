package com.project.bridgebackend.Model.Entity;

import com.project.bridgebackend.Model.Entity.enumeration.CategoriaCorso;
import com.project.bridgebackend.Model.Entity.enumeration.Lingua;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;

/**
 * @author [Biagio]
 * Creato il: [03/12/2024].
 * Classe che rappresenta un Corso.
 * I campi sono: id, descrizione, categoria del corso, titolo, pdf, lingua, proprietario.
 */
@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "corso")
public class Corso implements Serializable {

    // Lunghezza massima della descrizione del corso
    private static final int DESCRIZIONE_MAX_LENGTH = 500;

    // Lunghezza massima del titolo del corso
    private static final int TITOLO_MAX_LENGTH = 50;

    //Lunghezza minima del titolo del corso
    private static final int TITOLO_MIN_LENGTH = 3;

    //Lunghezza minima della descrizione del corso
    private static final int DESCRIZIONE_MIN_LENGTH = 3;

    // ID del corso, generato automaticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Categoria del corso
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_corso")
    private CategoriaCorso categoriaCorso;

    // Descrizione del corso
    @Column(name = "descrizione", length = DESCRIZIONE_MAX_LENGTH)
    private String descrizione;

    // Lingua del corso
    @Enumerated(EnumType.STRING)
    @Column(name = "lingua")
    private Lingua lingua;

    // Nome del file PDF del corso
    @Column(name = "pdf")
    private byte[] pdf;

    // Titolo del corso
    @Column(name = "titolo", length = TITOLO_MAX_LENGTH)
    private String titolo;

    // Proprietario del corso
    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario")
    private FiguraSpecializzata proprietario;
}