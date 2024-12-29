package com.project.bridgebackend.GestioneEvento.service;

import com.project.bridgebackend.Model.Entity.Evento;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.dao.EventoDAO;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
import com.project.bridgebackend.Model.dao.RifugiatoDAO;
import com.project.bridgebackend.Model.dao.VolontarioDAO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
     * DAO per la gestione dei rifugiati.
     **/
    @Autowired
    private RifugiatoDAO rifugiatoDAO;

    /**
     * DAO per la gestione dei volontari.
     **/
    @Autowired
    private VolontarioDAO volontarioDAO;

    /**
     * Permette di creare un evento.
     * @param evento evento da creare.
     * @return evento creato.
     **/
    @Transactional
    @Override
    public Evento insertEvento(final Evento evento) {
        //Controllo su DTO nullo o id nullo
        if (evento == null) {
            throw new IllegalArgumentException("Evento non valido.");
        }

        //Salvataggio entità Evento
        return eventoDAO.save(evento);
    }

    /**
     * Permette di avere tutti gli eventi.
     * @return lista di tutti gli eventi.
     */
    @Override
    public List<Evento> getAllEventi() {
        return eventoDAO.findAllWithOrganizzatore();
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
    public long salvaIndirizzoEvento(final Indirizzo indirizzo) {
        //Controllo su Indirizzo nullo o id nullo
        if (indirizzo == null) {
            throw new IllegalArgumentException("Id indirizzo non valido");
        }

        //Salvataggio indirizzo e ritorno id
        return indirizzoDAO.save(indirizzo).getId();
    }



    /**
     * Permette di iscriversi a un evento.
     * @param eventoId identificativo dell'evento.
     * @param  partecipanteEmail email del volontario.
     * @return evento a cui si è iscritti.
     */
    @Transactional
    @Override
    public Evento iscrizioneEvento(
            final long eventoId,
            final String partecipanteEmail) {
        //Controlla che l'evento esista
        Evento evento = eventoDAO.findById(eventoId)
                .orElseThrow(() -> new
                        IllegalArgumentException("Evento non trovato"));

        //Controlla che il partecipante esista
        Rifugiato partecipante = rifugiatoDAO.findByEmail(partecipanteEmail);
        if (partecipante == null) {
            throw new IllegalArgumentException("Partecipante non trovato");
        }

        //Controlla che il rifugiato non sia già iscritto
        if (evento.getListaPartecipanti().contains(partecipante)) {
            throw new IllegalArgumentException("Partecipante già iscritto");
        }

        //Controlla che l'evento non sia già pieno
        if (evento.getListaPartecipanti().size() >= evento.getMaxPartecipanti()) {
            throw new IllegalArgumentException("Evento pieno");
        }

        //Aggiungi il partecipante alla lista
        evento.getListaPartecipanti().add(partecipante);

        //Salva l'evento aggiornato
        return eventoDAO.save(evento);
    }

    /**
     * Permette di disiscriversi a un evento.
     * @param eventoId identificativo dell'evento.
     * @param  partecipanteEmail email del volontario.
     * @return evento a cui si è disiscritti.
     */
    @Transactional
    @Override
    public Evento disiscrizioneEvento(
            final long eventoId,
            final String partecipanteEmail) {
        //Controlla che l'evento esista
        System.out.println("Evento id: " + eventoId);
        System.out.println("Partecipante email: " + partecipanteEmail);
        System.out.println("Evento: " + eventoDAO.findById(eventoId));
        Evento evento = eventoDAO.findEventoWithPartecipanti(eventoId)
                .orElseThrow(() -> new
                        IllegalArgumentException("Evento non trovato"));

        //Controlla che il partecipante esista
        System.out.println("Partecipante: " + rifugiatoDAO.findByEmail(partecipanteEmail));
        Rifugiato partecipante = rifugiatoDAO.findByEmail(partecipanteEmail);
        if (partecipante == null) {
            throw new IllegalArgumentException("Partecipante non trovato");
        }

        //Controlla che il rifugiato sia iscritto
        System.out.println("Lista partecipanti: " + evento.getListaPartecipanti());
        if (!evento.getListaPartecipanti().contains(partecipante)) {
            throw new IllegalArgumentException("Partecipante non iscritto");
        }

        //Rimuovi il partecipante dalla lista
        System.out.println("Partecipante da rimuovere: " + partecipante);
        evento.getListaPartecipanti().remove(partecipante);

        //Salva l'evento aggiornato
        return eventoDAO.save(evento);
    }

    /**
     * Permette di trovare gli eventi di un volontario.
     * @param emailOrganizzatore email del volontario.
     * @return lista di eventi.
     */
    @Override
    public List<Evento> getEventiByVolontario(
            final String emailOrganizzatore) {
        System.out.println("Stampami qualcosa: " + emailOrganizzatore);
        Volontario organizzatore = volontarioDAO.findByEmail(emailOrganizzatore);
        if (organizzatore == null) {
            throw new IllegalArgumentException("Volontario non trovato con email: " + emailOrganizzatore);
        }
        return eventoDAO.findByOrganizzatore(organizzatore);
    }

    /**
     * Recupera un evento specifico, inclusa la lista dei partecipanti associati.
     *
     * @param eventoId Identificativo univoco dell'evento da recuperare.
     * @return L'oggetto {@link Evento} che include la lista dei partecipanti.
     * @throws IllegalArgumentException se l'evento con l'ID specificato non viene trovato.
     */
    @Override
    public Evento trovaEventoConPartecipanti(
            final long eventoId) {
        return eventoDAO.findEventoWithPartecipanti(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato"));
    }

    @Override
    public List<Evento> getRandomEvents() {
        // Recupera tutti gli eventi
        List<Evento> eventi = eventoDAO.findAllWithOrganizzatore();

        // Mischia la lista
        Collections.shuffle(eventi);

        //Restituisce solo 5 eventi
        return eventi.stream().limit(5).toList();
    }




    @Override
    public List<Rifugiato> getPartecipantiPerEvento(final long eventoId) {
        // Recupera l'evento specificato tramite ID
        Evento evento = eventoDAO.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato"));

        // Restituisce la lista dei partecipanti (Rifugiato) associati all'evento
        return evento.getListaPartecipanti();
    }


    /**
     * Restituisce la lista di eventi a cui un rifugiato è iscritto.
     *
     * @param email Email del rifugiato.
     * @return Lista di eventi a cui il rifugiato è iscritto.
     */
    public List<Evento> getEventiIscritti(String email) {
        List<Evento> eventi = eventoDAO.findEventiByRifugiatoEmail(email);
        if (eventi.isEmpty()) {
            return Collections.emptyList();
        }
        return eventi;
    }
}
