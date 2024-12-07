package com.project.bridgebackend.Model.dto;

import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AlloggioDTO {

    /**
     *Parametro metratura della casa
     */
    private int metratura;

    /**
     *Parametro massimo persone possibili nell'alloggio
     */
    private int maxPersone;

    /**
     *Parametro descrizione della casa
     */
    private String descrizione;

    /**
     *Parametro lista di rifugiati che si candidano per un alloggio
     */
    private List<Rifugiato> listaCandidati;

    /**
     *Parametro per le foto
     */
    private List<Byte> byteList;
}
