package com.project.bridgebackend.GestioneAlloggio.Service;

import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 *Created By Mario Zurolo
 * Date 4/12/24
 **/

@Service
@Validated
public class AlloggioServiceImplementazione implements AlloggioService {

    @Autowired
    private VolontarioDAO volontarioDAO;
    @Autowired
    RifugiatoDAO rifugiatoDAO;
    @Autowired
    private AlloggioDAO alloggioDAO;
    @Autowired
    private IndirizzoDAO indirizzoDAO;



    /**
     * Metodo per l'aggiunta di un nuovo alloggio nel sistema
     * Precondizione: l'alloggio non deve essere nullo
     *
     * @Param alloggio l'alloggio che si desidera caricare
     * @Param idAlloggio l'identificativo dell'alloggio
     * @Return true o false
     * PostCondizione l'alloggio sarà caricato nel sistema
     */
    @Override
    public Alloggio addAlloggio(Alloggio alloggio) {
        if (alloggio == null) {
            throw new IllegalArgumentException("L'alloggio non può essere nullo");
        }
        return alloggioDAO.save(alloggio);
    }

    @Override
    public void sendEmailRifugiato(String message, String emailRifugiato) {

    }


    /**
     * Metodo per la manifestazione di interesse da parte di un rifugiato ad un alloggio
     * Precondizione: l'alloggio non deve essere nullo
     * Precondizione: il rifugiato non deve essere nullo
     *
     * @Param rifugiato, ovvero, colui che mostra interesse per un alloggio
     * @Param alloggio, ovvero, l'alloggio al quale un rifugiato mostra interesse
     * @Return void
     * <p>
     * PostCondizione: Il rifugiato mostra interesse per un alloggio
     */

    @Override
    public boolean manifestazioneInteresse(String emailRifugiato, String titoloAlloggio) {
        // Validazione input
        if (emailRifugiato == null || emailRifugiato.isEmpty()) {
            throw new IllegalArgumentException("L'email del rifugiato non può essere nulla o vuota.");
        }
        if (titoloAlloggio == null || titoloAlloggio.isEmpty()) {
            throw new IllegalArgumentException("Il titolo dell'alloggio non può essere nullo o vuoto.");
        }

        // Recupera il rifugiato tramite l'email
        Rifugiato rifugiato = rifugiatoDAO.findByEmail(emailRifugiato);
        if (rifugiato == null) {
            throw new IllegalArgumentException("Rifugiato non trovato con l'email specificata.");
        }

        // Recupera l'alloggio tramite il titolo
        Optional<Alloggio> alloggioOptional = alloggioDAO.findByTitolo(titoloAlloggio);
        if (alloggioOptional.isEmpty()) {
            throw new IllegalArgumentException("Alloggio non trovato con il titolo specificato.");
        }

        Alloggio alloggio = alloggioOptional.get();

        // Verifica se il rifugiato ha già manifestato interesse per questo alloggio
        if (alloggio.getListaCandidati().contains(rifugiato)) {
            return false; // Interesse già manifestato
        }

        // Aggiunge il rifugiato alla lista dei candidati
        alloggio.getListaCandidati().add(rifugiato);
        alloggioDAO.save(alloggio);

        return true; // Interesse aggiunto con successo
    }




    @Override
    public Alloggio assegnazioneAlloggio(String titolo, String emailRifugiato) {

        /*Optional<Alloggio> alloggioOptional = alloggioDAO.findByTitolo(titolo);

        if (alloggioOptional == null || !alloggioOptional.isPresent()) {
            throw new IllegalArgumentException("Alloggio non trovato");
        }

        Rifugiato rifugiato = rifugiatoDAO.findByEmail(emailRifugiato);

        if (rifugiato == null) {
            throw new IllegalArgumentException("Il rifugiato non trovato");
        }

        Alloggio alloggio = alloggioOptional.get();

        //Se non ha mostrato interesse per l'alloggio
        if (!manifestazioneInteresse(rifugiato, alloggio)) {
            throw new IllegalArgumentException("Il rifugiato non ha mostrato interesse per questo alloggio");
        } else { // se ha mostrato interesse per l'alloggio
            alloggio.getListaCandidati().clear();
            alloggio.getListaCandidati().add(rifugiato);

            alloggioDAO.save(alloggio);
            return alloggio;
            //throw new IllegalArgumentException("Già mostrato interesse");
        }*/return null;
    }

    @Override
    public void sendEmailVolontario(String message, String emailVolontario) {
    }


    @Override
    public List<Alloggio> getAllAlloggio() {
        return alloggioDAO.findAllAlloggiWithProprietario();
    }

    @Override
    public long salvaIndirizzoAlloggio(Indirizzo indirizzo) {
        indirizzoDAO.save(indirizzo);
        return indirizzo.getId();
    }

    @Override
    public long getIdIndirizzo(Indirizzo indirizzo) {
        if (indirizzo == null) {
            throw new IllegalArgumentException("Indirizzo nullo");
        }

        return indirizzo.getId();
    }

    @Override
    public Alloggio getAlloggioByTitolo(@RequestBody String titolo) {

      Alloggio alloggio = alloggioDAO.findAlloggioByTitolo(titolo).get();
        System.out.println(alloggio);
      if (alloggio == null) {
          throw new IllegalArgumentException("Alloggio non trovato");
      }
        System.out.println(alloggio);
      return alloggio;
    }
}
