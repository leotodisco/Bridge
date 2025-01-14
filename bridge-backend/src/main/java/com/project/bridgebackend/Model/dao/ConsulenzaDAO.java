package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Consulenza;
import com.project.bridgebackend.Model.Entity.Utente;
import com.project.bridgebackend.Model.Entity.enumeration.TipoConsulenza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Geraldine Montella.
 * Creato il: 03/12/2024.
 * Repository per la gestione degli oggetti {@link Consulenza}.
 * Fornisce metodi per l'accesso e la manipolazione dei,
 * dati relativi alle consulenze.
 * Utilizza Spring Data JPA per fornire implementazioni automatiche,
 * delle query.
 */
@Repository
public interface ConsulenzaDAO extends JpaRepository<Consulenza, Long> {
       /**
        * Questo metodo effettua una ricerca delle consulenze,
        * che sono collegate all'utente specificato
        * come proprietario.
        *
        * @param proprietario l'utente che è il proprietario,
        *                     delle consulenze da recuperare.
        * @return una lista di consulenze associate al proprietario,
        * specificato. La lista potrebbe essere vuota se
        * non ci sono consulenze associate a quel proprietario.
        */
       List<Consulenza> findByProprietario(Utente proprietario);

       @Query("SELECT c FROM Consulenza c WHERE c.id =:id")
       Consulenza findConsulenzaById(long id);

       @Query("SELECT c FROM Consulenza c WHERE c.tipo =:tipo")
       List<Consulenza> findByTipo(TipoConsulenza tipo);

       /**
        * Recupera tutte le consulenze a cui un rifugiato si è candidato,
        * filtrando in base all'email.
        *
        * @param rifugiatoEmail l'email del rifugiato.
        * @return la lista delle consulenze corrispondenti.
        */
       @Query("SELECT c FROM Consulenza c JOIN c.candidati cand WHERE KEY(cand) = :rifugiatoEmail")
       List<Consulenza> findAllByRifugiatoEmail(String rifugiatoEmail);

       /**
        * Recupera tutte le consulenze a cui un rifugiato si è candidato,
        * filtrando in base all'email e allo stato della candidatura.
        *
        * @param rifugiatoEmail l'email del rifugiato.
        * @param stato lo stato della candidatura (true o false).
        * @return la lista delle consulenze corrispondenti.
        */
       @Query("SELECT c FROM Consulenza c JOIN c.candidati cand WHERE KEY(cand) = :rifugiatoEmail AND VALUE(cand) = :stato")
       List<Consulenza> findAllByRifugiatoEmailAndStato(String rifugiatoEmail, Boolean stato);
}
