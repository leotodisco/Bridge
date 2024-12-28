package com.project.bridgebackend.registrazione.service;

import com.project.bridgebackend.Model.Entity.Admin;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.util.AuthenticationRequest;
import com.project.bridgebackend.util.AuthenticationResponse;

/**
 *@author: Antonio Ceruso.
 * Creato il: 04/12/2024.
 * Interfaccia per il service di registrazione.
 * */
public interface RegistrazioneService {
    /**
     * Metodo per la registrazione di un Volontario.
     * @param volontario è il volontario che viene registrato.
     * @param confermaPW conferma della password utente.
     * */
    void registraVolontario(Volontario volontario, String confermaPW)
            throws Exception;
    /**
     * Metodo per la registrazione di un Rifugiato.
     * @param rifugiato è il rifugiato che viene registrato.
     * @param confermaPW conferma della password utente.
     * */
    void registraRifugiato(Rifugiato rifugiato, String confermaPW)
            throws Exception;
    /**
     * Metodo per la registrazione di un admin.
     * @param admin è l'admin che viene registrato.
     * @param confermaPW conferma della password utente
     * */
    void registraAdmin(Admin admin, String confermaPW) throws Exception;
    /**
     * Metodo per la registrazione di una Figura Specializzata.
     * @param figspec è la figura specializzata che viene registrata.
     * @param confermaPW conferma della password utente
     * */
    void registraFiguraSpecializzata(FiguraSpecializzata figspec,
                                     String confermaPW)
            throws Exception;

    /**
     * Metodo per effettuare il login di un utente.
     * Riceve una richiesta contenente le credenziali,
     * dell'utente (email e password),
     * verifica l'autenticità delle credenziali,
     * e restituisce una risposta con le informazioni
     * di autenticazione, come un token JWT.
     *
     * @param request l'oggetto contenente le credenziali
     *                dell'utente (email e password).
     * @return un oggetto contenente il token JWT,
     *         o altre informazioni necessarie per l'autenticazione.
     * @throws Exception se le credenziali sono errate o si verifica un errore
     *                   durante il processo di autenticazione.
     */
    AuthenticationResponse login(AuthenticationRequest request) throws Exception;
}
