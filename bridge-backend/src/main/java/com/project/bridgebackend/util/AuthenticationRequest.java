package com.project.bridgebackend.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Benedetta Colella
 * Creato il: 04/12/2024
 * Questa classe rappresenta il modello per una richiesta di autenticazione  durante il login.
 * Contiene le credenziali necessarie, come email e password,
 * inviate dall'utente al server per ottenere un token JWT.
 *
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    /**
     * password dell'utente.
     */
    private String password;
    /**
     * email utente.
     */
    private String email;
}
