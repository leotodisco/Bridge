package com.project.bridgebackend.GestioneCorso.Service;


import com.project.bridgebackend.Model.Entity.Corso;
import com.project.bridgebackend.Model.dto.CorsoDTO;
import org.springframework.web.multipart.MultipartFile;

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
    Corso creaCorso(final Corso corso);
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
    void eliminaCorso(final Corso corso);

    /**
     * Trova tutti i corsi.
     * @return una lista di tutti i corsi come CorsoDTO.
     */
    List<Corso> findAll();
}
