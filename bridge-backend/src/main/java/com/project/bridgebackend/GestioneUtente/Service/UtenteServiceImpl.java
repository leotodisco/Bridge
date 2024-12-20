package com.project.bridgebackend.GestioneUtente.Service;

import com.project.bridgebackend.Model.Entity.Admin;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Lavoro;
import com.project.bridgebackend.Model.Entity.Utente;
import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import com.project.bridgebackend.Model.dao.FiguraSpecializzataDAO;
import com.project.bridgebackend.Model.dao.UtenteDAO;
import com.project.bridgebackend.fotoProfilo.FotoProfilo;
import com.project.bridgebackend.fotoProfilo.FotoProfiloService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author Antonio Ceruso
 * Data creazione: 12/12/2024
 * Classe che implemeta il service per l'utente
 * */
@Service

@Transactional
public class UtenteServiceImpl implements UtenteService {

    @Autowired
    private FotoProfiloService fotoProfiloService;

    @Autowired
    private UtenteDAO utenteDAO;

    @Autowired
    private FiguraSpecializzataDAO fsDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Metodo per la cifratura della password.
     * @param password
     * @return la password cifrata come stringa.
     * */
    public String safePassword(final String password) {
        BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
        String pwCifrata = pwEncoder.encode(password);
        return pwCifrata;
    }

    @Override
    public void eliminaUtente(String email) throws Exception {
        try {
            String idFoto = utenteDAO.findByEmail(email).getFotoProfilo();
            utenteDAO.delete(utenteDAO.findByEmail(email));
            fotoProfiloService.deleteIMG(idFoto);
        }catch(Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public FotoProfilo getFotoUtente(String email) throws IOException {
        Utente u = utenteDAO.findByEmail(email);
        FotoProfilo fp = fotoProfiloService.getIMG(u.getFotoProfilo());
        return fp;
    }

    public void modificaPassword(String email, String password) throws Exception {
        Utente u = utenteDAO.findByEmail(email);
        System.out.println(password);
        u.setPassword(safePassword(password));
        utenteDAO.save(u);
    }

    public void modificaDisp(final String email, final String disp) throws Exception {
        FiguraSpecializzata fs = fsDAO.findByEmail(email);
        fs.setDisponibilita(disp);
          fsDAO.save(fs);
    }

    public Utente modificaUtente(final String email, final HashMap<String, Object> aggiornamenti) throws IOException {
        Optional<Utente> utenteOptional = utenteDAO.findById(email);
        if (utenteOptional.isEmpty()) {
            throw new IllegalArgumentException("Utente non trovato.");
        }

        Utente utente = utenteOptional.get();
        try {
            aggiornamenti.forEach((campo, valore) -> {
                switch (campo) {
                    case "nomeUtente":
                        utente.setNome((String) valore);
                        break;
                    case "cognomeUtente":
                        utente.setCognome((String) valore);
                        break;
                    case "nazionalitaUtente":
                        utente.setNazionalita((String) valore);
                        break;
                    case "dataNascitaUtente":
                        utente.setDataNascita(LocalDate.parse((String) valore));
                        break;
                    case "genderUtente":
                        utente.setGender(Gender.valueOf((String) valore));
                        break;
                    case "skillUtente":
                        utente.setSkill((String) valore);
                        break;
                    case "titoloDiStudioUtente":
                        utente.setTitoloDiStudio(TitoloDiStudio.valueOf((String) valore));
                        break;
                    case "lingueParlateUtente":
                        utente.setLingueParlate((String) valore);
                        break;
                    default:
                        throw new IllegalArgumentException("Campo non valido: " + campo);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return utenteDAO.save(utente);
    }

    public void modificaFotoUtente(String email, String base64Image) throws IOException {
        // Trova l'utente dal database tramite email
        Optional<Utente> utenteOptional = utenteDAO.findById(email);
        if (utenteOptional.isEmpty()) {
            throw new IllegalArgumentException("Utente non trovato.");
        }

        Utente utente = utenteOptional.get();

        // Verifica se la foto è presente e non è vuota
        if (base64Image != null && !base64Image.isEmpty()) {
            // Verifica se contiene il prefisso 'data:image/jpeg;base64,' e rimuovilo

                // Decodifica la stringa base64 in byte[]
                byte[] fotoData = Base64.getDecoder().decode(base64Image);

                // Salva l'immagine tramite il servizio FotoProfiloService (deve restituire un ID unico per la foto)
                String fotoProfiloId = fotoProfiloService.saveIMG(utente.getEmail(), fotoData);

                // Aggiorna l'utente con il nuovo ID foto
                utente.setFotoProfilo(fotoProfiloId);
                utenteDAO.save(utente);  // Salva l'utente con il nuovo fotoProfiloId nel database


        } else {
            throw new IllegalArgumentException("La stringa Base64 per la foto è vuota.");
        }
    }


    public Utente getUtente(String email) {
        Utente u = utenteDAO.findByEmail(email);
        return u;
    }
}
