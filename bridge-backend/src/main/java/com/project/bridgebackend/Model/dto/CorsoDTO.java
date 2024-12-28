/**
 * Questo è il package che contiene i DTO per la gestione dei corsi.
 */
package com.project.bridgebackend.Model.dto;


import com.project.bridgebackend.Model.Entity.enumeration.CategoriaCorso;
import com.project.bridgebackend.Model.Entity.enumeration.Lingua;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "La descrizione del corso non può essere vuota.")
    private String descrizione;

    /**
     * Categoria del corso.
     */
    @NotNull(message = "La categoria del corso è obbligatoria.")
    private CategoriaCorso categoria;

    /**
     * Titolo del corso.
     */
    @NotBlank(message = "Il titolo del corso è obbligatorio.")
    private String titolo;

    /**
     * PDF del corso.
     */
    @NotBlank(message = "Il PDF del corso è obbligatorio.")
    private String pdf;

    /**
     * Lingua del corso.
     */
    @NotNull(message = "La lingua del corso è obbligatoria.")
    private Lingua lingua;

    /**
    * Email del proprietario del corso.
     */
    @NotBlank(message = "L'email del proprietario è obbligatoria.")
    @Email(message = "L'email del proprietario non è valida.")
    private String proprietario;
}
