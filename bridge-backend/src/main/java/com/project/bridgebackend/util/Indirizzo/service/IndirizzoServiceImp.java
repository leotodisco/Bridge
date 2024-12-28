package com.project.bridgebackend.util.Indirizzo.service;

import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

/**
 * Implementazione del servizio per la gestione degli indirizzi.
 * Fornisce metodi per aggiornare i dati degli indirizzi nel sistema.
 */
@Service
public class IndirizzoServiceImp implements IndirizzoService {

    /**
     * Oggetto per l'accesso e la gestione degli indirizzi nel database.
     * Utilizza il DAO per interagire con la tabella degli indirizzi.
     */
    @Autowired
    private IndirizzoDAO indirizzoDAO;

    /**
     * Metodo per aggiornare un indirizzo esistente nel database.
     * <p>
     * Aggiorna i campi dell'indirizzo identificato tramite il suo ID.
     * Se i campi sono presenti nella mappa,
     * verranno aggiornati nel database.
     * In caso di ID non valido,
     * viene lanciata un'eccezione.
     * </p>
     *
     * @param idIndirizzo ID univoco dell'indirizzo da aggiornare.
     * @param indirizzoData Mappa contenente i dati da aggiornare nell'indirizzo.
     *                      Le chiavi della mappa sono: "via", "citta", "cap",
     *                      "num_civico" e "provincia". I valori devono essere
     *                      di tipo String per le stringhe e Integer per i numeri.
     * @throws IllegalArgumentException se l'indirizzo con l'ID fornito non esiste.
     */
    @Override
    @Transactional
    public void aggiornaIndirizzo(
            final long idIndirizzo,
            final HashMap<String, Object> indirizzoData) {
        Optional<Indirizzo> indirizzoOptional = indirizzoDAO.findById(idIndirizzo);

        if (indirizzoOptional.isEmpty()) {
            throw new IllegalArgumentException("Indirizzo non trovato per l'ID: " + idIndirizzo);
        }

        Indirizzo indirizzo = indirizzoOptional.get();

        // Aggiorna i campi solo se presenti nella mappa
        if (indirizzoData.containsKey("via")) {
            indirizzo.setVia((String) indirizzoData.get("via"));
        }
        if (indirizzoData.containsKey("citta")) {
            indirizzo.setCitta((String) indirizzoData.get("citta"));
        }
        if (indirizzoData.containsKey("cap")) {
            indirizzo.setCap((Integer) indirizzoData.get("cap"));
        }
        if (indirizzoData.containsKey("num_civico")) {
            indirizzo.setNumCivico((Integer) indirizzoData.get("num_civico"));
        }
        if (indirizzoData.containsKey("provincia")) {
            indirizzo.setProvincia((String) indirizzoData.get("provincia"));
        }

        // Salva l'indirizzo aggiornato
        indirizzoDAO.save(indirizzo);
    }
}

