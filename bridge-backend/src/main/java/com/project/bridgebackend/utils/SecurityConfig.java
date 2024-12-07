package com.project.bridgebackend.utils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
/**
 * @author Antonio Ceruso.
 * @co-author Geraldine Montella.
 * Data creazione: 05/12/2024.
 * Classe di configurazione, viene richiamata da Spring automaticamente.
 * */
@Configuration
public class SecurityConfig {

    /**
     * Mertodo che permette di aggiungere filtri a una request.
     * @param http
     * @return l'insieme dei filtri che la richiesta http deve attraversare.
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
                                                throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disabilita CSRF per API REST
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults()); // Abilita l'autenticazione Basic per le API
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

     @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http)
                                                        throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .inMemoryAuthentication()
                .withUser("user").password(passwordEncoder().
                        encode("password")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder()
                        .encode("adminpassword")).roles("ADMIN");

        return authenticationManagerBuilder.build();
    }
}


