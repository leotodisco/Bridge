package com.project.bridgebackend.GetioneAnnuncio.Service;

import com.project.bridgebackend.Model.Entity.Annuncio;
import com.project.bridgebackend.Model.Entity.Consulenza;
import com.project.bridgebackend.Model.Entity.Indirizzo;

import java.util.HashMap;


/**
 * @author Geraldine Montella.
 * Creato il: 04/12/2024.
 * Questa Java Persistence Entity per un annuncio,
 * la inheritance è settata a JOINED perciò Ogni entità ha la propria tabella,
 * e le tabelle sono unite tramite chiavi primarie.
 */
public interface GestioneAnnuncioService {
    /**
     * Funzione per l'inserimento di una consulenza nel db
     * @param consulenza ovvero l'annuncio che si vuole inserire
     *                   già correttamente validato e popolato
     * @return la consulenza appena inserita nel db
     */
    Consulenza inserimentoConsulenza(Consulenza consulenza);

    /**
     * Funzione per l'inserimento dell'indirizzo specifico per
     * la sede di consulenza
     * @param indirizzo ovvero dove la sede di consulenza è locata
     *                  tale struttura pre-popolata e validata
     * @return l'identificativo in long dell'indirizzo appena inserito
     */
    long salvaIndirizzoConsulenza(Indirizzo indirizzo);
}
