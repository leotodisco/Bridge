package com.project.bridgebackend.ModuloFiaUtil;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCSV {

    @CsvBindByName(column = "Email")
    private String email;

    @CsvBindByName(column = "Nome")
    private String nome;

    @CsvBindByName(column = "Cognome")
    private String cognome;

    @CsvBindByName(column = "Data di nascita")
    private String dataNascita;

    @CsvBindByName(column = "Nazionalità")
    private String nazionalita;

    @CsvBindByName(column = "Lingue parlate")
    private String lingueParlate;

    @CsvBindByName(column = "Genere")
    private String genere;

    @CsvBindByName(column = "Skill")
    private String skill;

    @CsvBindByName(column = "Titolo di studio")
    private String titoloDiStudio;

    @CsvBindByName(column = "embeddings_json")
    private String embeddingsJson;
}
