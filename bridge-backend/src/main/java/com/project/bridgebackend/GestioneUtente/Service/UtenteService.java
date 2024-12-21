package com.project.bridgebackend.GestioneUtente.Service;

import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Utente;
import com.project.bridgebackend.fotoProfilo.FotoProfilo;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Antonio Ceruso
 * Data creazione: 12/12/2024
 * Interfaccia Service per l'utente e la sua area personale
 * */
public interface UtenteService {
    FotoProfilo getFotoUtente(String email) throws IOException;
    void eliminaUtente(String email) throws Exception;
    Utente getUtente(String email);
    void modificaPassword(String email, String password) throws Exception;
    void modificaFotoUtente(String email, String base64Image) throws IOException;
    Utente modificaUtente(String email, HashMap<String, Object> aggiornamenti) throws IOException;
    void modificaDisp(final String email, final String disp) throws Exception;
}
