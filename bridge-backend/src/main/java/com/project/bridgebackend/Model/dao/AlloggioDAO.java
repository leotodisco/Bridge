package com.project.bridgebackend.Model.dao;
import com.project.bridgebackend.Model.Entity.Alloggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author : Mario Zurolo
 * created: 3/12/24
 * Repository per la gestione degli oggetti {@link Alloggio}.
 * Fornisce metodi per l'accesso e la manipolazione dei,
 * dati relativi agli alloggi.
 * Utilizza Spring Data JPA per fornire implementazioni automatiche,
 * delle query.
 */
@Repository
public interface AlloggioDAO extends JpaRepository<Alloggio, Long> {
    /**
     * Recupera tutti gli alloggi con il loro proprietario associato.
     * Utilizza un JOIN FETCH per caricare il proprietario insieme agli alloggi.
     *
     * @return una lista di alloggi, ciascuno con il proprio proprietario.
     */
    @Query("SELECT a FROM Alloggio a JOIN FETCH a.proprietario")
    List<Alloggio> findAllAlloggiWithProprietario();

    /**
     * Trova un alloggio in base al suo titolo utilizzando il metodo fornito da Spring Data JPA.
     *
     * @param titolo titolo dell'alloggio da trovare.
     * @return un {@link Optional} contenente l'alloggio se trovato, altrimenti vuoto.
     */
    Optional<Alloggio> findAlloggioByTitolo(String titolo);

    @Query("SELECT a FROM Alloggio a WHERE a.id =:id")
    Alloggio findAlloggioById(long id);

    @Query("SELECT a FROM Alloggio a WHERE a.proprietario.email = :email")
    List<Alloggio> getAllAlloggiByEmail(@Param("email") String email);

    @Query("SELECT a FROM Alloggio a JOIN a.listaCandidati r WHERE r.email = :email")
    List<Alloggio> findAllAlloggiByRifugiatoEmail(@Param("email") String email);

    @Query("SELECT a FROM Alloggio a")
    List<Alloggio> findMyAll();
}
