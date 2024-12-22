package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import org.springframework.stereotype.Repository;

/**
 * @author Antonio Ceruso.
 * Data creazione: 04/12/2024.
 * Repository per la gestione degli oggetti {@link FiguraSpecializzata}.
 * Fornisce metodi per l'accesso e la manipolazione dei,
 * dati relativi agli utenti figura specializzata.
 * Utilizza Spring Data JPA per fornire implementazioni automatiche,
 * delle query.
 */
@Repository
public interface FiguraSpecializzataDAO extends UtenteDAO {
    /**
     * Query che ci permette di ricercare una Figura Specializzata dalla email.
     * @param email della Figura Specializzata da ricercare.
     * @return Figura Specializzata corrispondente.
     * */
    FiguraSpecializzata findByEmail(String email);
}
