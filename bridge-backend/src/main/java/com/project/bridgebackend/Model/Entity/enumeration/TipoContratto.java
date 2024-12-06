package com.project.bridgebackend.Model.Entity.enumeration;

/**
 * Enumerazione per i tipi di contratto.
 */
public enum TipoContratto {
    FULL_TIME("Full Time"),
    PART_TIME("Part Time"),
    APPRENDISTATO("Apprendistato"),
    INTERNSHIP("Stage/Tirocinio"),
    PROGETTO("Contratto a Progetto"),
    COLLABORAZIONE("Collaborazione"),
    COOPERATIVA("Cooperativa"),
    ALTRO("Altro");

    private final String description;

    /**
     * Costruttore per l'enum con descrizione.
     *
     * @param description Descrizione leggibile del tipo di contratto.
     */
    TipoContratto(String description) {
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
