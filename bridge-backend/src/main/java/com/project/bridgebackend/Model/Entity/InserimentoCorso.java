package com.project.bridgebackend.Model.Entity;

import jakarta.persistence.*;
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
public class InserimentoCorso implements Serializable {

    /**
     * L'ID viene generato automaticamente. È un valore univoco per ogni corso.
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Descrizione del corso. Il campo è obbligatorio e può contenere fino a 500 caratteri.
     */
    @Column(name = "descrizione", nullable = false, length = 500)
    private String descrizione;

    /**
     * Categoria del corso. Indica la categoria o il tipo del corso.
     * È un campo obbligatorio con una lunghezza massima di 50 caratteri.
     */
    @Column(name = "categoriaCorso", nullable = false, length = 50)
    private String categoriaCorso;


    /**
     * Titolo del corso. Rappresenta il nome del corso.
     * È un campo obbligatorio con una lunghezza massima di 50 caratteri.
     */
    @Column(name = "titolo", nullable = false, length = 50)
    private String titolo;


    /**
     * PDF del corso. Contiene i dati in formato binario relativi al documento del corso.
     * È un campo obbligatorio.
     */
    @Column(name = "pdf", nullable = false)
    private byte[] pdf;

    /**
     * Lingua del corso. È un campo che rappresenta la lingua in cui è disponibile il corso.
     * Utilizza un enum che può contenere valori come ITALIANO, INGLESE, TEDESCO, FRANCESE, SPAGNOLO.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "lingua", nullable = false)
    private Lingua lingua;


    /**
     * Proprietario del corso. Indica l'utente che ha creato o possiede il corso.
     * La relazione è ManyToOne, poiché un utente può possedere molti corsi.
     * È un campo obbligatorio.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario", nullable = false)
    private Utente proprietario;

}

