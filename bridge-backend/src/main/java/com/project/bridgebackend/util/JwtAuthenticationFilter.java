package com.project.bridgebackend.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Benedetta Colella
 * Creato il: 04/12/2024
 * JwtAuthenticationFilter
 * Questo filtro intercetta ogni richiesta HTTP per verificare la presenza di un token JWT
 * nell'header "Authorization". Se il token è valido:
 * - Estrae l'username dal token.
 * - Carica i dettagli utente tramite `UserDetailsService`.
 * - Autentica l'utente impostando un oggetto di autenticazione nel contesto di sicurezza di Spring.
 * Funzionalità:
 * - Controlla il formato dell'header "Authorization" per assicurarsi che contenga un token valido.
 * - Utilizza il servizio `JwtService` per estrarre e validare il token JWT.
 * - Aggiorna il contesto di sicurezza con le credenziali dell'utente autenticato.
 * Specifiche tecniche:
 * - Estende `OncePerRequestFilter`, garantendo che il filtro venga eseguito una sola volta per ogni richiesta.
 * - Gestisce anche gli header CORS per supportare richieste provenienti da domini diversi.
 */


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * service.
     */
    @Autowired
    private JwtService jwtService;

    /**
     * user.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     *
     * @param request richiesta.
     * @param response risposta.
     * @param filterChain carena dei filtri che la richiesta attraversa.
     * @throws ServletException
     * @throws IOException
     */

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (!isValidAuthorizationHeader(authHeader)) {
            logger.debug("Invalid or missing Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String userEmail;

        try {
            userEmail = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            logger.error("Error extracting username from token", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            return;
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        setCorsHeaders(response);
        filterChain.doFilter(request, response);
    }

    private boolean isValidAuthorizationHeader(
            final String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }

    private void setCorsHeaders(
            final HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
    }
}
