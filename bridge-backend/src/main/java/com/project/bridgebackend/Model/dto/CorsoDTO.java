/**
 * Questo è il package che contiene i DTO per la gestione dei corsi.
 */
package com.project.bridgebackend.Model.dto;


import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Utente;
import com.project.bridgebackend.Model.Entity.enumeration.CategoriaCorso;
import com.project.bridgebackend.Model.Entity.enumeration.Lingua;
import lombok.Data;

/**
 * @author Biagio Gallo.
 * Creato il: 04/12/2024.
 *
 */

@Data
public class CorsoDTO {

    /**
     * Descrizione del corso.
     */
    private String descrizione;

    /**
     * Categoria del corso.
     */
    private CategoriaCorso categoriaCorso;

    /**
     * Titolo del corso.
     */
    private String titolo;

    /**
     * PDF del corso.
     */
    private byte[] pdf;

    /**
     * Lingua del corso.
     */
    private Lingua lingua;

    /**
    * Email del proprietario del corso.
     */
    private FiguraSpecializzata proprietario;
}
