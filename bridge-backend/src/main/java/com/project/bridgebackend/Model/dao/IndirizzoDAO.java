package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.Indirizzo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Benedetta Colella.
 * creato il 03/12/2024.
 * DAO della classe Indirizzo.
 */

@Repository
public interface IndirizzoDAO extends JpaRepository<Indirizzo, Long> {
}
