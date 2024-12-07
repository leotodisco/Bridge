package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.Volontario;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Antonio Ceruso.
 * Data creazione: 04/12/2024.
 * Interfaccia che indica un DAO per il Volontario.
 */
@Repository
public interface VolontarioDAO extends UtenteDAO {
    /**
     * Query che ci permette di ricercare un Volontario dalla email.
     * @param email del Volontario da ricercare.
     * @return Volontario corrispondente.
     * */
    Volontario findByEmail(String email);
}
