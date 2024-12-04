package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.Rifugiato;
import org.springframework.stereotype.Repository;

/**
 * @author Antonio Ceruso.
 * Data creazione: 04/12/2024.
 * Interfaccia che indica un DAO per il Rifugiato
 */
@Repository
public interface RifugiatoDAO extends UtenteDAO {
    /**
     * Query che ci permette di ricercare un Rifugiato dalla email
     * @param email del Rifugiato da ricercare
     * return Rifugiato corrispondente
     * */
    Rifugiato findByEmail(String email);
}
