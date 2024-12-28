package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.Admin;

/**
 *
 * @author Benedetta Colella.
 * Data creazione: 05/12/2024
 * Repository per la gestione degli oggetti {@link Admin}.
 * Fornisce metodi per l'accesso e la manipolazione dei,
 * dati relativi agli utenti admin.
 * Utilizza Spring Data JPA per fornire implementazioni automatiche,
 * delle query.
 */

public interface AdminDAO extends UtenteDAO {
    /**
     * Query che ci permette di ricercare un Admin dalla email.
     * @param email dell'Admin da ricercare
     * @return Admin corrispondente
     * */
    Admin findByEmail(String email);

}
