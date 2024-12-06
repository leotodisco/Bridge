package com.project.bridgebackend.Utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Benedetta Colella.
 * Creato il: 07/12/2024.
 * Classe che indica la risposta a un login avvenuto con successo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    /**
     * Token.
     */
    private String token;
}

