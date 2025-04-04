package com.project.bridgebackend.registrazione.service;

import com.project.bridgebackend.Model.Entity.Admin;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.dao.AdminDAO;
import com.project.bridgebackend.Model.dao.FiguraSpecializzataDAO;
import com.project.bridgebackend.Model.dao.RifugiatoDAO;
import com.project.bridgebackend.Model.dao.VolontarioDAO;
import com.project.bridgebackend.util.AuthenticationRequest;
import com.project.bridgebackend.util.AuthenticationResponse;
import com.project.bridgebackend.util.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Antonio Ceruso.
 * Data creazione: 04/12/2024.
 * Classe che rappresenta il service della registrazione.
 */
@Service
public class RegistrazioneServiceImpl implements RegistrazioneService {
    /**
     * Si occupa delle operazioni relative all'admin nel db.
     * */
    @Autowired
    private AdminDAO adminDAO;
    /**
     * Si occupa delle operazioni relative al rifugiato nel db.
     * */
    @Autowired
    private RifugiatoDAO rifugiatoDAO;
    /**
     * Si occupa delle operazioni relative al volontario nel db.
     * */
    @Autowired
    private VolontarioDAO volontarioDAO;
    /**
     * Si occupa delle operazioni relative alla figura specializzata nel db.
     * */
    @Autowired
    private FiguraSpecializzataDAO figSpecDAO;

    /**
     * Gestore delle autenticazioni che verifica le credenziali degli utenti
     * (email e password) e valida il processo di autenticazione.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Servizio per la generazione e la gestione dei token JWT, utilizzati
     * per l'autenticazione e l'autorizzazione degli utenti.
     */
    @Autowired
    private JwtService jwtService;

    /**
     * Metodo per la cifratura della password.
     * @param password scritta dall'utente
     * @return la password cifrata come stringa.
     * */
    public String safePassword(final String password) {
        BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
        String pwCifrata = pwEncoder.encode(password);
        return pwCifrata;
    }

    /**
     *Implementazione metodo di registrazione di un volontario.
     * @param volontario
     * @param confermaPW
     * */
    @Override
    public void registraVolontario(@Valid final Volontario volontario,
                                   final String confermaPW)
            throws Exception {
        if (volontario == null) {
            throw new IllegalArgumentException("Volontario non valido");
        } else if (volontarioDAO.findByEmail(volontario.getEmail()) != null) {
            throw new IllegalArgumentException("Volontario già presente");
        }
        if (!volontario.getPassword().equals(confermaPW)) {
            throw new
                    IllegalArgumentException("Le due password non combaciano");
        }
        volontario.setPassword(safePassword(volontario.getPassword()));
        volontarioDAO.save(volontario);
    }

    /**
     *Implementazione metodo di registrazione di un rifugiato.
     * @param rifugiato
     * @param confermaPW
     * */
    @Override
    public void registraRifugiato(@Valid final Rifugiato rifugiato,
                                  final String confermaPW)
            throws Exception {
        if (rifugiato == null) {
            throw new IllegalArgumentException("Rifugiato non valido");
        } else if (rifugiatoDAO.findByEmail(rifugiato.getEmail()) != null) {
            throw new IllegalArgumentException("Rifugiato già presente");
        }

        if (!rifugiato.getPassword().equals(confermaPW)) {
            throw new
                    IllegalArgumentException("Le due password non combaciano");
        }
        rifugiato.setPassword(safePassword(rifugiato.getPassword()));
        rifugiatoDAO.save(rifugiato);
    }

    /**
     *Implementazione metodo di registrazione di un admin.
     * @param admin
     * @param confermaPW
     * */
    @Override
    public void registraAdmin(@Valid final Admin admin,
                              final String confermaPW)
            throws Exception {
        if (admin == null) {
            throw new IllegalArgumentException("Admin non valido");
        } else if (adminDAO.findByEmail(admin.getEmail()) != null) {
            throw new IllegalArgumentException("Admin già presente");
        }

        if (!admin.getPassword().equals(confermaPW)) {
            throw new
                    IllegalArgumentException("Le due password non combaciano");
        }
        admin.setPassword(safePassword(admin.getPassword()));
        adminDAO.save(admin);
    }

    /**
     *Implementazione metodo di registrazione di una Figura Specializzata.
     * @param figspec
     * @param confermaPW
     * */
    @Override
    public void registraFiguraSpecializzata(@Valid
                                            final FiguraSpecializzata figspec,
                                            final String confermaPW)
            throws Exception {
        if (figspec == null) {
            throw new
                    IllegalArgumentException("Figura Specializzata non valida");
        } else if (figSpecDAO.findByEmail(figspec.getEmail()) != null) {
            throw new
                    IllegalArgumentException("Figura Specializzata già presente");
        }

        if (!figspec.getPassword().equals(confermaPW)) {
            throw new
                    IllegalArgumentException("Le due password non combaciano");
        }
        figspec.setPassword(safePassword(figspec.getPassword()));
        figSpecDAO.save(figspec);
    }

    /**
     * Implementazione per il metodo del login tramite token jwt.
     * @param request parametro richiesta per il login.
     * @return response.
     */
    @Override
    public AuthenticationResponse login(final AuthenticationRequest request)
            throws Exception {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                ));
        String jwtToken;
        //setto anche il ruolo
        String ruoloUtente = "";

        if (adminDAO.findByEmail(request.getEmail()) != null) {
            jwtToken = jwtService.generateToken(adminDAO.findByEmail(request.getEmail()));
            ruoloUtente = "Admin";
        } else if (volontarioDAO.findByEmail(request.getEmail()) != null) {
            jwtToken = jwtService.generateToken(volontarioDAO.findByEmail(request.getEmail()));
            ruoloUtente = "Volontario";
        } else if (rifugiatoDAO.findByEmail(request.getEmail()) != null) {
            jwtToken = jwtService.generateToken(rifugiatoDAO.findByEmail(request.getEmail()));
            ruoloUtente = "Rifugiato";
        } else if (figSpecDAO.findByEmail(request.getEmail()) != null) {
            jwtToken = jwtService.generateToken(figSpecDAO.findByEmail(request.getEmail()));
            ruoloUtente = "FiguraSpecializzata";
        } else {
            throw new IllegalArgumentException("Utente non trovato");
        }

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .ruolo(ruoloUtente)
                .build();
    }

}
