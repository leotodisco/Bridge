package com.project.bridgebackend.GestioneAnnuncio.Service;

import com.project.bridgebackend.Model.Entity.*;

import java.util.HashMap;
import java.util.List;

/**
 * Servizio per la gestione degli annunci di lavoro e consulenza.
 * Include operazioni di inserimento, modifica, eliminazione e gestione delle preferenze.
 *
 * @author Geraldine Montella, Vito Vernellati
 * @created 04/12/2024
 * @version 1.0
 */
public interface GestioneAnnuncioService {

    //      **Metodi per Consulenza**

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

    List<Consulenza> getAllConsulenze();

    List<Consulenza> getConsulenzeByProprietario(Utente proprietario);

    Consulenza getConsulenze( long id);

    Consulenza modificaAnnuncioConsulenza(final long idConsulenza,
                                          final HashMap<String, Object> aggiornamenti);

    //      **Metodi per Lavoro**

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
     * Recupera tutti gli annunci di lavoro presenti nel database.
     *
     * @return Una lista di oggetti `Lavoro` contenenti tutti gli annunci di lavoro.
     */
    List<Lavoro> getAllLavori();

    /**
     * Recupera tutti gli annunci di lavoro associati a un proprietario specifico.
     *
     * @param proprietario L'oggetto `Utente` che rappresenta il proprietario degli annunci di lavoro.
     * @return Una lista di oggetti `Lavoro` associati al proprietario specificato.
     */
    List<Lavoro> getLavoriByProprietario(Utente proprietario);

    /**
     * Recupera un singolo annuncio di lavoro specifico tramite il suo ID.
     *
     * @param id L'identificativo unico dell'annuncio di lavoro da recuperare.
     * @return L'oggetto `Lavoro` corrispondente all'ID fornito.
     * @throws IllegalArgumentException se l'annuncio non viene trovato.
     */
    Lavoro getLavori(long id);

    /**
     * Modifica un annuncio di lavoro esistente.
     *
     * @param idAnnuncio L'identificativo unico dell'annuncio di lavoro da modificare.
     * @param aggiornamenti Una mappa contenente i campi da aggiornare e i relativi nuovi valori.
     * @return L'oggetto `Lavoro` aggiornato.
     * @throws IllegalArgumentException se l'annuncio non viene trovato o i dati forniti non sono validi.
     */
    Lavoro modificaAnnuncioLavoro(final long idAnnuncio,
                                  final HashMap<String, Object> aggiornamenti);

    /**
     * Elimina un annuncio di lavoro dal database.
     *
     * @param id ID dell'annuncio da eliminare.
     */
    void eliminaAnnuncioLavoro(long id);

}
