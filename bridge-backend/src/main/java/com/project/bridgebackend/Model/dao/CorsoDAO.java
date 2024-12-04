package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.Corso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @author Biagio Gallo.
 * Creato il 04/12/2024.
 * Questa classe rappresenta il DAO della classe Corso.
 */


@Repository
public interface CorsoDAO extends JpaRepository<Corso, Long> {
    /**
     * Questo metodo permette di trovare un corso tramite il suo id.
     * @param id id del corso da trovare.
     * @return il corso trovato.
     */
    Corso findById(long id);
}
