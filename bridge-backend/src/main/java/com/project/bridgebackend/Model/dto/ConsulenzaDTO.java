package com.project.bridgebackend.Model.dto;

import com.project.bridgebackend.Model.Entity.enumeration.TipoConsulenza;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ConsulenzaDTO {

    // Campi di Annuncio
    @NotBlank(message = "Il titolo non può essere vuoto")
    @Min(5)
    @Size(max = 100, message = "Il titolo non può superare i 100 caratteri")
    private String titolo;

    @NotNull(message = "La disponibilità è obbligatoria")
    private Boolean disponibilita;

    @NotNull(message = "La tipologia è obbligatoria")
    private Boolean tipologia;

    @NotNull(message = "L'ID dell'indirizzo è obbligatorio")
    private Long indirizzoId;

    @NotNull(message = "L'ID del proprietario è obbligatorio")
    private String proprietario_email;

    @NotNull(message = "Il numero massimo di candidature è obbligatorio")
    private Integer maxCandidature;

    // Campi specifici di Consulenza
    @NotNull(message = "Il tipo di consulenza è obbligatorio")
    private TipoConsulenza tipo;

    @NotBlank(message = "Gli orari disponibili non possono essere vuoti")
    private String orariDisponibili;

    @NotBlank(message = "La descrizione non può essere vuota")
    @Min(2)
    @Size(max = 500, message = "la descrizione non può superare i 500 caratteri")
    private String descrizione;

    @NotBlank(message = "Il numero di telefono non può essere vuoto")
    @Pattern(
            regexp = "^((00|\\+)39[\\. ]??)??3\\d{2}[\\. ]??\\d{6,7}$",
            message = "Numero di telefono non valido. Deve seguire il formato italiano."
    )
    private String numero;

}
