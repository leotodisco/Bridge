package com.project.bridgebackend.ModuloFiaUtil;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnunciLavoroCSV {

    @CsvBindByName(column = "\uFEFFTitoloo")
    private String titolo;

    @CsvBindByName(column = "Disponibilita")
    private Boolean disponibilita;

    @CsvBindByName(column = "indirizzo")
    private String indirizzo;

    @CsvBindByName(column = "Numero_candidature")
    private Integer numeroCandidature;

    @CsvBindByName(column = "Lista_candidati")
    private String listaCandidati; // Lista vuota per ora, potresti volerla deserializzare in una lista

    @CsvBindByName(column = "Tipologia")
    private Boolean tipologia;

    @CsvBindByName(column = "Posizione_Lavorativa")
    private String posizioneLavorativa;

    @CsvBindByName(column = "Nome_Azienda")
    private String nomeAzienda;

    @CsvBindByName(column = "Orario_lavoro")
    private String orarioLavoro;

    @CsvBindByName(column = "Tipo_contratto")
    private String tipoContratto;

    @CsvBindByName(column = "Retribuzione")
    private Double retribuzione;

    @CsvBindByName(column = "Nome_sede")
    private String nomeSede;

    @CsvBindByName(column = "Info_Utili")
    private String infoUtili;

    @CsvBindByName(column = "proprietario_email")
    private String proprietarioEmail;

}
