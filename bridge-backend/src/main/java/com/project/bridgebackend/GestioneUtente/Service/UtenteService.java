package com.project.bridgebackend.GestioneUtente.Service;

import com.project.bridgebackend.Model.Entity.Utente;
import com.project.bridgebackend.fotoProfilo.FotoProfilo;

import java.io.IOException;

/**
 * @author Antonio Ceruso
 * Data creazione: 12/12/2024
 * Interfaccia Service per l'utente e la sua area personale
 * */
public interface UtenteService {
    FotoProfilo getFotoUtente(String email) throws IOException;
    void eliminaUtente(String email) throws Exception;
    Utente getUtente(String email);
}
