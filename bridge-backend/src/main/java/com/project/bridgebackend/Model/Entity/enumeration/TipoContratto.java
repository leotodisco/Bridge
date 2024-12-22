/**
 * Pacchetto che contiene le enumerazioni utilizzate nel progetto Bridge Backend.
 */
package com.project.bridgebackend.Model.Entity.enumeration;

/**
 * Enumerazione per i tipi di contratto.
 * @author Vito Vernellati
 * Creato in data 04/12/2024
 * @version 1.0
 */
public enum TipoContratto {
    /**
     * Contratto a tempo pieno.
     */
    FULL_TIME("Full Time"),

    /**
     * Contratto a tempo parziale.
     */
    PART_TIME("Part Time"),

    /**
     * Contratto di apprendistato.
     */
    APPRENDISTATO("Apprendistato"),

    /**
     * Stage o tirocinio.
     */
    INTERNSHIP("Stage/Tirocinio"),

    /**
     * Contratto a progetto.
     */
    PROGETTO("Contratto a Progetto"),

    /**
     * Collaborazione.
     */
    COLLABORAZIONE("Collaborazione"),

    /**
     * Cooperativa.
     */
    COOPERATIVA("Cooperativa"),

    /**
     * Altro tipo di contratto.
     */
    ALTRO("Altro");

    /**
     * Descrizione del tipo di contratto.
     */
    private final String description;

    /**
     * Costruttore per l'enum con descrizione.
     *
     * @param description Descrizione leggibile del tipo di contratto.
     */
    TipoContratto(final String description) {
        this.description = description;
    }

    /**
     * Ottiene la descrizione del tipo di contratto.
     *
     * @return Descrizione leggibile.
     */
    public String getDescription() {
        return description;
    }
}
