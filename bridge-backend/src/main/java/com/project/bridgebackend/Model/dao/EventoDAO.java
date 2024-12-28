package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.Evento;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.Entity.enumeration.Lingua;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author Alessia De Filippo.
 * Creato il 03/12/2024.
 * Repository per la gestione degli oggetti {@link Evento}.
 * Fornisce metodi per l'accesso e la manipolazione dei,
 * dati relativi agli eventi.
 * Utilizza Spring Data JPA per fornire implementazioni automatiche,
 * delle query. */

@Repository
public interface EventoDAO extends JpaRepository<Evento, Long> {

    /**
     * Trova eventi per lingua parlata.
     * @param lingua lista di lingue dell'evento.
     * @return lista di eventi.
     */
    List<Evento> findByLinguaParlata(Lingua lingua);


    /**
     * Restituisce gli eventi svolti in determinata data.
     * @param data Data dell'evento.
     * @return Lista di eventi.
     */
    List<Evento> findByData(LocalDate data);

    /**
     * Restituisce gli eventi che si svolgono in un determinato luogo.
     * @param luogo Luogo dell'evento.
     * @return Lista di eventi.
     */
    List<Evento> findByLuogo(Indirizzo luogo);

    /**
     * Restituisce gli eventi organizzati da un determinato volontario.
     * @param organizzatore Volontario organizzatore dell'evento.
     * @return Lista di eventi.
     */
    List<Evento> findByOrganizzatore(Volontario organizzatore);

    /**
     * Elimina gli eventi con data precedente a oggi.
     * @param dataBefore Data per l'eliminazione.
     */
    @Transactional
    void deleteByDataBefore(LocalDate dataBefore);

    /**
     * Restituisce gli eventi con spazio disponibile.
     * @return Lista di evento.
     */
    @Query("SELECT e FROM Evento e WHERE "
            + "SIZE(e.listaPartecipanti) < e.maxPartecipanti")
    List<Evento> findEventiConSpazioDisponibile();

    /**
     * Questo metodo esegue una query per ottenere l'evento specificato,
     * e carica anche la lista dei partecipanti,
     * attraverso una join con la tabella relativa.
     * L'evento restituito conterrà tutte le informazioni necessarie,
     * sui partecipanti associati.
     *
     * @param eventoId l'ID dell'evento da recuperare.
     * @return la lista dei partecipanti.
     * Se non esiste un evento con l'ID fornito, il metodo restituirà null.
     */
    @Query("SELECT e FROM Evento e JOIN FETCH e.listaPartecipanti WHERE e.id = :eventoId")
    Optional<Evento> findEventoWithPartecipanti(long eventoId);

    /**
     * Questo metodo esegue una query per ottenere una lista di eventi,
     * e carica anche le informazioni relative,
     * agli organizzatori di ciascun evento attraverso una join.
     * Gli eventi restituiti conterranno tutte le informazioni
     * sugli organizzatori associati.
     *
     * @return una lista di eventi, ciascuno con il proprio organizzatore.
     *         La lista potrebbe essere vuota se non ci sono eventi nel sistema.
     */
    @Query("SELECT e FROM Evento e JOIN Fetch e.organizzatore")
    List<Evento> findAllWithOrganizzatore();
}
