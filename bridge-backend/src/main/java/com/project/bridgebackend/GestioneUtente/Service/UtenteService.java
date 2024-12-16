package com.project.bridgebackend.GestioneUtente.Service;

import com.project.bridgebackend.Model.Entity.Utente;
/**
 * @author Antonio Ceruso
 * Data creazione: 12/12/2024
 * Interfaccia Service per l'utente e la sua area personale
 * */
public interface UtenteService {

    void eliminaUtente(String email) throws Exception;
    Utente getUtente(String email);
}
