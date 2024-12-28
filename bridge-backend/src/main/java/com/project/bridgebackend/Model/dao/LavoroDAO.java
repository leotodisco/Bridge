package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.Lavoro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.bridgebackend.Model.Entity.Utente;
import java.util.List;

/**
 * @author Vito Vernellati.
 * Creato il: 04/12/2024.
 * Repository per la gestione degli oggetti {@link Lavoro}.
 * Fornisce metodi per l'accesso e la manipolazione dei,
 * dati relativi agli annunci di lavoro.
 * Utilizza Spring Data JPA per fornire implementazioni automatiche,
 * delle query.
 */

@Repository
public interface LavoroDAO extends JpaRepository<Lavoro, Long> {

    /**
     * Recupera una lista di lavori associati a un determinato proprietario.
     * Questo metodo esegue una query per ottenere tutti i lavori che sono
     * associati a un utente specifico, identificato dal parametro "proprietario".
     *
     * @param proprietario l'utente proprietario dei lavori da recuperare.
     * @return una lista di annunci di lavoro
     * associati al proprietario. La lista potrebbe essere,
     * vuota se non esistono lavori per il proprietario.
     */
    List<Lavoro> findByProprietario(Utente proprietario);
}
