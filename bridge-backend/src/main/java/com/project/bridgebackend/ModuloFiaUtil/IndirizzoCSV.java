package com.project.bridgebackend.ModuloFiaUtil;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class IndirizzoCSV {

    // Getters and setters
    @CsvBindByName(column = "citta")
    private String citta;

    @CsvBindByName(column = "provincia")
    private String provincia;

    @CsvBindByName(column = "via")
    private String via;

    @CsvBindByName(column = "numero_civico")
    private int numCivico;

    @CsvBindByName(column = "cap")
    private String cap;

    @CsvBindByName(column = "latitudine")
    private Double latitudine;

    @CsvBindByName(column = "longitudine")
    private Double longitudine;

}
