package com.project.bridgebackend.registrazione.service;

import com.project.bridgebackend.Model.Entity.Admin;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.Entity.Volontario;

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
}
