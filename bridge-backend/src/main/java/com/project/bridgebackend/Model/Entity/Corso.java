package com.project.bridgebackend.Model.Entity;

import com.project.bridgebackend.Model.Entity.enumeration.Lingua;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


/**
 * @author [Biagio]
 * Creato il: [03/12/2024].
 * Questa è la classe relativa ad un Corso.
 * I campi sono: id, descrizione, categoria del corso, titolo, pdf, lingua.
 */


@Entity
@ToString
@Getter
@Setter
@Table(name = "inserimento_corso")
public class Corso implements Serializable {

    /**
     * Lunghezza massima della descrizione del corso.
     */
    private static final int DESCRIZIONE_MAX_LENGTH = 500;

    /**
     * Lunghezza massima della categoria del corso.
     */
    private static final int CATEGORIA_CORSO_MAX_LENGTH = 50;

    /**
     * Lunghezza massima del titolo del corso.
     */
    private static final int TITOLO_MAX_LENGTH = 50;


    /**
     * L'ID viene generato automaticamente.
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Descrizione del corso.
     */
    @Column(name = "descrizione", nullable = false,
            length = DESCRIZIONE_MAX_LENGTH)
    private String descrizione;

    /**
     * Categoria del corso.
     */
    @Column(name = "categoria_corso", nullable = false,
            length = CATEGORIA_CORSO_MAX_LENGTH)
    private String categoriaCorso;


    /**
     * Titolo del corso.
     */
    @Column(name = "titolo", nullable = false, length = TITOLO_MAX_LENGTH)
    private String titolo;


    /**
     * PDF del corso.
     */
    @Column(name = "pdf", nullable = false)
    private byte[] pdf;

    /**
     * Lingua del corso.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "lingua", nullable = false)
    private Lingua lingua;


    /**
     * Proprietario del corso.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario", nullable = false)
    private Utente proprietario;

}

