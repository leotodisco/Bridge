package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import org.springframework.stereotype.Repository;

/**
 * @author Antonio Ceruso.
 * Data creazione: 04/12/2024.
 * Interfaccia che indica un DAO per la Figura Specializzata
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
