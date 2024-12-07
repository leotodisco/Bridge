package com.project.bridgebackend.Model.Entity.enumeration;
/**
 * @author Antonio Ceruso.
 * Creato il 03/12/2024.
 * Questa è l'enumeratore relativa al Titolo di Studio dell'Utente.
 * Il campo è displayTitoloStudio.
 */
public enum TitoloDiStudio {
    /**
     * Titolo di studio che rappresenta la Scuola Primaria.
     */
    ScuolaPrimaria("Scuola Primaria"),

    /**
     * Titolo di studio che rappresenta la Scuola Secondaria di Primo Grado.
     */
    ScuolaSecondariaPrimoGrado("Scuola Secondaria Primo Grado"),

    /**
     * Titolo di studio che rappresenta la Scuola Secondaria di Secondo Grado.
     */
    ScuolaSecondariaSecondoGrado("Scuola Secondaria Secondo Grado"),

    /**
     * Titolo di studio che rappresenta la Laurea.
     */
    Laurea("Laurea"),

    /**
     * Titolo di studio che rappresenta una Specializzazione post-laurea.
     */
    Specializzazione("Specializzazione");

    /**
     * Campo relativo al nome del titolo di studio che verrà mostrata.
     */
    private String displayTitoloStudio;
    /**
     *
     * @param displayTitoloStudio rappresenta il nome del titolo di studio che verrà mostrato.
     * Costruttore dell'enumeratore TitoloDiStudio.
     */
    TitoloDiStudio(final String displayTitoloStudio) {
        this.displayTitoloStudio = displayTitoloStudio;
    }

    /**
     *
     * @return displayTitoloStudio.
     * metodo che resituisce il titolo di studio.
     */
    public String getDisplayTitoloStudio() {
        return displayTitoloStudio;
    }
}
