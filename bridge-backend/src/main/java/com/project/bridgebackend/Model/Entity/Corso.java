package com.project.bridgebackend.Model.Entity;

import com.project.bridgebackend.Model.Entity.enumeration.Lingua;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


/**
 * @author Biagio Gallo.
 * Creato il: 03/12/2024.
 * Questa è la classe relativa a un Corso.
 * I campi sono: id, descrizione, categoria del corso,
 * titolo, pdf, lingua, volontario.
 */


@Entity
@ToString
@Getter
@Setter
@Table(name = "corso")
public class Corso implements Serializable {

    /**
     * Lunghezza massima di descrizione.
     */
    private static final int MAX_DESCRIZONE_LENGTH = 500;
    /**
     * Lunghezza massima del titolo.
     */
    private static final int MAX_TITOLO_LENGTH = 50;

    /**
     * L'ID viene generato automaticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Descrizione del corso.
     */
    @Column(name = "descrizione", nullable = false,
            length = MAX_DESCRIZONE_LENGTH)
    private String descrizione;

    /**
     * Categoria del corso.
     */
    @Column(name = "categoriaCorso", nullable = false)
    private String categoriaCorso;


    /**
     * Titolo del corso.
     */
    @Column(name = "titolo", nullable = false, length = MAX_TITOLO_LENGTH)
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
    private Volontario proprietario;

}

