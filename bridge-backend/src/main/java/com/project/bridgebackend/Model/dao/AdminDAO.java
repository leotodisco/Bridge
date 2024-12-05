package com.project.bridgebackend.Model.dao;


import com.project.bridgebackend.Model.Entity.Admin;

/**
 *
 * @author Benedetta Colella.
 * Data creazione: 05/12/2024
 * Interfaccia che indica un DAO per l'Admin
 */

public interface AdminDAO extends UtenteDAO {
    /**
     * Query che ci permette di ricercare un Admin dalla email.
     * @param email dell'Admin da ricercare
     * @return Admin corrispondente
     * */
    Admin findByEmail(String email);

}