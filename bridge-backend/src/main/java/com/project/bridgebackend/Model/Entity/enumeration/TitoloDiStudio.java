package com.project.bridgebackend.Model.Entity.enumeration;

public enum TitoloDiStudio {
    ScuolaPrimaria("Scuola Primaria"),
    ScuolaSecondariaPrimoGrado("Scuola Secondaria Primo Grado"),
    ScuolaSecondariaSecondoGrado("Scuola Secondaria Secondo Grado"),
    Laurea("Laurea"),
    Specializzazione("Specializzazione");

    private String displayMsg;
    private TitoloDiStudio(String displayMsg) {this.displayMsg = displayMsg;}

    public String getDisplayMsg() {return displayMsg;}
}
