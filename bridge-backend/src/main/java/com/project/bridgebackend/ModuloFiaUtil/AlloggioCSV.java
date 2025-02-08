package com.project.bridgebackend.ModuloFiaUtil;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlloggioCSV {
    @CsvBindByName(column = "\uFEFFtitolo")
    private String titolo;

    @CsvBindByName(column = "descrizione")
    private String descrizione;

    @CsvBindByName(column = "indirizzo")
    private String indirizzo;

    @CsvBindByName(column = "max_persone")
    private int maxPersone;

    @CsvBindByName(column = "metratura")
    private int metratura;

    @CsvBindByName(column = "proprietario")
    private String proprietario;

    @CsvBindByName(column = "servizi")
    private String servizi;

    @CsvBindByName(column = "foto_url")
    private String fotoUrl;
}
