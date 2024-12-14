package com.project.bridgebackend.util.Indirizzo.service;

import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class IndirizzoServiceImp implements IndirizzoService {

    @Autowired
    private IndirizzoDAO indirizzoDAO;

    @Override
    @Transactional
    public void aggiornaIndirizzo(long idIndirizzo, HashMap<String, Object> indirizzoData) {
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

