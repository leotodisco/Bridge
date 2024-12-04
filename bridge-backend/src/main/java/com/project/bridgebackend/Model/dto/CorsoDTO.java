package com.project.bridgebackend.Model.dto;


import lombok.Data;

/**
 * @author Biagio Gallo.
 * Creato il: 04/12/2024.
 *
 */

@Data
public class CorsoDTO {

    /**
     * Descrizione del corso. Il campo è obbligatorio e può contenere fino a 500 caratteri.
     */
    private String descrizione;

    /**
     * Categoria del corso. Indica la categoria del corso.
     * È un campo obbligatorio con una lunghezza massima di 50 caratteri.
     */
    private String categoriaCorso;

    /**
     * Titolo del corso. Rappresenta il nome del corso.
     */
    private String titolo;

    /**
     * PDF del corso. Contiene i dati in formato binario relativi al documento del corso.
     */
    private byte[] pdf;

    /**
     * Lingua del corso. Indica la lingua in cui è scritto il corso.
     */
    private String lingua;
}
