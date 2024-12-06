package com.project.bridgebackend.Controller;

import com.project.bridgebackend.Model.Entity.Utente;
import com.project.bridgebackend.Utils.AuthenticationRequest;
import com.project.bridgebackend.Utils.AuthenticationResponse;
import com.project.bridgebackend.Utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        // Autentica le credenziali dell'utente
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Carica i dettagli dell'utente
        var userDetails = (Utente) userDetailsService.loadUserByUsername(request.getEmail());

        // Genera il token JWT
        var jwtToken = jwtService.generateToken(userDetails);

        // Restituisci il token come risposta
        return ResponseEntity.ok(AuthenticationResponse.builder()
                .token(jwtToken)
                .build());
    }
}