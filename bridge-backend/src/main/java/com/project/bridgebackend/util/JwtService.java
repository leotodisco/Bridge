package com.project.bridgebackend.util;

import com.project.bridgebackend.Model.Entity.Utente;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Benedetta Colella
 * Creato il: 04/12/2024
 * Questa classe fornisce un servizio per la gestione dei token JWT (JSON Web Token), utilizzati per
 * autenticazione e autorizzazione nelle applicazioni. I principali compiti di questa classe sono:
 * - Generazione dei token JWT: Creazione di token firmati con una chiave segreta, includendo informazioni
 *   personalizzate (claims) come identificativo utente e ruolo.
 * - Validazione dei token JWT: Verifica che un token sia valido, non scaduto e appartenga all'utente corretto.
 * - Estrazione di informazioni dai token JWT: Recupero di dati come il subject (es. email) o la data di scadenza.
 * La chiave segreta utilizzata per firmare i token è configurata esternamente, migliorando la sicurezza e la
 * flessibilità. La durata del token è anch'essa configurabile, consentendo un controllo preciso sul ciclo di vita
 * dei token.
 * Principali caratteristiche:
 * - Sicurezza: Utilizza l'algoritmo HMAC-SHA256 per la firma dei token.
 * - Modularità: Separazione chiara tra generazione, validazione ed estrazione di informazioni.
 * - Configurabilità: Chiave segreta e durata del token sono gestite tramite file di configurazione.
 * Questo servizio è un componente essenziale per l'integrazione con meccanismi di autenticazione basati su JWT
 * in un'applicazione Spring.
 */


@Service
public class JwtService {

    private String secretKey = "UmdVa1hwMnI1dTh4L0E/RChHK0tiUGVTaFZtWXEzdDY="; // Chiave segreta configurata in application.properties

    /**
     * Metodo per la generazione di un token.
     * @param userDetails
     * @return token.
     */
    public String generateToken(Utente userDetails) {
        HashMap<String, Object> mappaClaims = new HashMap<>();
        mappaClaims.put("id", userDetails.getEmail());
        mappaClaims.put("ruolo", userDetails.getRole());
        return generateToken(mappaClaims, userDetails);
    }

    /**
     * Metodo per la generazione di un token.
     * Un token dura 24 ore.
     * Al suo interno, il token contiene: email, data di scadenza,
     * id, ruolo.
     * @param extraClaims
     * @param userDetails
     * @return token.
     *
     */
    public String generateToken(final Map<String, Object> extraClaims,
                                final Utente userDetails) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * 60 * 60 * 24);
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Metodo che restituisce l'email presente nel token.
     * @param token
     * @return username/password.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Estrae la data di scadenza dal token.
     * @param token Token JWT.
     * @return Data di scadenza.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Metodo che controlla che il token sia valido.
     * @param token
     * @param userDetails
     * @return booleano si o no.
     */
    public boolean isTokenValid(final String token,
                                final UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()))
                && !isTokenExpired(token);

    }

    /**
     * Metodo che controlla se il token è scaduto.
     * @param token
     * @return true o false.
     */
    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Estrae un claim specifico dal token.
     * @param token Token JWT.
     * @param claimsResolver Funzione per risolvere il claim.
     * @return Valore del claim.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Estrae tutti i claims dal token.
     * @param token Token JWT.
     * @return Tutti i claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Metodo che ottiene la chiave di firma utilizzata per verificare e firmare i token.
     * @return Key una chiave segreta basata sull'algoritmo HMAC.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
