package com.project.bridgebackend.GestioneUtente.Service;

import com.project.bridgebackend.Model.Entity.Utente;
import com.project.bridgebackend.fotoProfilo.FotoProfilo;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Antonio Ceruso.
 * Data creazione: 12/12/2024
 * Interfaccia Service per l'utente e la sua area personale
 * */
public interface UtenteService {

    /**
     * Recupera la foto profilo di un utente dato il suo indirizzo email.
     *
     * @param email indirizzo email dell'utente.
     * @return l'oggetto FotoProfilo associato all'utente.
     * @throws IOException se si verifica un errore durante il recupero della foto.
     */
    FotoProfilo getFotoUtente(String email) throws IOException;

    /**
     * Elimina un utente dal sistema dato il suo indirizzo email.
     *
     * @param email indirizzo email dell'utente da eliminare.
     * @throws Exception se si verifica un errore durante l'eliminazione.
     */
    void eliminaUtente(String email) throws Exception;

    /**
     * Recupera un utente dal sistema dato il suo indirizzo email.
     *
     * @param email indirizzo email dell'utente.
     * @return l'oggetto Utente associato all'indirizzo email fornito.
     */
    Utente getUtente(String email);

    /**
     * Modifica la password di un utente dato il suo indirizzo email.
     *
     * @param email indirizzo email dell'utente.
     * @param password nuova password da impostare.
     * @throws Exception se si verifica un errore durante l'aggiornamento della password.
     */
    void modificaPassword(String email, String password) throws Exception;

    /**
     * Modifica la foto profilo di un utente.
     *
     * @param email indirizzo email dell'utente.
     * @param base64Image nuova immagine in formato Base64.
     * @throws IOException se si verifica un errore durante l'aggiornamento della foto.
     */
    void modificaFotoUtente(String email, String base64Image) throws IOException;

    /**
     * Modifica i dati di un utente.
     *
     * @param email indirizzo email dell'utente.
     * @param aggiornamenti mappa contenente le chiavi e i valori dei dati da aggiornare.
     * @return l'oggetto Utente aggiornato.
     * @throws IOException se si verifica un errore durante l'aggiornamento.
     */
    Utente modificaUtente(String email, HashMap<String, Object> aggiornamenti) throws IOException;

    /**
     * Modifica la disponibilità di un utente specializzato.
     *
     * @param email indirizzo email dell'utente.
     * @param disp nuova disponibilità da impostare.
     * @throws Exception se si verifica un errore durante l'aggiornamento della disponibilità.
     */
    void modificaDisp(String email, String disp) throws Exception;
}
