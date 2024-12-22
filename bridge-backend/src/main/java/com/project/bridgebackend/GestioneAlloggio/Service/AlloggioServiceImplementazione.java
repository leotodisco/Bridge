package com.project.bridgebackend.GestioneAlloggio.Service;

import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.dao.RifugiatoDAO;
import com.project.bridgebackend.Model.dao.VolontarioDAO;
import com.project.bridgebackend.Model.dao.AlloggioDAO;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Optional;

/**
 *@author Mario Zurolo.
 * Date 4/12/24.
 * Implementazione del servizio per la gestione degli alloggi.
 * Contiene metodi per l'aggiunta, la manifestazione di interesse,
 * e l'assegnazione degli alloggi,
 * oltre alla gestione delle email per comunicare con i rifugiati,
 * e i volontari.
 **/

@Service
@Validated
public class AlloggioServiceImplementazione implements AlloggioService {

    /**
     * Service per la logica di gestione degli utenti volontari.
     */
    @Autowired
    private VolontarioDAO volontarioDAO;

    /**
     * Service per la logica di gestione degli utenti rifugiati.
     */
    @Autowired
    RifugiatoDAO rifugiatoDAO;

    /**
     * Service per la logica di gestione degli alloggi.
     */
    @Autowired
    private AlloggioDAO alloggioDAO;

    /**
     * Service per la logica di gestione degli indirizzi.
     */
    @Autowired
    private IndirizzoDAO indirizzoDAO;

    /**
     * Bean per l'invio di email tramite JavaMailSender.
     * Utilizzato per inviare email al rifugiato e,
     * al volontario nel sistema.
     */

    @Autowired
    private JavaMailSender mailSender;



    /**
     * Aggiunge un nuovo alloggio nel sistema.
     *
     * @param alloggio l'alloggio che si desidera caricare
     * @return l'alloggio appena creato
     * @throws IllegalArgumentException se l'alloggio è nullo
     */
    @Override
    public Alloggio addAlloggio(final Alloggio alloggio) {
        if (alloggio == null) {
            throw new IllegalArgumentException("L'alloggio non può essere nullo");
        }
        return alloggioDAO.save(alloggio);
    }

    /**
     * Metodo che invia una email al rifugiato.
     *
     * @param message il messaggio da inviare
     * @param emailRifugiato l'email del rifugiato destinatario
     */
    @Override
    public void sendEmailRifugiato(final String message,
                                   final String emailRifugiato) {

    }


    /**
     * Metodo che gestisce la manifestazione di interesse,
     * di un rifugiato per un alloggio.
     *
     * @param emailRifugiato l'email del rifugiato.
     * @param titoloAlloggio il titolo dell'alloggio.
     * @return true se l'interesse è stato aggiunto con successo,
     * false se l'interesse è già stato manifestato
     * @throws IllegalArgumentException se il rifugiato,
     * o l'alloggio non esistono o se i parametri sono vuoti.
     */
    @Override
    public boolean manifestazioneInteresse(
            final String emailRifugiato,
            final String titoloAlloggio) {
        if (emailRifugiato == null || emailRifugiato.isEmpty()) {
            throw new IllegalArgumentException("L'email del rifugiato non può essere nulla o vuota.");
        }
        if (titoloAlloggio == null || titoloAlloggio.isEmpty()) {
            throw new IllegalArgumentException("Il titolo dell'alloggio non può essere nullo o vuoto.");
        }

        Rifugiato rifugiato = rifugiatoDAO.findByEmail(emailRifugiato);
        if (rifugiato == null) {
            throw new IllegalArgumentException("Rifugiato non trovato con l'email specificata.");
        }

        Optional<Alloggio> alloggioOptional = alloggioDAO.findByTitolo(titoloAlloggio);
        if (alloggioOptional.isEmpty()) {
            throw new IllegalArgumentException("Alloggio non trovato con il titolo specificato.");
        }

        Alloggio alloggio = alloggioOptional.get();

        if (alloggio.getListaCandidati().contains(rifugiato)) {
            return false; // Interesse già manifestato
        }

        alloggio.getListaCandidati().add(rifugiato);
        alloggioDAO.save(alloggio);
        sendEmailVolontario("riuscito coglione", "provais226@gmail.com");

        return true; // Interesse aggiunto con successo
    }


    /**
     * Metodo che gestisce l'assegnazione di un alloggio a un rifugiato.
     *
     * @param titolo il titolo dell'alloggio.
     * @param emailRifugiato l'email del rifugiato.
     * @return l'alloggio assegnato o null se non viene,
     * trovato un alloggio valido.
     * @throws IllegalArgumentException se il rifugiato,
     * non ha mostrato interesse per l'alloggio.
     */
    @Override
    public Alloggio assegnazioneAlloggio(
            final String titolo,
            final String emailRifugiato) {

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
        }*/ return null;
    }

    /**
     * Metodo asincrono che invia una email al volontario.
     *
     * @param messaggio il messaggio da inviare.
     * @param emailDestinatario l'email del destinatario.
     */
    @Override
    @Async
    public void sendEmailVolontario(
            final String messaggio,
            final String emailDestinatario) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("beehaveofficial@gmail.com");
            message.setTo("mariozurolo00@gmail.com");
            message.setSubject("PROVA");
            message.setText(messaggio);
            mailSender.send(message);
            System.out.println("email inviata");
        } catch (Exception e) {
            System.out.println("Errore nell invio dell'email a: " + emailDestinatario + e.getMessage());
        }
    }

    /**
     * Restituisce la lista di tutti gli alloggi.
     *
     * @return la lista di tutti gli alloggi.
     */
    @Override
    public List<Alloggio> getAllAlloggio() {
        return alloggioDAO.findAllAlloggiWithProprietario();
    }

    /**
     * Salva un indirizzo nel sistema.
     *
     * @param indirizzo l'indirizzo da salvare.
     * @return l'ID dell'indirizzo salvato.
     * @throws IllegalArgumentException se l'indirizzo è nullo.
     */

    @Override
    public long salvaIndirizzoAlloggio(final Indirizzo indirizzo) {
        indirizzoDAO.save(indirizzo);
        return indirizzo.getId();
    }


    /**
     * Restituisce l'ID di un indirizzo.
     *
     * @param indirizzo l'indirizzo di cui si vuole ottenere l'ID.
     * @return l'ID dell'indirizzo.
     * @throws IllegalArgumentException se l'indirizzo è nullo.
     */
    @Override
    public long getIdIndirizzo(final Indirizzo indirizzo) {
        if (indirizzo == null) {
            throw new IllegalArgumentException("Indirizzo nullo");
        }

        return indirizzo.getId();
    }

    /**
     * Restituisce un alloggio dato il suo titolo.
     *
     * @param titolo il titolo dell'alloggio.
     * @return l'alloggio corrispondente al titolo.
     * @throws IllegalArgumentException se l'alloggio non viene trovato.
     */

    @Override
    public Alloggio getAlloggioByTitolo(
            @RequestBody final String titolo) {

      Alloggio alloggio = alloggioDAO.findAlloggioByTitolo(titolo).get();
        System.out.println(alloggio);
      if (alloggio == null) {
          throw new IllegalArgumentException("Alloggio non trovato");
      }
        System.out.println(alloggio);
      return alloggio;
    }
}
