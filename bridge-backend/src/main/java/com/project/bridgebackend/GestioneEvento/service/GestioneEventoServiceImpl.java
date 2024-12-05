package com.project.bridgebackend.GestioneEvento.service;

import com.project.bridgebackend.Model.Entity.Evento;
import com.project.bridgebackend.Model.dao.EventoDAO;
import com.project.bridgebackend.Model.dto.EventoDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * @author Alessia De Filippo
 * Creato il: 05/12/2024
 * Implementazione dell'interfaccia GestioneEventoService.
 **/

@Service
public class GestioneEventoServiceImpl implements GestioneEventoService {

    /**
     * DAO per la gestione degli eventi.
     **/
    @Autowired
    private EventoDAO eventoDAO;

    /**
     * Permette di creare un evento.
     * @param eventoDTO evento da creare.
     * @return evento creato.
     **/
    @Transactional
    @Override
    public Evento createEvento(final EventoDTO eventoDTO) {
        //Controllo su DTO nullo o id nullo
        if (eventoDTO == null || eventoDTO.getId() == null) {
            throw new IllegalArgumentException("Id evento non valido o DTO nullo");
        }

        //Creazione entità Evento
        Evento evento = new Evento();
        evento.setNome(
                eventoDTO.getNome());
        evento.setData(
                eventoDTO.getData());
        evento.setOra(
                eventoDTO.getOra());
        evento.setLingueParlate(
                eventoDTO.getLingueParlate());
        evento.setDescrizione(
                eventoDTO.getDescrizione());
        evento.setLuogo(
                eventoDTO.getLuogo());
        evento.setOrganizzatore(
                eventoDTO.getOrganizzatore());
        evento.setMaxPartecipanti(
                eventoDTO.getMaxPartecipanti());

        //Salvataggio entità Evento
        return eventoDAO.save(evento);
    }

    /**
     * Permette di aggiornare un evento.
     * @param eventoDTO evento da aggiornare.
     * @return eventoDTO aggiornato.
     */
    @Override
    public EventoDTO updateEvento(final EventoDTO eventoDTO) {
        //Controllo su DTO nullo o id nullo
        if (eventoDTO == null || eventoDTO.getId() == null) {
            throw new IllegalArgumentException("Id evento non valido");
        }

        //Controllo esistenza evento
        if (eventoDAO.existsById(eventoDTO.getId())) {
            //Update campi Evento
            Evento evento = new Evento();
            evento.setId(
                    eventoDTO.getId());
            evento.setNome(
                    eventoDTO.getNome());
            evento.setData(
                    eventoDTO.getData());
            evento.setOra(
                    eventoDTO.getOra());
            evento.setLingueParlate(
                    eventoDTO.getLingueParlate());
            evento.setDescrizione(
                    eventoDTO.getDescrizione());
            evento.setLuogo(
                    eventoDTO.getLuogo());
            evento.setOrganizzatore(
                    eventoDTO.getOrganizzatore());
            evento.setMaxPartecipanti(
                    eventoDTO.getMaxPartecipanti());

            //Salvataggio entità Evento
            eventoDAO.save(evento);
            return eventoDTO;
        } else {
            throw new IllegalArgumentException("Evento con id " + eventoDTO.getId() + " non trovato");
        }
    }

    /**
     * Permette di eliminare un evento.
     * @param id identificativo dell'evento da eliminare.
     */
    @Override
    public void deleteEvento(final long id) {
        //Controllo su id nullo
        if (id <= 0) {
            throw new IllegalArgumentException("Id evento non valido");
        }

        //Controllo esistenza evento e cancellazione
        if (eventoDAO.existsById(id)) {
            eventoDAO.deleteById(id);
        } else {
            throw new IllegalArgumentException("Evento con id " + "non trovato");
        }
    }

    /**
     * Permette di ottenere un evento tramite il suo id.
     * @param id identificativo dell'evento.
     * @return l'evento, se viene trovato
     */
    @Override
    public Optional<Evento> getEventoById(final long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id evento non valido");
        }
        return eventoDAO.findById(id);
    }
}
