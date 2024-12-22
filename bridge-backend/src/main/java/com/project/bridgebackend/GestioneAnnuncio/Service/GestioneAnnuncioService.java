package com.project.bridgebackend.GestioneAnnuncio.Service;



import com.project.bridgebackend.Model.Entity.Consulenza;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Lavoro;
import com.project.bridgebackend.Model.Entity.Utente;
import java.util.HashMap;
import java.util.List;

/**
 * Servizio per la gestione degli annunci di lavoro e consulenza.
 * Include operazioni di inserimento, modifica, eliminazione e gestione delle preferenze.
 *
 * @author Geraldine Montella, Vito Vernellati
 * creato in data 04/12/2024
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

    /**
     * Prende tutte le consulenze presenti nel DB.
     * @return la lista contente tutte le consulenze.
     */
    List<Consulenza> getAllConsulenze();

    /**
     * Prende tutte le consulenze presenti nel DB di un certo utente.
     *
     * @param proprietario il proprietario di cui si vogliono,
     *                     estrarre tutti gli annunci di consulenze,
     *                     pubblicati.
     * @return lista di annunci di consulenze pubblicati dall'utente proprietario
     */
    List<Consulenza> getConsulenzeByProprietario(Utente proprietario);

    /**
     * Prende una consulenze specifica nel DB.
     *
     * @param id intero identificativo della consulenza che si vuole ottenere.
     * @return consulenza avente nel db l'id specificato se presente.
     */
    Consulenza getConsulenze(long id);

    /**
     * Funzione per aggiornare una certa consulenza sulla base delle informazioni passate.
     *
     * @param idConsulenza intero identificativo della consulenza che si vuole modificare.
     * @param aggiornamenti nuove informazioni con cui si vuole aggiornare la consulenza.
     *
     * @return consulenza aggiornata con le nuove modifiche
     */
    Consulenza modificaAnnuncioConsulenza(long idConsulenza,
                                          HashMap<String, Object> aggiornamenti);

    /**
     * Funzione per l'eliminazione di una consulenza sulla base,
     * dell'identificativo passato.
     * @param idConsulenza identificativo della consulenza che,
     *                     si vuole eliminare.
     */

    void eliminaConsulenza(long idConsulenza);
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
     */
    Lavoro modificaAnnuncioLavoro(long idAnnuncio, HashMap<String, Object> aggiornamenti);

    /**
     * Elimina un annuncio di lavoro dal database.
     *
     * @param idAnnuncio L'identificativo unico dell'annuncio di lavoro da eliminare.
     */
    void eliminaAnnuncioLavoro(long idAnnuncio);


    /**
     * Recupera in maniera casuale 5 annunci di lavoro.
     * @return Una lista di 5 oggetti `Lavoro` contenenti annunci di lavoro casuali.
     */
    List<Lavoro> getRandomLavori();
}
