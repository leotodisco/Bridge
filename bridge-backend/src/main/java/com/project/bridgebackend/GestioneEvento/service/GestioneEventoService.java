package com.project.bridgebackend.GestioneEvento.service;

import com.project.bridgebackend.Model.Entity.Evento;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import java.util.List;
import java.util.Optional;

/**
 * @author Alessia De Filippo
 * Creato il: 05/12/2024
 * Interfaccia per la gestione degli eventi.
 **/

public interface GestioneEventoService {

    /**
     * Permette di creare un evento.
     * @param evento evento da creare.
     * @return evento creato.
     **/
    Evento insertEvento(Evento evento);

    /**
     * Permette di aggiornare un evento.
     * @param evento evento da aggiornare.
     * @return evento aggiornato.
     **/
    Evento updateEvento(Evento evento);

    /**
     * Permette di eliminare un evento.
     * @param id identificativo dell'evento da eliminare.
     **/
    void deleteEvento(long id);

    /**
     * Permette di avere tutti gli eventi.
     * @return lista di eventi.
     */
    List<Evento> getAllEventi();

    /**
     * Permette di avere un evento tramite id.
     * @param id identificativo dell'evento.
     * @return evento.
     */
    Optional<Evento> getEventoById(long id);

    /**
     * Permette di salvare l'indirizzo di un evento.
     * @param indirizzo indirizzo da salvare.
     * @return indirizzo salvato.
     */
    long salvaIndirizzoEvento(Indirizzo indirizzo);

    /**
     * Permette di aggiornare l'inidirzzo di un evento.
     * @param indirizzo indirizzo da aggiornare.
     * @return indirizzo aggiornato.
     */
    Indirizzo updateIndirizzoEvento(Indirizzo indirizzo);
}
