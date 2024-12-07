package com.project.bridgebackend.GestioneCorso.Service;


import com.project.bridgebackend.Model.dto.CorsoDTO;

import java.util.List;

/**
 * @author Biagio Gallo.
 * Creato il: 06/12/2024.
 * Questa interfaccia fornisce metodi per la gestione dei corsi.
 */
public interface GestioneCorsoService {

    /**
     * Crea un nuovo corso.
     * @param corsoDTO il DTO del corso contenente i dettagli del corso.
     * @return il corso creato come CorsoDTO.
     */
    CorsoDTO creaCorso(CorsoDTO corsoDTO);

    /**
     * Modifica un corso esistente.
     * @param corsoDTO il DTO del corso contenente
     * i dettagli aggiornati del corso.
     * @return il corso modificato come CorsoDTO.
     */
    CorsoDTO modificaCorso(CorsoDTO corsoDTO);

    /**
     * Elimina un corso esistente.
     * @param corsoDTO il DTO del corso da eliminare
     */
    void eliminaCorso(CorsoDTO corsoDTO);

    /**
     * Trova tutti i corsi.
     * @return una lista di tutti i corsi come CorsoDTO.
     */
    List<CorsoDTO> findAll();
}
