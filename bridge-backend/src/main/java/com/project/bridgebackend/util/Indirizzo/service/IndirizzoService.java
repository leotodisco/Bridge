package com.project.bridgebackend.util.Indirizzo.service;

import java.util.HashMap;

public interface IndirizzoService {
    /**
     * Aggiorna i dati di un indirizzo esistente nel sistema.
     * <p>
     * Questo metodo riceve un ID di indirizzo e una mappa,
     * contenente i dati aggiornati
     * dell'indirizzo. I campi dell'indirizzo che sono presenti,
     * nella mappa verranno aggiornati
     * nel database. Se l'indirizzo con l'ID specificato non esiste,
     * viene generata un'eccezione.
     * </p>
     *
     * @param idIndirizzo ID univoco dell'indirizzo da aggiornare.
     * @param indirizzoData Mappa contenente i dati dell'indirizzo da aggiornare.
     *                      La mappa può contenere le seguenti chiavi:
     *                      "via", "citta", "cap", "num_civico", "provincia".
     *                      I valori associati a queste chiavi dovranno essere di tipo
     *                      appropriato (String per "via", "citta" e "provincia", Integer per "cap" e "num_civico").
     *
     * @throws IllegalArgumentException se l'indirizzo con l'ID fornito non esiste nel sistema.
     */
    void aggiornaIndirizzo(
            long idIndirizzo,
            HashMap<String, Object> indirizzoData);

}
