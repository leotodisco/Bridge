package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.Rifugiato;
import org.springframework.stereotype.Repository;

/**
 * @author Antonio Ceruso.
 * Data creazione: 04/12/2024.
 * Interfaccia che indica un DAO per l'admin
 */
@Repository
public interface AdminDAO extends UtenteDAO {
    /**
     * Query che ci permette di ricercare un admin dalla email.
     * @param email dell'admin da ricercare.
     * @return Admin corrispondente.
     * */
    Rifugiato findByEmail(String email);
}
