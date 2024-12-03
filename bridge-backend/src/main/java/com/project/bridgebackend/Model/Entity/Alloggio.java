package com.project.bridgebackend.Model.Entity;

/*Created by Mario Zurolo*/

import com.project.bridgebackend.Model.Entity.enumeration.Servizi;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Alloggio")
public class Alloggio {
    @Id
    @GeneratedValue()
    /*
        Id univoco di ogni alloggio
     */
    private long id;

    /*
        Metratura in metri di ogni appartamento
    */
    private int metratura;

    /*
        Numero massimo di massime persone consentite all'interno dell'alloggio
    */
    private int maxPersone;
    /*
        Breve descrizione dell'appartamento
    */
    private String descrizione;

    /*
        Nome del proprietario dell'alloggio
    */
    private Utente proprietario;
    private List <rifugiato> listaCandidati;
    private Servizi servizi;

    public Alloggio() {

    }

}
