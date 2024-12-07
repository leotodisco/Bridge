package com.project.bridgebackend.Model.dto;

import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.Entity.enumeration.Lingua;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Alessia De Filippo
 * Creato il: 04/12/2024
 * è la classe relativa all'DTO Evento
 * I campi sono: id, nome, data, ora,
 * lingueParlate, descrizione, luogo,
 * organizzatore, maxPartecipanti
 **/

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
public class EventoDTO {

    /**
     * Identificativo univoco dell'evento.
     * Rappresenta la chiave primaria dell'evento.
     **/
    private Long id;

    /**
     * Nome dell'evento.
     * Rappresenta il titolo dell'evento.
     **/
    private String nome;

    /**
     * Data dell'evento.
     * Rappresenta la data di svolgimento dell'evento.
     **/
    private String data;

    /**
     * Ora dell'evento.
     * Indica l'orario di inizio dell'evento.
     **/
    private LocalTime ora;

    /**
     * Lingue parlate durante l'evento.
     * Indica le lingue parlate durante l'evento.
     **/
    private List<Lingua> lingueParlate;

    /**
     * Descrizione dell'evento.
     * Rappresenta una breve descrizione dell'evento.
     **/
    private String descrizione;

    /**
     * Luogo dell'evento.
     * Indica il luogo in cui si svolgerà l'evento.
     **/
    private Indirizzo luogo;

    /**
     * Organizzatore dell'evento.
     * Indica il volontario che ha organizzato l'evento.
     **/
    private Volontario organizzatore;

    /**
     * Numero massimo di partecipanti all'evento.
     * Numero massimo di partecipanti all'evento.
     **/
    private int maxPartecipanti;

    //Costruttore vuoto
    public EventoDTO() {
    }
}
