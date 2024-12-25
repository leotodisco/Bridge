package com.project.bridgebackend.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * @author Benedetta Colella
 * Creato il: 04/12/2024
 * Questa classe configura Spring Security per proteggere l'applicazione.
 * Specifica:
 * - Gli endpoint pubblici e quelli protetti
 * - La gestione delle sessioni come stateless
 * - L'integrazione del filtro JWT per l'autenticazione
 * - Configurazioni CORS per abilitare richieste da client frontend.
 * È il cuore della sicurezza applicativa basata su token.
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    /**
     * Filtro per l'autenticazione tramite JWT.
     */
    @Autowired
    private final JwtAuthenticationFilter authFilter;

    /**
     * Fornitore di autenticazione per la gestione della logica di autenticazione.
     */
    @Autowired
    private final AuthenticationProvider provider;

    /**
     * Configura la sicurezza HTTP dell'applicazione, incluse le autorizzazioni degli endpoint,
     * la gestione delle sessioni come stateless e l'integrazione del filtro JWT.
     *
     * @param http Oggetto HttpSecurity per configurare la sicurezza HTTP.
     * @return SecurityFilterChain configurato per la protezione degli endpoint.
     * @throws Exception Se si verificano errori durante la configurazione della sicurezza.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/authentication/login", "/authentication/registrazioneUtente",
                                "/api/eventi/crea", "/api/corsi/crea", "/api/annunci/creaConsulenza",
                                "/api/corsi/upload", "/alloggi/aggiungi", "/api/annunci/creaLavoro", "/api/annunci/view_lavori",
                                "api/annunci/view_lavori/proprietario/{id}", "/api/annunci/view_lavori/retrieve/{id}",
                                "/api/annunci/modifica_lavoro/{id}", "/api/annunci/elimina_lavoro/{id}",
                                "/api/annunci/view_consulenze", "/api/annunci/view_consulenze/proprietario/{id}",
                                "/api/eventi/all", "/api/eventi/retrieve/{id}", "/api/eventi/{id}/iscrivi",
                                "/api/eventi/{id}/disiscrivi","/api/eventi/{id}/partecipanti",  "/api/eventi/pubblicati", "/areaPersonale/elimina/{email},",
                                "/api/annunci/view_consulenze/retrieve/{id}","/api/annunci/modifica_consulenza/{idConsulenza}",
                                "/api/eventi/{id}/iscrizione", "/api/corsi/cerca/{id}", "/api/corsi/modifica/{id}",
                                "/api/corsi/listaCorsi","/api/corsi/listaCorsiUtente/{email}","/areaPersonale/DatiUtente/{email}","/alloggi/mostra","/alloggi/preferiti","/alloggi/isFavorito",
                                "/alloggi/SingoloAlloggio/{titolo}",
                                "/api/corsi/download/{id}","/areaPersonale/DatiFotoUtente/{email}",
                                "/api/eventi/{id}/iscrizione", "/api/corsi/cerca/{id}",
                                "/api/corsi/modifica/{id}",
                                "/api/corsi/listaCorsi","/areaPersonale/DatiUtente/{email}","/api/corsi/download/{id}",
                                "/areaPersonale/DatiFotoUtente/{email}","/areaPersonale/modificaUtente/{email}",
                                "/areaPersonale/modificaPassword/{email}","/areaPersonale/modificaFotoUtente/{email}",
                                "/api/annunci/manifestazione-interesse/{idConsulenza}",
                                "/api/annunci/rimuovi-interesse/{idConsulenza}",
                                "/api/annunci/pubblicati").permitAll()
                        .requestMatchers(getPublicEndpoints()).permitAll()
                        .requestMatchers("/authentication/login", "/authentication/registrazioneUtente",
                                "/api/eventi/crea", "/api/corsi/crea", "/api/annunci/creaConsulenza",
                                "/api/corsi/upload", "/alloggi/aggiungi", "/api/annunci/creaLavoro", "/api/annunci/view_lavori",
                                "api/annunci/view_lavori/proprietario/{id}", "/api/annunci/view_lavori/retrieve/{id}",
                                "/api/annunci/modifica_lavoro/${id}", "/api/annunci/elimina_lavoro/{id}",
                                "/api/annunci/view_consulenze", "/api/annunci/view_consulenze/proprietario/{id}",
                                "/api/eventi/all", "/api/eventi/retrieve/{id}", "/api/eventi/{id}/iscrivi",
                                "/api/eventi/{id}/disiscrivi", "/api/eventi/pubblicati", "/areaPersonale/elimina/{email},",
                                "/api/annunci/view_consulenze/retrieve/{id}","/api/annunci/modifica_consulenza/{idConsulenza}",
                                "/api/eventi/{id}/iscrizione","/alloggi/mostra","/alloggi/SingoloAlloggio/{titolo}", "/api/corsi/cerca/{id}", "/api/corsi/modifica/{id}",
                                "/api/corsi/listaCorsi","/areaPersonale/DatiUtente/{email}","/api/annunci/pubblicati",
                                "/api/annunci/manifestazione-interesse/{idConsulenza}", "/api/annunci/rimuovi-interesse/{idConsulenza}").permitAll()
                        .requestMatchers(getPublicEndpoints()).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(provider)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Restituisce una lista di endpoint pubblici che non richiedono autenticazione.
     *
     * @return Un array di endpoint pubblici.
     */
    private String[] getPublicEndpoints() {
        return new String[]{
                // AUTENTICAZIONE
                "/authentication/login",
                "/authentication/registrazioneUtente",

                // EVENTi
                "/api/eventi/crea",
                "/api/eventi/all",
                "/api/eventi/retrieve/{id}",
                "/api/eventi/{id}/iscrivi",
                "/api/eventi/{id}/disiscrivi",
                "/api/eventi/pubblicati",
                "/api/eventi/{id}/iscrizione",
                "/api/eventi/random",

                // ANNUNCI (CONSULENZA E LAVORO)
                "/api/annunci/creaConsulenza",
                "/api/annunci/view_consulenze",
                "/api/annunci/view_consulenze/proprietario/{id}",
                "/api/annunci/view_consulenze/retrive/{id}",
                "/api/annunci/modifica_consulenza/{idConsulenza}",
                "/api/annunci/eliminaConsulenza/{id}",
                "/api/annunci/manifestazione-interesse/{idConsulenza}",
                "/api/annunci/verifica-candidato/{idConsulenza}",
                "/api/annunci/rimuovi-interesse/{idConsulenza}",
                "/api/annunci/pubblicati",
                "/api/annunci/creaLavoro",
                "/api/annunci/view_lavori",
                "/api/annunci/view_lavori/proprietario/{id}",
                "/api/annunci/view_lavori/retrive/{id}",
                "/api/annunci/modifica_lavoro/{id}",
                "/api/annunci/elimina_lavoro/{id}",
                "/api/annunci/random",

                // CORSI
                "/api/corsi/crea",
                "/api/corsi/upload",
                "/api/corsi/cerca/{id}",
                "/api/corsi/modifica/{id}",
                "/api/corsi/listaCorsi",
                "/api/corsi/download/{id}",

                // ALLOGGI
                "/alloggi/aggiungi",
                "/alloggi/mostra",
                "/alloggi/preferiti",
                "/alloggi/isFavorito",
                "/alloggi/SingoloAlloggio/{titolo}",
                "/alloggi/interesse",
                "/api/alloggi/random",

                // AREA PERSONALE
                "/areaPersonale/DatiUtente/{email}",
                "/areaPersonale/elimina/{email}",
                "/areaPersonale/DatiFotoUtente/{email}",
                "/areaPersonale/modificaUtente/{email}",
                "/areaPersonale/modificaPassword/{email}",
                "/areaPersonale/modificaFotoUtente/{email}"
        };
    }

    /**
     * Configura le impostazioni CORS per consentire richieste da client specifici
     * e gestire correttamente le intestazioni e i metodi HTTP supportati.
     *
     * @return Un oggetto CorsConfigurationSource configurato per l'applicazione.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5174",
                "http://localhost:5173",
                "https://your-production-domain.com"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
