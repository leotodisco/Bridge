package com.project.bridgebackend.GestioneCorso.Service;

import com.project.bridgebackend.Model.Entity.Corso;
import com.project.bridgebackend.Model.Entity.enumeration.Lingua;
import com.project.bridgebackend.Model.dao.CorsoDAO;
import com.project.bridgebackend.Model.dto.CorsoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Biagio Gallo.
 * Creato il: 06/12/2024.
 * Implementazione del service per la gestione dei corsi.
 * Questa classe fornisce metodi per
 * creare, modificare, eliminare e trovare corsi.
 * Utilizza il CorsoDAO per interagire con il database.
 * L'annotazione @RequiredArgsConstructor di Lombok
 * viene utilizzata per generare un costruttore con gli argomenti richiesti.
 */
@Service
@RequiredArgsConstructor
public class GestioneCorsoServiceImpl implements GestioneCorsoService {

    /**
     * Iniezione del DAO per interagire con il database.
     */
    @Autowired
    private final CorsoDAO corsoDAO;

    /**
     * Crea un nuovo corso.
     * @param corso il DTO del corso contenente i dettagli del corso.
     * @return il corso creato come CorsoDTO.
     */
    @Override
    public Corso creaCorso(final Corso corso) {
        if (corso == null) {
            throw new IllegalArgumentException("Corso non valido");
        }
        return corsoDAO.save(corso);
    }
    /**
     * Modifica un corso esistente.
     * @param corso il DTO del corso contenente
     * i dettagli aggiornati del corso.
     * @return il corso modificato come CorsoDTO.
     */
    @Override
    public Corso modificaCorso(final Corso corso) {
        // Implementazione per modificare un corso
        return null; // Placeholder return statement
    }

    /**
     * Elimina un corso esistente.
     * @param corso il DTO del corso contenente
     * i dettagli del corso da eliminare.
     */
    @Override
    public void eliminaCorso(final Corso corso) {
       if (corso.getId() == null) {
            throw new IllegalArgumentException("ID del corso non valido");
        }

       if (corsoDAO.findById(corso.getId()).isEmpty()) {
            throw new IllegalArgumentException("Corso non trovato");
        }
        corsoDAO.delete(corso);

    }

    /**
     * Trova tutti i corsi.
     * @return una lista di tutti i corsi come CorsoDTO.
     */
    @Override
    public List<Corso> findAll() {
        return corsoDAO.findAll();
    }
}
