package com.project.bridgebackend.Model.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author Benedetta Colella.
 * Creato il: 03/12/2024
 * Questa Java Persistence Entity rappresenta un indirizzo,
 *
 */
@Entity
@Table(name = "Indirizzo")
@Data
public final class Indirizzo implements Serializable {

    /**87
     * Rappresenta l'id autogenerato.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Stringa che rappresenta la città dell'indirizzo.
     */
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[A-zÀ-ù ‘]{2,50}$",
            message = "La città contiene caratteri non validi")
    @Column(name = "citta")
    private String citta;

    /**
     * Numero Intero che rappresenta il numero civico dell'indirizzo.
     */
    @NotNull
    @Column(name = "num_civico")
    private int numCivico;

    /**
     * Numero Intero che rappresenta il cap dell'indirizzo.
     * deve essere un numero intero di 5 cifre
     */
    @NotNull
    //@Pattern(regexp = "^\\d{5}$",
    //        message = "Il CAP contiene caratteri non validi")
    @Column(name = "cap")
    private String cap;
    /**
     * Stringa che rappresenta la provincia dell'indirizzo.
     */
    @NotNull
    @Pattern(regexp = "^[A-Z]{2}$",
            message = "La provincia contiene caratteri non validi")
    @Column(name = "provincia")
    private String provincia;

    /**
     * Stringa che rappresenta la via dell'indirizzo.
     */
    @NotNull
    @Pattern(regexp = "^[A-zÀ-ù ‘]{2,50}$",
            message = "La via contiene caratteri non validi")
    @Column(name = "via")
    private String via;


    /**
     * modulo fia
     * latitudine e longitudine per semplificare i calcoli
     */
    @NotNull
    @Column(name = "latitudine")
    private Double latitudine;

    @NotNull
    @Column(name = "longitudine")
    private Double longitudine;
    /**
     * Costruttore vuoto.
     */
    public Indirizzo() {
    }



    /**
     * Costruttore con parametri.
     * @param citta
     * @param numCivico
     * @param cap
     * @param provincia
     * @param via
     */
    public Indirizzo(final String citta,
                     final int numCivico,
                     final String cap,
                     final String provincia,
                     final String via) {
        this.citta = citta;
        this.numCivico = numCivico;
        this.cap = cap;
        this.provincia = provincia;
        this.via = via;
        this.latitudine = (double) 0;
        this.longitudine = (double) 0;
    }

    /**
     * Costruttore con parametri. + latitudine e longitudine
     */
    public Indirizzo(final String citta,
                     final int numCivico,
                     final String cap,
                     final String provincia,
                     final String via,
                     final double latitudine,
                     final double longitudine) {
        this.citta = citta;
        this.numCivico = numCivico;
        this.cap = cap;
        this.provincia = provincia;
        this.via = via;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    /**
     * Metodo equals.
     * @param o oggetto da confermare
     * @return true se l'oggetto è uguale, false altrimenti
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Indirizzo indirizzo)) {
            return false;
        }
        return Objects.equals(id, indirizzo.id)
                && Objects.equals(getCitta(), indirizzo.getCitta())
                && Objects.equals(getNumCivico(), indirizzo.getNumCivico())
                && Objects.equals(getCap(), indirizzo.getCap())
                && Objects.equals(getProvincia(), indirizzo.getProvincia())
                && Objects.equals(getVia(), indirizzo.getVia());
    }

    /**
     * Metodo hashCode.
     * @return hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, getCitta(), getNumCivico(), getCap(), getProvincia(), getVia());
    }
}
