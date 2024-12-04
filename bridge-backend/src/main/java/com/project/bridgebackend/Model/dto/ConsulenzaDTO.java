package com.project.bridgebackend.Model.dto;

import com.project.bridgebackend.Model.Entity.enumeration.TipoConsulenza;

import javax.validation.constraints.*;

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
    private Long proprietarioId;

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

    public Boolean getDisponibilita() {
        return disponibilita;
    }

    public void setDisponibilita(Boolean disponibilita) {
        this.disponibilita = disponibilita;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public Boolean getTipologia() {
        return tipologia;
    }

    public void setTipologia(Boolean tipologia) {
        this.tipologia = tipologia;
    }

    public Long getIndirizzoId() {
        return indirizzoId;
    }

    public void setIndirizzoId(Long indirizzoId) {
        this.indirizzoId = indirizzoId;
    }

    public Long getProprietarioId() {
        return proprietarioId;
    }

    public void setProprietarioId(Long proprietarioId) {
        this.proprietarioId = proprietarioId;
    }

    public Integer getMaxCandidature() {
        return maxCandidature;
    }

    public void setMaxCandidature(Integer maxCandidature) {
        this.maxCandidature = maxCandidature;
    }

    public TipoConsulenza getTipo() {
        return tipo;
    }

    public void setTipo(TipoConsulenza tipo) {
        this.tipo = tipo;
    }

    public String getOrariDisponibili() {
        return orariDisponibili;
    }

    public void setOrariDisponibili(String orariDisponibili) {
        this.orariDisponibili = orariDisponibili;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    // Getters e Setters
    // ...
}
