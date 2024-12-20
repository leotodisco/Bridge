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
 * DAO della classe Evento.
 */

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
     *
     */
    @Query("SELECT e FROM Evento e JOIN FETCH e.listaPartecipanti WHERE e.id = :eventoId")
    Optional<Evento> findEventoWithPartecipanti(long eventoId);

    @Query("SELECT e FROM Evento e JOIN Fetch e.organizzatore")
    List<Evento> findAllWithOrganizzatore();
}
