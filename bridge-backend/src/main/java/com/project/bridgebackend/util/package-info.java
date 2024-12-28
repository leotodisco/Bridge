/**
 * Contiene tutte le classi e i componenti relativi alla configurazione,
 * dell'autenticazione,
 * e alla gestione della sicurezza nell'applicazione.
 * <p>
 * Questo pacchetto implementa la logica per la gestione dell'autenticazione,
 * basata su token JWT,
 * la configurazione della sicurezza, e la protezione degli endpoint API.
 * </p>
 * <ul>
 * <li><strong>ApplicationConfig</strong>:
 *      Configura i bean principali dell'applicazione, inclusi il
 * servizio di autenticazione e la gestione delle email.</li>
 * <li><strong>AuthenticationRequest</strong>:
 *      Modello di richiesta per l'autenticazione utente, contenente
 * le credenziali dell'utente (email e password).</li>
 * <li><strong>AuthenticationResponse</strong>:
 *      Modello di risposta che contiene il token JWT generato
 * per l'autenticazione dell'utente.</li>
 * <li><strong>JwtAuthenticationFilter</strong>:
 *      Filtro che intercetta le richieste HTTP e verifica
 * la validità del token JWT.</li>
 * <li><strong>JwtService</strong>:
 *      Servizio che gestisce la generazione, l'estrazione e la validazione
 * dei token JWT.</li>
 * <li><strong>SecurityConfig</strong>:
 *      Configurazione della sicurezza dell'applicazione, inclusi i filtri
 * di autenticazione, la gestione delle sessioni come stateless e la configurazione,
 * CORS.</li>
 * </ul>
 * <p>
 * Questo pacchetto è essenziale per la gestione della sicurezza,
 * e dell'autenticazione nell'applicazione,
 * fornendo un'implementazione basata su token JWT per proteggere,
 * gli endpoint e garantire un accesso sicuro.
 * </p>
 */
package com.project.bridgebackend.util;
