package com.project.bridgebackend.Model.Entity;

import com.project.bridgebackend.Model.Entity.enumeration.TipoConsulenza;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Entity;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Map;

/**
 * @author Geraldine Montella.
 * Creato il: 03/12/2024.
 * Questa Java Persistence Entity per una consulenza,
 *
 * eredita altri attributi dall'entity Annuncio
 */
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Getter
@Setter
@Table(name = "consulenza")
@NoArgsConstructor
@AllArgsConstructor
public class Consulenza extends Annuncio {

    /**
    * Tipo identifica la tipologia di consulenza,
    * che si vuole proporre.
    * */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoConsulenza tipo;

    /**
    * Orari di disponibilità indicano la fascia oraria,
    * in cui il consulente è disponbile.
    * todo: to verify if string is a valid option
     */
    @Column(name = "orari_disponibili", nullable = false, length = 500)
    @NotBlank(message = "Gli orari di disponibilità non possono essere vuoti")
    @Pattern(
            regexp = "^(Lunedi|Martedi|Mercoledi|Giovedi|Venerdi|Sabato|Domenica)\\s+(0\\d|1\\d|2[0-3]):[0-5]\\d\\s*-\\s*(0\\d|1\\d|2[0-3]):[0-5]\\d$",
            message = "Formato orario non valido! Usa: <Giorno> HH:MM - HH:MM (es. Lunedi 09:30-10:45)"
    )
    private String orariDisponibili;

    /**
    * Descrizione è una stringa atta a discriminare come la,
    * consulenza mira ad aiutare il rifugiato.
    */
    @Column(name = "descrizione", nullable = false, length = 1000)
    private String descrizione;

    /**
     * Numero di telefono per contattare il consulente.
     * Valida il formato secondo il modello italiano.
     */
    @NotBlank(message = "Il numero non può essere vuoto")
    @Pattern(
            regexp = "^((00|\\+)39[\\. ]??)??3\\d{2}[\\. ]??\\d{6,7}$",
            message = "Numero di telefono non valido. Deve seguire il formato italiano."
    )
    @Column(name = "numero", nullable = false, length = 15)
    private String numero;

    /**
     * Lista di candidati (di tipo Rifugiato) che hanno applicato per l'annuncio.
     * È una relazione @OneToMany, in cui un annuncio può essere associato a più candidati.
     * La relazione è mappata dall'attributo annuncio nell'entità Rifugiato.
     */
    @ElementCollection
    @CollectionTable(
            name = "consulenza_rifugiati",
            joinColumns = @JoinColumn(name = "consulenza_id")
    )
    @MapKeyColumn(name = "rifugiato_email")
    @Column(name = "stato")
    private Map<String, Boolean> candidati; // true = accettato, false = respinto/in attesa

}
