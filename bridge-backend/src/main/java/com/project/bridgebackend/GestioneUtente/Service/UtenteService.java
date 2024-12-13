package com.project.bridgebackend.GestioneUtente.Service;

import com.project.bridgebackend.Model.Entity.Utente;

public interface UtenteService {

    void eliminaUtente(String email) throws Exception;
    Utente getUtente(String email);
}
