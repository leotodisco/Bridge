package com.project.bridgebackend.GestioneCorso.Service;

import com.project.bridgebackend.CDN.CDNService;
import com.project.bridgebackend.Model.Entity.Corso;
import com.project.bridgebackend.Model.dao.CorsoDAO;
import com.project.bridgebackend.Model.dao.FiguraSpecializzataDAO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * @author Biagio Gallo.
 * Creato il: 06/12/2024.
 * Implementazione del service per la gestione dei corsi.
 * Questa classe fornisce metodi per,
 * creare, modificare, eliminare e trovare corsi.
 * Utilizza il CorsoDAO per interagire con il database.
 * L'annotazione @RequiredArgsConstructor di Lombok,
 * viene utilizzata per generare un costruttore con gli argomenti richiesti.
 */
@Service
@RequiredArgsConstructor
public class GestioneCorsoServiceImpl implements GestioneCorsoService {

    /**
     * Iniezione del DAO per interagire con il database.
     */
    @Autowired
    private final CorsoDAO corsoDAO;

    /**
     * Iniezione logica di gestione per i pdf.
     */
    @Autowired
    private final CDNService pdfService;
    @Autowired
    private FiguraSpecializzataDAO figuraSpecializzataDAO;

    /**
     * Crea un nuovo corso.
     * @param corso il DTO del corso contenente i dettagli del corso.
     * @return il corso creato come CorsoDTO.
     */

    @Override
    public Corso creaCorso(final Corso corso) throws Exception {
        if (corso == null) {
            throw new IllegalArgumentException("Corso non valido");
        }

        // Controllo del titolo
        if (corso.getTitolo() == null || corso.getTitolo().trim().isEmpty()) {
            throw new IllegalArgumentException("Il titolo del corso non può essere vuoto");
        }
        if (corso.getTitolo().length() < 3) {
            throw new IllegalArgumentException("Il titolo del corso deve contenere almeno 3 caratteri");
        }

        // Controllo della descrizione
        if (corso.getDescrizione() == null || corso.getDescrizione().trim().isEmpty()) {
            throw new IllegalArgumentException("La descrizione del corso non può essere vuota");
        }
        if (corso.getDescrizione().length() < 10) {
            throw new IllegalArgumentException("La descrizione del corso deve contenere almeno 10 caratteri");
        }

        // Controllo della categoria del corso
        if (corso.getCategoriaCorso() == null) {
            throw new IllegalArgumentException("La categoria del corso è obbligatoria");
        }

        // Controllo della lingua
        if (corso.getLingua() == null) {
            throw new IllegalArgumentException("La lingua del corso è obbligatoria");
        }

        // Controllo del proprietario del corso
        if (corso.getProprietario() == null) {
            throw new IllegalArgumentException("Il proprietario del corso è obbligatorio");
        }

        if (figuraSpecializzataDAO.findByEmail(corso.getProprietario().getEmail()) == null) {
            throw new IllegalArgumentException("Il proprietario del corso non è valido");
        }

        if (!corso.getTitolo().matches("^[A-Za-z0-9À-ÿ .,'-]{3,100}$")) {
            throw new Exception("Il titolo del corso non è valido");
        }

        if (!corso.getCategoriaCorso().toString().matches("^[A-Za-zÀ-ÿ' -]{3,50}$")) {
            throw new Exception("La categoria del corso non è valida");
        }

        if (!corso.getDescrizione().matches("^[A-Za-z0-9À-ÿ .,'-]{10,500}$")) {
            throw new Exception("La descrizione del corso non è valida");
        }

        if (!corso.getLingua().toString().matches("(?i)^(Italiano|Inglese|Francese|Tedesco|Spagnolo|Portoghese|Russo|Cinese|Ucraino, Arabo)(,\\s*(Italiano|Inglese|Francese|Tedesco|Spagnolo|Portoghese|Russo|Cinese|Ucraino, Arabo))*$")) {
            throw new Exception("La lingua del corso non è valida");
        }

        return corsoDAO.save(corso);
    }
    /**
     * Modifica un corso esistente.
     * todo: inserire possibilità modifica pdf
     * @param corso il DTO del corso contenente.
     * i dettagli aggiornati del corso.
     * @return il corso modificato come CorsoDTO.
     */
    @Override
    public Corso modificaCorso(final Corso corso) {
        if (corso == null || corso.getTitolo() == null || corso.getProprietario() == null) {
            throw new IllegalArgumentException("Il corso o i dettagli richiesti non sono validi");
        }

        Corso existingCorso = corsoDAO.findById(corso.getId())
                .orElseThrow(() -> new IllegalArgumentException("Corso non trovato"));
        if (existingCorso == null) {
            throw new IllegalArgumentException("Corso non trovato");
        }

        // Aggiorna i campi del corso esistente
        existingCorso.setId(corso.getId());
        existingCorso.setTitolo(corso.getTitolo());
        existingCorso.setDescrizione(corso.getDescrizione());
        existingCorso.setCategoriaCorso(corso.getCategoriaCorso());
        existingCorso.setLingua(corso.getLingua());
        existingCorso.setPdf(corso.getPdf());
        return corsoDAO.save(existingCorso);
    }

    /**
     * Elimina un corso esistente.
     * @param corso il DTO del corso contenente.
     * i dettagli del corso da eliminare.
     */
    @Override
    @Transactional
    public void eliminaCorso(final Corso corso) {
        if (corso.getId() == null) {
            throw new IllegalArgumentException("ID del corso non valido");
        }

        Corso existingCorso = corsoDAO.findById(corso.getId())
                .orElseThrow(() -> new IllegalArgumentException("Corso non trovato"));

        // Elimina il PDF associato
        if (existingCorso.getPdf() != null) {
            pdfService.deletePdf(existingCorso.getPdf());
        }

        corsoDAO.delete(existingCorso);
    }

    /**
     * Trova tutti i corsi.
     * @return una lista di tutti i corsi come CorsoDTO.
     */
    @Override
    public List<Corso> findAll() {
        return corsoDAO.findAll();
    }

}
