package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.Indirizzo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Benedetta Colella.
 * creato il 03/12/2024.
 * Repository per la gestione degli oggetti {@link Indirizzo}.
 * Fornisce metodi per l'accesso e la manipolazione dei,
 * dati relativi agli indirizzi.
 * Utilizza Spring Data JPA per fornire implementazioni automatiche,
 * delle query.  */

@Repository
public interface IndirizzoDAO extends JpaRepository<Indirizzo, Long> {

}
