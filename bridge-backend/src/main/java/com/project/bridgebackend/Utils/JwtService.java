package com.project.bridgebackend.Utils;

import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author Benedetta Colella
 * Creato il: 04/12/2024
 * Questa classe fornisce metodi per generare, validare e decodificare
 * i token JWT. Utilizza una chiave segreta per firmare i token e include
 * informazioni come email, ruolo e altre proprietà utente.
 * È responsabile di:
 * - Creare token con claims personalizzati
 * - Estrarre informazioni dai token
 * - Verificare la validità dei token
 */

@Service
public class JwtService {

    /**
     * Chiave segreta per firmare i token.
     */
    private static final String SECRET_KEY =
            "5267556B58703272357538782F413F4428472B4B6250655368566D5971337436";

    /**
     * Estrae l'email dall'header del token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Estrae un claim specifico dal token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Estrae tutti i claims dal token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    /**
     * Verifica se il token è scaduto.
     */

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Estrae la data di scadenza dal token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Genera un token JWT con claims personalizzati.
     */

    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("role", role);
        return createToken(claims, email);
    }

    /**
     * Crea un token JWT con claims personalizzati.
     */


