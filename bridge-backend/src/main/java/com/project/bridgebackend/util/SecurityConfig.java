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
    @Autowired
    private final JwtAuthenticationFilter authFilter;

    @Autowired
    private final AuthenticationProvider provider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(getPublicEndpoints()).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(provider)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

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

                // ANNUNCI (CONSULENZA E LAVORO)
                "/api/annunci/creaConsulenza",
                "/api/annunci/view_consulenze",
                "/api/annunci/view_consulenze/proprietario/{id}",
                "/api/annunci/view_consulenze/retrive/{id}",
                "/api/annunci/modifica_consulenza/{idConsulenza}",
                "/api/annunci/eliminaConsulenza/{id}",
                "/api/annunci/creaLavoro",
                "/api/annunci/view_lavori",
                "/api/annunci/view_lavori/proprietario/{id}",
                "/api/annunci/view_lavori/retrive/{id}",
                "/api/annunci/modifica_lavoro/{id}",
                "/api/annunci/elimina_lavoro/{id}",

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

                // AREA PERSONALE
                "/areaPersonale/DatiUtente/{email}",
                "/areaPersonale/elimina/{email}",
                "/areaPersonale/DatiFotoUtente/{email}",
                "/areaPersonale/modificaUtente/{email}",
                "/areaPersonale/modificaPassword/{email}",
                "/areaPersonale/modificaFotoUtente/{email}"
        };
    }

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
