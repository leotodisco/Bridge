package com.project.bridgebackend.GestioneEvento.service;

import com.project.bridgebackend.Model.Entity.Evento;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.dao.EventoDAO;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
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
     * DAO per la gestione degli indirizzi.
     **/
    @Autowired
    private IndirizzoDAO indirizzoDAO;

    /**
     * Permette di creare un evento.
     * @param evento evento da creare.
     * @return evento creato.
     **/
    @Transactional
    @Override
    public Evento insertEvento(final Evento evento) {
        //Controllo su DTO nullo o id nullo
        if (evento == null || evento.getId() == null) {
            throw new IllegalArgumentException("Evento non valido.");
        }

        //Salvataggio entità Evento
        return eventoDAO.save(evento);
    }

    /**
     * Permette di aggiornare un evento.
     * @param evento evento da aggiornare.
     * @return evento aggiornato.
     */
    @Override
    public Evento updateEvento(final Evento evento) {
        //Controllo su DTO nullo o id nullo
        if (evento == null || evento.getId() == null) {
            throw new
                    IllegalArgumentException("Id evento non valido");
        }

        //Verifica che l'evento esista
        Evento existingEvento = eventoDAO.findById(evento.getId())
                .orElseThrow(() -> new
                        IllegalArgumentException("Evento non trovato"));

        //Aggiornamento evento
        existingEvento
                .setNome(evento.getNome());
        existingEvento
                .setData(evento.getData());
        existingEvento
                .setOra(evento.getOra());
        existingEvento
                .setLingueParlate(evento.getLingueParlate());
        existingEvento
                .setDescrizione(evento.getDescrizione());
        existingEvento
                .setLuogo(evento.getLuogo());
        existingEvento
                .setOrganizzatore(evento.getOrganizzatore());
        existingEvento
                .setMaxPartecipanti(evento.getMaxPartecipanti());

        //Salvataggio entità Evento
        return eventoDAO.save(existingEvento);
    }

    /**
     * Permette di eliminare un evento.
     * @param id identificativo dell'evento da eliminare.
     */
    @Override
    public void deleteEvento(final long id) {
       //Controllo su id nullo
        if (id <= 0) {
           throw new
                   IllegalArgumentException("Id evento non valido");
       }

        //Verifica che l'evento esista
        if (eventoDAO.existsById(id)) {
            eventoDAO.deleteById(id);
        } else {
            throw new IllegalArgumentException(
                    "Evento con id " + id + " non trovato");
        }
    }

    /**
     * Permette di avere tutti gli eventi.
     * @return lista di tutti gli eventi.
     */
    @Override
    public List<Evento> getAllEventi() {
        return eventoDAO.findAll();
    }

    /**
     * Permette di avere un evento tramite id.
     * @param id identificativo dell'evento.
     * @return evento.
     */
    @Override
    public Optional<Evento> getEventoById(final long id) {
        //Controllo su id nullo
        if (id <= 0) {
            throw new IllegalArgumentException("Id evento non valido");
        }

        //Recupero evento
        return eventoDAO.findById(id);
    }

    /**
     * Permette di salvare l'indirizzo di un evento.
     * @param indirizzo indirizzo da salvare.
     * @return indirizzo salvato.
     */
    @Override
    public Indirizzo salvaIndirizzoEvento(final Indirizzo indirizzo) {
        //Controllo su Indirizzo nullo o id nullo
        if (indirizzo == null || indirizzo.getId() == null) {
            throw new IllegalArgumentException("Id indirizzo non valido");
        }

        //Salvataggio indirizzo
        return indirizzoDAO.save(indirizzo);
    }

    /**
     * Permette di aggiornare l'indirizzo di un evento.
     * @param indirizzo indirizzo da aggiornare.
     * @return indirizzo aggiornato.
     */
    @Override
    public Indirizzo updateIndirizzoEvento(final Indirizzo indirizzo) {
        //Controllo su Indirizzo nullo o id nullo
        if (indirizzo == null || indirizzo.getId() == null) {
            throw new IllegalArgumentException("Id indirizzo non valido");
        }

        //Verifica che l'indirizzo esista
        Indirizzo existingIndirizzo = indirizzoDAO.findById(indirizzo.getId())
                .orElseThrow(() -> new
                        IllegalArgumentException("Indirizzo non trovato"));

        //Aggiornamento indirizzo
        existingIndirizzo.setCitta(indirizzo.getCitta());
        existingIndirizzo.setNumCivico(indirizzo.getNumCivico());
        existingIndirizzo.setCap(indirizzo.getCap());
        existingIndirizzo.setProvincia(indirizzo.getProvincia());
        existingIndirizzo.setVia(indirizzo.getVia());

        return indirizzoDAO.save(existingIndirizzo);
    }
}
