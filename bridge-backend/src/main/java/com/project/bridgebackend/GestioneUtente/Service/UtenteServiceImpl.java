package com.project.bridgebackend.GestioneUtente.Service;

import com.project.bridgebackend.CDN.CDNService;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Utente;
import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import com.project.bridgebackend.Model.dao.FiguraSpecializzataDAO;
import com.project.bridgebackend.Model.dao.UtenteDAO;
import com.project.bridgebackend.CDN.Document.FotoProfilo;
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

    /**
     * Service per la gestione delle foto.
     */
    @Autowired
    private CDNService fotoProfiloService;

    /**
     * Dao per la gestione degli utenti.
     */
    @Autowired
    private UtenteDAO utenteDAO;

    /**
     * Figura specializzata per la gestione degli utenti.
     */
    @Autowired
    private FiguraSpecializzataDAO fsDAO;


    /**
     * Bean per la gestione della cifratura delle password.
     * Utilizza un'implementazione di,
     * {@link org.springframework.security.crypto.password.PasswordEncoder}
     * per garantire la sicurezza durante la memorizzazione delle password.
     */
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

    /**
     * Metodo per eliminare un utente dato il suo indirizzo email.
     * Viene eliminata anche la foto profilo associata all'utente.
     *
     * @param email indirizzo email dell'utente da eliminare.
     * @throws Exception se si verifica un errore durante l'eliminazione.
     */
    @Override
    public void eliminaUtente(final String email) throws Exception {
        try {
            String idFoto = utenteDAO.findByEmail(email).getFotoProfilo();
            utenteDAO.delete(utenteDAO.findByEmail(email));
            fotoProfiloService.deleteFotoProfilo(idFoto);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Recupera la foto profilo di un utente dato il suo indirizzo email.
     *
     * @param email indirizzo email dell'utente.
     * @return un oggetto {@link FotoProfilo} contenente la foto dell'utente.
     * @throws IOException se si verifica un errore durante il recupero della foto.
     */
    public FotoProfilo getFotoUtente(
            final String email) throws IOException {
        Utente u = utenteDAO.findByEmail(email);
        FotoProfilo fp = fotoProfiloService.getFotoProfilo(u.getFotoProfilo());
        return fp;
    }

    /**
     * Modifica la password di un utente dato il suo indirizzo email.
     * La nuova password viene cifrata prima di essere salvata.
     *
     * @param email indirizzo email dell'utente.
     * @param password nuova password da impostare.
     * @throws Exception se si verifica un errore durante l'aggiornamento della password.
     */
    public void modificaPassword(
            final String email,
            final String password) throws Exception {
        Utente u = utenteDAO.findByEmail(email);
        System.out.println(password);
        u.setPassword(safePassword(password));
        utenteDAO.save(u);
    }

    /**
     * Modifica la disponibilità di un utente specializzato dato il suo indirizzo email.
     *
     * @param email indirizzo email dell'utente.
     * @param disp nuova disponibilità da impostare.
     * @throws Exception se si verifica un errore durante l'aggiornamento della disponibilità.
     */
    public void modificaDisp(final String email, final String disp) throws Exception {
        FiguraSpecializzata fs = fsDAO.findByEmail(email);
        fs.setDisponibilita(disp);
          fsDAO.save(fs);
    }

    /**
     * Modifica i dati di un utente dato il suo indirizzo email e una mappa,
     * di aggiornamenti.
     *
     * @param email indirizzo email dell'utente.
     * @param aggiornamenti mappa contenente le chiavi e i valori dei dati da aggiornare.
     * @return l'oggetto {@link Utente} aggiornato.
     * @throws IOException se si verifica un errore durante l'aggiornamento.
     */
    public Utente modificaUtente(
            final String email,
            final HashMap<String, Object> aggiornamenti) throws IOException {
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

    /**
     * Modifica la foto profilo di un utente dato il suo indirizzo email e una nuova immagine in formato Base64.
     *
     * @param email indirizzo email dell'utente.
     * @param base64Image immagine in formato Base64 da salvare come nuova foto profilo.
     * @throws IOException se si verifica un errore durante l'aggiornamento della foto.
     */
    public void modificaFotoUtente(
            final String email,
            final String base64Image) throws IOException {
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
                String fotoProfiloId = fotoProfiloService.saveFotoProfilo(utente.getEmail(), fotoData);

                // Aggiorna l'utente con il nuovo ID foto
                utente.setFotoProfilo(fotoProfiloId);
                utenteDAO.save(utente);  // Salva l'utente con il nuovo fotoProfiloId nel database


        } else {
            throw new IllegalArgumentException("La stringa Base64 per la foto è vuota.");
        }
    }

    /**
     * Recupera un utente dal sistema dato il suo indirizzo email.
     *
     * @param email indirizzo email dell'utente.
     * @return l'oggetto {@link Utente} associato all'indirizzo email fornito.
     */
    public Utente getUtente(
            final String email) {
        Utente u = utenteDAO.findByEmail(email);
        return u;
    }
}
