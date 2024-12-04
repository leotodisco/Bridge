package com.project.bridgebackend.Model.Entity;

import com.project.bridgebackend.Model.Entity.enumeration.TipoConsulenza;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "consulenza")
public class Consulenza extends Annuncio {

    /*
    * Tipo identifica la tipologia di consulenza
    * che si vuole proporre
    * */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoConsulenza tipo;

    /*
    * Orari di disponibilità indicano la fascia oraria
    * in cui il consulente è disponbile
    * todo: to verify if string is a valid option
     */
    @Column(name = "orari_disponibili", nullable = false, length = 500)
    private String orariDisponibili;

    /*
    * descrizione è una stringa atta a discriminare come la
    * consulenza mira ad aiutare il rifugiato
    *
    *
    */
    @NotBlank(message = "la descrizione non può essere vuota")
    @Column(name = "descrizione", nullable = false, length = 1000)
    private String descrizione;

    @NotBlank(message = "Il numero non può essere vuoto")
    @Pattern(
            regexp = "^((00|\\+)39[\\. ]??)??3\\d{2}[\\. ]??\\d{6,7}$",
            message = "Numero di telefono non valido. Deve seguire il formato italiano."
    )
    @Column(name = "numero", nullable = false, length = 15)
    private String numero;

    public Consulenza() {
        super();
    }

    public Consulenza(Utente proprietario, Boolean tipologia, String titolo, Boolean disponibilita, Indirizzo indirizzo, int maxCandidature, List<Rifugiato> candidati, String orariDisponibili, String descrizione, String numero, TipoConsulenza tipo) {
        super(proprietario, tipologia, titolo, disponibilita, indirizzo, maxCandidature, candidati);
        this.orariDisponibili = orariDisponibili;
        this.descrizione = descrizione;
        this.numero = numero;
        this.tipo = tipo;
    }
}
