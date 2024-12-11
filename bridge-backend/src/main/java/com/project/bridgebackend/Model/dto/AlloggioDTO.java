package com.project.bridgebackend.Model.dto;

import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.Entity.enumeration.Servizi;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Data Transfer Object per la gestione degli alloggi, con validazione
 */
@Data
public class AlloggioDTO {

    private Long id; // Identificativo univoco dell'alloggio (opzionale per la creazione)

    @Size(max = 500, message = "La descrizione non può superare i 500 caratteri")
    private String descrizione;

    @NotNull(message = "Il numero di posti è obbligatorio")
    @Positive(message = "Il numero di posti deve essere un valore positivo")
    private Integer maxPersone;

    @NotNull(message = "La metratura è obbligatoria")
    @Positive(message = "Il numero della metratura deve essere un valore positivo")
    private Integer metratura;

    private List<String> fotos;

    @NotBlank(message = "L'email del proprietario è obbligatoria")
    @Email(message = "L'email del proprietario deve essere valida")
    private String emailProprietario;

    @NotNull(message = "Servizi obbligatori")
    private Servizi servizi;

    // Getters e Setters
}
