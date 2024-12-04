package com.project.bridgebackend.Utils;

import com.project.bridgebackend.Model.Entity.Utente;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 *
 * @author Benedetta Colella
 * Creato il: 04/12/2024
 * Questo filtro intercetta ogni richiesta HTTP per verificare se è presente
 * un token JWT nell'header di autorizzazione. Se il token è valido, il filtro
 * autentica l'utente e aggiorna il contesto di sicurezza di Spring.
 * È un filtro di tipo OncePerRequestFilter, quindi viene eseguito una sola
 * volta per ogni richiesta.
 *
 */

@Component // Spring lo riconosce come un componente
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /**
     * jwtService.
     */
    private final JwtService jwtService;
    /**
     * userDetailsService.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Questo metodo è chiamato per ogni richiesta HTTP.
     * Controlla se il token è presente nell'header di autorizzazione.
     * Se il token è valido, autentica l'utente e aggiorna il contesto di sicurezza.
     *
     * @param request richiesta HTTP
     * @param response risposta HTTP
     * @param filterChain catena dei filtri
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        // Estrae l'header di autenticazione dalla richiesta
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Se l'header di autenticazione è nullo o non inizia con "Bearer ", lascia passare la richiesta senza autenticazione
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Estrae il token JWT dall'header di autenticazione (rimuovendo prefisso "Bearer ")
        jwt = authHeader.substring(7);

        // Estrae l'indirizzo email dal token
        userEmail = jwtService.extractUsername(jwt);

        // Se l'indirizzo email è valido e l'utente non è già autenticato, autentica l'utente
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carica i dettagli dell'utente dal servizio UserDetailsService
            var userDetails = (Utente) this.userDetailsService.loadUserByUsername(userEmail);

            // Se il token è valido, autentica l'utente e aggiorna il contesto di sicurezza
            if (jwtService.isTokenValid(jwt, userDetails)) {
                var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Aggiunge i dettagli della richiesta corrente
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
    }
}
