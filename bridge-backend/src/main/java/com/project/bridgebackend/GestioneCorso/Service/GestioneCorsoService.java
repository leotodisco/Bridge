package com.project.bridgebackend.GestioneCorso.Service;


import com.project.bridgebackend.Model.Entity.Corso;

import java.util.List;

/**
 * @author Biagio Gallo.
 * Creato il: 06/12/2024.
 * Questa interfaccia fornisce metodi per la gestione dei corsi.
 */
public interface GestioneCorsoService {

    /**
     * Crea un nuovo corso.
     * @param corso il DTO del corso contenente i dettagli del corso.
     * @return il corso creato come CorsoDTO.
     */
    Corso creaCorso(Corso corso);
    /**
     * Modifica un corso esistente.
     * @param corso il DTO del corso contenente
     * i dettagli aggiornati del corso.
     * @return il corso modificato come CorsoDTO.
     */
    Corso modificaCorso(Corso corso);

    /**
     * Elimina un corso esistente.
     * @param corso il DTO del corso da eliminare
     */
    void eliminaCorso(Corso corso);

    /**
     * Trova tutti i corsi.
     * @return una lista di tutti i corsi come CorsoDTO.
     */
    List<Corso> findAll();
}
