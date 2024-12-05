package com.project.bridgebackend.Model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Benedetta Colella
 * Creato il: 03/12/2024
 * Questa Java Persistence Entity rappresenta un indirizzo,
 *
 */
@Entity
@Table(name = "Indirizzo")
@Getter
@Setter
public final class Indirizzo implements Serializable {

    /**
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
     * deve essere un numero intero di 5 cifre.
     */
    @NotNull
    @Column(name = "cap")
    private int cap;

    /**
     * Stringa che rappresenta la provincia dell'indirizzo.
     */
    @NotNull
    @Column(name = "provincia")
    private String provincia;

    /**
     * Stringa che rappresenta la via dell'indirizzo.
     */
    @NotNull
    @Column(name = "via")
    private String via;

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
    public Indirizzo(String citta, int numCivico, int cap, String provincia, String via) {
        this.citta = citta;
        this.numCivico = numCivico;
        this.cap = cap;
        this.provincia = provincia;
        this.via = via;
    }

    /**
     * Metodo equals
     * @param o oggetto da confermare
     * @return true se l'oggetto è uguale, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if(!(o instanceof Indirizzo indirizzo)) {
            return false;
        }
        return Objects.equals(getId(), indirizzo.getId())
                && Objects.equals(getCitta(), indirizzo.getCitta())
                && Objects.equals(getNumCivico(), indirizzo.getNumCivico())
                && Objects.equals(getCap(), indirizzo.getCap())
                && Objects.equals(getProvincia(), indirizzo.getProvincia())
                && Objects.equals(getVia(), indirizzo.getVia());
    }

    /**
     * Metodo hashCode.
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, getCitta(), getNumCivico(), getCap(), getProvincia(), getVia());
    }

}
