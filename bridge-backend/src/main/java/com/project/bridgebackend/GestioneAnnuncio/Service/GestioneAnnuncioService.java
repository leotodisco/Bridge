package com.project.bridgebackend.GestioneAnnuncio.Service;

import com.project.bridgebackend.Model.Entity.Consulenza;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Lavoro;

import java.util.HashMap;

/**
 * Servizio per la gestione degli annunci di lavoro e consulenza.
 * Include operazioni di inserimento, modifica, eliminazione e gestione delle preferenze.
 *
 * @author Geraldine Montella, Vito Vernellati
 * @created 04/12/2024
 * @version 1.0
 */
public interface GestioneAnnuncioService {

    // Metodi per Consulenza

    /**
     * Inserisce una nuova consulenza nel database.
     *
     * @param consulenza l'annuncio di consulenza da inserire, già validato.
     * @return la consulenza appena inserita nel database.
     */
    Consulenza inserimentoConsulenza(Consulenza consulenza);

    /**
     * Salva l'indirizzo specifico per la sede di consulenza.
     *
     * @param indirizzo l'indirizzo da salvare, già validato.
     * @return l'identificativo dell'indirizzo appena salvato.
     */
    long salvaIndirizzoConsulenza(Indirizzo indirizzo);

    // Metodi per Lavoro

    /**
     * Inserisce un nuovo annuncio di lavoro nel database.
     *
     * @param lavoro l'annuncio di lavoro da inserire, già validato.
     * @return l'annuncio di lavoro appena inserito nel database.
     */
    Lavoro inserimentoLavoro(Lavoro lavoro);

    /**
     * Salva l'indirizzo specifico per la sede di lavoro.
     *
     * @param indirizzo l'indirizzo da salvare, già validato.
     * @return l'identificativo dell'indirizzo appena salvato.
     */
    long salvaIndirizzoLavoro(Indirizzo indirizzo);

    /**
     * Modifica un annuncio di lavoro esistente.
     *
     * @param idAnnuncio l'identificativo dell'annuncio da modificare.
     * @param aggiornamenti mappa contenente i campi da aggiornare e i relativi valori.
     * @return l'annuncio di lavoro aggiornato.
     */
    Lavoro modificaAnnuncioLavoro(long idAnnuncio, HashMap<String, Object> aggiornamenti);

    /**
     * Elimina un annuncio di lavoro dal database.
     *
     * @param idAnnuncio l'identificativo dell'annuncio da eliminare.
     */
    void eliminaAnnuncioLavoro(long idAnnuncio);
}
