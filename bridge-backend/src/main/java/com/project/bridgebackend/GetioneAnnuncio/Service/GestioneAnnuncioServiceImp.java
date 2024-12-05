package com.project.bridgebackend.GetioneAnnuncio.Service;

import com.project.bridgebackend.Model.Entity.Consulenza;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.dao.ConsulenzaDAO;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;


/**
 * @author Geraldine Montella.
 * Creato il: 04/12/2024.

 * Questa classe implementa il servizio per la gestione degli annunci,
 * interagendo con i repository di Consulenza e Indirizzo per il salvataggio nel database.
 */
@Valid
@Service
public class GestioneAnnuncioServiceImp implements GestioneAnnuncioService {
    /**
     * Repository per la gestione delle consulenze nel database
     */
    @Autowired
    private ConsulenzaDAO consulenzaDAO;

    /**
     * Repository per la gestione degli indirizzi nel database
     */
    @Autowired
    private IndirizzoDAO indirizzoDAO;

    /**
     * Metodo per inserire una nuova consulenza nel sistema.
     * Prima di salvare, viene verificato che il numero massimo di candidature
     * sia almeno 1. Se non è valido, viene lanciata un'eccezione.
     * @param consulenza-> l'oggetto Consulenza da salvare
     * @return la consulenza salvata nel database
     */
    @Override
    public Consulenza inserimentoConsulenza(Consulenza consulenza) {

        /**
         * Verifica che il numero massimo di candidature sia almeno 1
         */
        if (consulenza.getMaxCandidature() < 1) {
            throw new IllegalArgumentException("Il numero massimo di candidature deve essere almeno 1.");
        }

        return consulenzaDAO.save(consulenza);
    }


    /**
     * Metodo per salvare un indirizzo di consulenza.
     * Dopo aver salvato l'indirizzo, viene restituito il suo ID.
     * @param indirizzo-> l'oggetto Indirizzo da salvare
     * @return l'ID dell'indirizzo salvato
     */
    @Override
    public long salvaIndirizzoConsulenza (Indirizzo indirizzo){
        indirizzoDAO.save(indirizzo);
        return indirizzo.getId();
    }

}
