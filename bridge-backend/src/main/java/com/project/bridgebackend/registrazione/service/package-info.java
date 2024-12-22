/**
 * Questo package contiene le classi e le interfacce relative alla gestione
 * del servizio di registrazione degli utenti nell'applicazione.
 * <p>
 * Include:
 * <ul>
 *   <li>{@link com.project.bridgebackend.registrazione.service.RegistrazioneService}
 *   : Interfaccia che definisce i metodi per la registrazione e il login degli utenti.</li>
 *   <li>{@link com.project.bridgebackend.registrazione.service.RegistrazioneServiceImpl}
 *   : Implementazione dell'interfaccia che si occupa della logica di registrazione,
 *   e autenticazione degli utenti, con il supporto di token JWT.</li>
 * </ul>
 * </p>
 * <p>
 * La registrazione supporta diversi ruoli come admin, volontario, rifugiato e figura specializzata.
 * Inoltre, il login genera e restituisce un token JWT per l'autenticazione dell'utente.
 * </p>
 *
 */
package com.project.bridgebackend.registrazione.service;
