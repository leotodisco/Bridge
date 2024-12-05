package com.project.bridgebackend.GestioneEvento.service;

import com.project.bridgebackend.Model.Entity.Evento;
import com.project.bridgebackend.Model.dto.EventoDTO;
import java.util.Optional;

/**
 * @author Alessia De Filippo
 * Creato il: 05/12/2024
 * Interfaccia per la gestione degli eventi.
 **/

public interface GestioneEventoService {

    /**
     * Permette di creare un evento.
     * @param eventoDTO evento da creare.
     * @return eventoDTO creato.
     **/
    Evento createEvento(EventoDTO eventoDTO);

    /**
     * Permette di aggiornare un evento.
     * @param eventoDTO evento da aggiornare.
     * @return eventoDTO aggiornato.
     **/
    EventoDTO updateEvento(EventoDTO eventoDTO);

    /**
     * Permette di eliminare un evento.
     * @param id identificativo dell'evento da eliminare.
     **/
    void deleteEvento(long id);

    /**
     * Permette di ottenere un evento tramite il suo id.
     *
     * @param id identificativo dell'evento.
     * @return eventoDTO.
     **/
    Optional<Evento> getEventoById(long id);
}
