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
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configurazione CORS esplicita
                .csrf(csrf -> csrf.disable()) // Disabilita CSRF esplicitamente
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/authentication/login", "/authentication/registrazioneUtente",
                                "/eventi/crea", "/api/corsi/crea", "/api/annunci/creaConsulenza",
                                "/api/corsi/upload", "/alloggi/aggiungi", "/api/annunci/creaLavoro",
                                "/api/annunci/view_consulenze", "/api/annunci/view_consulenze/proprietario/{id}",
                                "/api/eventi/all", "/api/eventi/retrieve/{id}", "/api/eventi/{id}/iscrivi",
                                "/api/eventi/{id}/disiscrivi", "/api/eventi/pubblicati", "/areaPersonale/{email}").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(provider)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5174", "http://localhost:5173", "https://your-production-domain.com", "http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
