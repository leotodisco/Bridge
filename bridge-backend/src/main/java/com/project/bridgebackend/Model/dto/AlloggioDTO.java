package com.project.bridgebackend.Model.dto;

import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.enumeration.Servizi;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object (DTO) per rappresentare gli alloggi.
 * Utilizzato per il trasferimento dei dati relativi agli alloggi tra i livelli
 * del sistema. Include la validazione dei dati per garantire che siano
 * conformi alle regole definite.
 */
@Data
public class AlloggioDTO {

    /**
     * Descrizione dell'alloggio. Deve essere lunga al massimo 500 caratteri.
     */
    @Size(max = 500, message = "La descrizione non può superare i 500 caratteri")
    private String descrizione;

    /**
     * Numero massimo di persone che l'alloggio può ospitare. Deve essere un valore positivo.
     */
    @NotNull(message = "Il numero di posti è obbligatorio")
    @Positive(message = "Il numero di posti deve essere un valore positivo")
    private Integer maxPersone;

    /**
     * La metratura dell'alloggio. Deve essere un valore positivo.
     */
    @NotNull(message = "La metratura è obbligatoria")
    @Positive(message = "Il numero della metratura deve essere un valore positivo")
    private Integer metratura;

    /**
     * Lista di foto dell'alloggio. Può contenere più URL o stringhe base64 delle immagini.
     */
    private List<String> foto;

    /**
     * Email del proprietario dell'alloggio. Deve essere un'email valida.
     */
    @NotBlank(message = "L'email del proprietario è obbligatoria")
    @Email(message = "L'email del proprietario deve essere valida")
    private String emailProprietario;

    /**
     * Servizi offerti dall'alloggio. Deve essere un valore non nullo.
     */
    @NotNull(message = "Servizi obbligatori")
    private Servizi servizi;

    /**
     * Titolo dell'alloggio. Deve essere un valore non nullo.
     */
    @NotNull(message = "Il titolo non può essere vuoto")
    private String titolo;

    /**
     * Indirizzo dell'alloggio. Deve essere un valore non nullo.
     */
    @NotNull(message = "l'indirizzo non può essere nullo")
    private Indirizzo indirizzo;

    /**
     * Costruttore vuoto per la creazione di un'istanza di AlloggioDTO.
     */
    public AlloggioDTO() {
    }

    // Getters e Setters
}
