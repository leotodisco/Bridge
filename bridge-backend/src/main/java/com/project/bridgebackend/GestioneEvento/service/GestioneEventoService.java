package com.project.bridgebackend.GestioneEvento.service;

import com.project.bridgebackend.Model.Entity.Evento;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Rifugiato;

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
     * Permette di iscriversi a un evento.
     * @param eventoId identificativo dell'evento.
     * @param  partecipanteEmail email del volontario.
     * @return evento a cui si è iscritti.
     */
    Evento iscrizioneEvento(long eventoId, String partecipanteEmail);

    /**
     * Permette di disiscriversi da un evento.
     * @param eventoId identificativo dell'evento.
     * @param partecipanteEmail email del volontario.
     * @return evento da cui ci si è disiscritti.
     */
    Evento disiscrizioneEvento(long eventoId, String partecipanteEmail);

    /**
     * Permette di trovare gli eventi di un volontario.
     * @param email email del volontario.
     * @return lista di eventi.
     */
    List<Evento> getEventiByVolontario(String email);

    /**
     * Permette di trovare i partecipanti di un evento.
     * @param eventoId identificativo dell'evento.
     * @return evento con partecipanti.
     */
    Evento trovaEventoConPartecipanti(long eventoId);

    /**
     * Permette di ottenere la lista dei partecipanti di un evento.
     * @param eventoId identificativo dell'evento.
     * @return lista dei partecipanti.
     */
    List<Rifugiato> getPartecipantiPerEvento(final long eventoId);

    /**
     * Permette di avere eventi random.
     * @return lista di eventi random.
     */
    List<Evento> getRandomEvents();

    /**
     * Restituisce la lista degli eventi a cui un rifugiato è iscritto.
     *
     * @param email Email del rifugiato
     * @return Lista di ID degli eventi a cui il rifugiato è iscritto
     */
    List<Evento> getEventiIscritti(String email);
}
