package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.Corso;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.enumeration.Lingua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author Biagio Gallo.
 * Creato il 04/12/2024.
 * Repository per la gestione degli oggetti {@link Corso}.
 * Fornisce metodi per l'accesso e la manipolazione dei,
 * dati relativi ai corsi.
 * Utilizza Spring Data JPA per fornire implementazioni automatiche,
 * delle query.
 */


@Repository
public interface CorsoDAO extends JpaRepository<Corso, Long> {

    /**
     * Questo metodo permette di trovare un corso tramite il suo titolo.
     * @param titolo titolo del corso da trovare.
     * @return il corso trovato.
     */
    Corso findByTitolo(String titolo);

    /**
     * Questo metodo permette di trovare i corsi tramite l'email del proprietario.
     * @param email email del proprietario.
     * @return lista dei corsi trovati.
     */
    List<Corso> findByProprietario_Email(String email);

    /**
     * Questo metodo permette di trovare un corso tramite la lingua.
     * @param lingua lingua del corso da trovare.
     * @return il corso trovato.
     */
    Corso findByLingua(Lingua lingua);
}
