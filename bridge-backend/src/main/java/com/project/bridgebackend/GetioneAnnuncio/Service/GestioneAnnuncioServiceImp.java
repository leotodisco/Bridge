package com.project.bridgebackend.GetioneAnnuncio.Service;

import com.project.bridgebackend.Model.Entity.Consulenza;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Lavoro;
import com.project.bridgebackend.Model.Entity.enumeration.TipoContratto;
import com.project.bridgebackend.Model.dao.ConsulenzaDAO;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
import com.project.bridgebackend.Model.dao.LavoroDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author Geraldine Montella.
 * Creato il: 04/12/2024.

 * Questa classe implementa il servizio per la gestione degli annunci,
 * interagendo con i repository di Consulenza e Indirizzo per il salvataggio nel database.
 */
@Valid
@Service
public class GestioneAnnuncioServiceImp implements GestioneAnnuncioService {
    /**
     * Repository per la gestione delle consulenze nel database
     */
    @Autowired
    private ConsulenzaDAO consulenzaDAO;

    /**
     * Repository per la gestione degli indirizzi nel database
     */
    @Autowired
    private IndirizzoDAO indirizzoDAO;

    @Autowired
    private LavoroDAO lavoroDAO;
    /**
     * Metodo per inserire una nuova consulenza nel sistema.
     * Prima di salvare, viene verificato che il numero massimo di candidature
     * sia almeno 1. Se non è valido, viene lanciata un'eccezione.
     * @param consulenza-> l'oggetto Consulenza da salvare
     * @return la consulenza salvata nel database
     */
    @Override
    public Consulenza inserimentoConsulenza(Consulenza consulenza) {

        /**
         * Verifica che il numero massimo di candidature sia almeno 1
         */
        if (consulenza.getMaxCandidature() < 1) {
            throw new IllegalArgumentException("Il numero massimo di candidature deve essere almeno 1.");
        }

        return consulenzaDAO.save(consulenza);
    }

    /**
     * @author Vito Vernellati
     * Creato il: 06/12/2024.
     */

    /**
     * Metodo per salvare un indirizzo di consulenza.
     * Dopo aver salvato l'indirizzo, viene restituito il suo ID.
     * @param indirizzo-> l'oggetto Indirizzo da salvare
     * @return l'ID dell'indirizzo salvato
     */
    @Override
    public long salvaIndirizzoConsulenza (Indirizzo indirizzo){
        indirizzoDAO.save(indirizzo);
        return indirizzo.getId();
    }

    /**
     * Metodo per inserire un nuovo annuncio di lavoro nel sistema.
     * @param lavoro L'oggetto Lavoro da salvare
     * @return L'annuncio di lavoro salvato nel database
     */
    @Override
    public Lavoro inserimentoLavoro(Lavoro lavoro) {
        if (lavoro.getRetribuzione() <= 0) {
            throw new IllegalArgumentException("La retribuzione deve essere positiva.");
        }
        return lavoroDAO.save(lavoro);
    }

    /**
     * Metodo per salvare un indirizzo di lavoro.
     * @param indirizzo L'oggetto Indirizzo da salvare
     * @return L'ID dell'indirizzo salvato
     */
    @Override
    public long salvaIndirizzoLavoro(Indirizzo indirizzo) {
        indirizzoDAO.save(indirizzo);
        return indirizzo.getId();
    }

    /**
     * Metodo per modificare un annuncio di lavoro esistente.
     * @param idAnnuncio L'ID dell'annuncio da modificare
     * @param aggiornamenti Mappa con i campi e i nuovi valori
     * @return L'annuncio di lavoro aggiornato
     */
    @Override
    public Lavoro modificaAnnuncioLavoro(long idAnnuncio, HashMap<String, Object> aggiornamenti) {
        Optional<Lavoro> lavoroOptional = lavoroDAO.findById(idAnnuncio);
        if (lavoroOptional.isEmpty()) {
            throw new IllegalArgumentException("Annuncio di lavoro non trovato");
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

    /**
     * Metodo per eliminare un annuncio di lavoro.
     * @param idAnnuncio L'ID dell'annuncio da eliminare
     */
    @Override
    public void eliminaAnnuncioLavoro(long idAnnuncio) {
        if (!lavoroDAO.existsById(idAnnuncio)) {
            throw new IllegalArgumentException("Annuncio di lavoro non trovato");
        }
        lavoroDAO.deleteById(idAnnuncio);
    }
}
