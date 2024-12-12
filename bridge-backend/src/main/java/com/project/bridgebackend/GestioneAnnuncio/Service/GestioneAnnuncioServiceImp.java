package com.project.bridgebackend.GestioneAnnuncio.Service;

import com.project.bridgebackend.Model.Entity.*;
import com.project.bridgebackend.Model.Entity.enumeration.TipoContratto;
import com.project.bridgebackend.Model.dao.ConsulenzaDAO;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
import com.project.bridgebackend.Model.dao.LavoroDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

/**
 * @author Geraldine Montella, Vito Vernellati.
 * Creato il: 04/12/2024.

 * Questa classe implementa il servizio per la gestione degli annunci,
 * interagendo con i repository di Consulenza e Indirizzo per il salvataggio nel database.
 */

@Valid
@Service
public class GestioneAnnuncioServiceImp implements GestioneAnnuncioService {

    /**
     * Repository per la gestione delle consulenze nel database.
     */
    @Autowired
    private ConsulenzaDAO consulenzaDAO;

    /**
     * Repository per la gestione degli indirizzi nel database.
     */
    @Autowired
    private IndirizzoDAO indirizzoDAO;

    /**
     * Repository per la gestione dei lavori nel database.
     */
    @Autowired
    private LavoroDAO lavoroDAO;

    // Implementazioni dei metodi per Consulenza

    /**
     * Inserisce una nuova consulenza nel database.
     * Verifica che il numero massimo di candidature sia almeno 1.
     *
     * @param consulenza l'oggetto Consulenza da inserire.
     * @return la consulenza salvata nel database.
     */
    @Override
    public Consulenza inserimentoConsulenza(final Consulenza consulenza) {
        if (consulenza.getMaxCandidature() < 1) {
            throw new IllegalArgumentException("Il numero massimo di candidature deve essere almeno 1.");
        }
        return consulenzaDAO.save(consulenza);
    }

    /**
     * Salva un indirizzo di consulenza nel database.
     *
     * @param indirizzo l'oggetto Indirizzo da salvare.
     * @return l'ID dell'indirizzo salvato.
     */
    @Override
    public long salvaIndirizzoConsulenza(final Indirizzo indirizzo) {
        indirizzoDAO.save(indirizzo);
        return indirizzo.getId();
    }

    // Implementazioni dei metodi per Lavoro

    /**
     * Inserisce un nuovo annuncio di lavoro nel database.
     * Verifica che la retribuzione sia positiva.
     *
     * @param lavoro l'oggetto Lavoro da inserire.
     * @return l'annuncio di lavoro salvato nel database.
     */
    @Override
    public Lavoro inserimentoLavoro(final Lavoro lavoro) {
        if (lavoro.getRetribuzione() <= 0) {
            throw new IllegalArgumentException("La retribuzione deve essere positiva.");
        }
        return lavoroDAO.save(lavoro);
    }

    /**
     * Salva un indirizzo di lavoro nel database.
     *
     * @param indirizzo l'oggetto Indirizzo da salvare.
     * @return l'ID dell'indirizzo salvato.
     */
    @Override
    public long salvaIndirizzoLavoro(final Indirizzo indirizzo) {
        indirizzoDAO.save(indirizzo);
        return indirizzo.getId();
    }

    /**
     * Modifica un annuncio di lavoro esistente nel database.
     *
     * @param idAnnuncio l'ID dell'annuncio da modificare.
     * @param aggiornamenti mappa contenente i campi da aggiornare e i relativi valori.
     * @return l'annuncio di lavoro aggiornato.
     */
    @Override
    public Lavoro modificaAnnuncioLavoro(final long idAnnuncio, final HashMap<String, Object> aggiornamenti) {
        Optional<Lavoro> lavoroOptional = lavoroDAO.findById(idAnnuncio);
        if (lavoroOptional.isEmpty()) {
            throw new IllegalArgumentException("Annuncio di lavoro non trovato.");
        }

        Lavoro lavoro = lavoroOptional.get();

        aggiornamenti.forEach((campo, valore) -> {
            switch (campo) {
                case "posizioneLavorativa":
                    lavoro.setPosizioneLavorativa((String) valore);
                    break;
                case "nomeAzienda":
                    lavoro.setNomeAzienda((String) valore);
                    break;
                case "orarioLavoro":
                    lavoro.setOrarioLavoro((String) valore);
                    break;
                case "tipoContratto":
                    lavoro.setTipoContratto((TipoContratto) valore);
                    break;
                case "retribuzione":
                    lavoro.setRetribuzione((Double) valore);
                    break;
                case "nomeSede":
                    lavoro.setNomeSede((String) valore);
                    break;
                case "infoUtili":
                    lavoro.setInfoUtili((String) valore);
                    break;
                default:
                    throw new IllegalArgumentException("Campo non valido per la modifica: " + campo);
            }
        });

        return lavoroDAO.save(lavoro);
    }

    @Override
    public void eliminaAnnuncioLavoro(final long idAnnuncio) {
        if (!lavoroDAO.existsById(idAnnuncio)) {
            throw new IllegalArgumentException("Annuncio di lavoro non trovato.");
        }
        lavoroDAO.deleteById(idAnnuncio);
    }


    @Override
    public List<Consulenza> getAllConsulenze(){
        return consulenzaDAO.findAll();
    }

    @Override
    public List<Consulenza> getConsulenzeByProprietario(Utente proprietario) {
        return consulenzaDAO.findByProprietario(proprietario);
    }
}
