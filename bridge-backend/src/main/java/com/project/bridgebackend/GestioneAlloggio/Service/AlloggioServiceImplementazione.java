package com.project.bridgebackend.GestioneAlloggio.Service;

import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Consulenza;
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
import java.util.Collections;
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


    /** Metodo che permette ad un rifugiato di manifesare interesse per un'alloggio
     * @param emailRifugiato l'email del rifugiato
     * @param idAlloggio l'id dell'alloggio al quale si manifesta interesse
     *
     * @return un booleano che specifica lo stato dell'operazione. true andato a buon fine, false altrimenti
     */
    @Override
    public boolean interesse(String emailRifugiato, long idAlloggio){
        if (emailRifugiato == null || emailRifugiato.trim().isEmpty()) {
            throw new IllegalArgumentException("Email vuota o nulla");
        }
        if (idAlloggio <= 0) {
            throw new IllegalArgumentException("ID consulenza non valido");
        }

        // Recupera il rifugiato dal database
        Rifugiato r = rifugiatoDAO.findByEmail(emailRifugiato);
        if (r == null) {
            throw new IllegalArgumentException("Rifugiato non trovato");
        }

        Alloggio a = alloggioDAO.findAlloggioById(idAlloggio);
        if(a == null) {
            throw new IllegalArgumentException("Alloggio non trovato");
        }

        // Verifica se l'email del rifugiato è già nella lista candidati
        if (a.getListaCandidati().contains(r.getEmail())) {
            throw new IllegalArgumentException("Interesse già manifestato");
        }

        a.getListaCandidati().add(r);
        //aggiorno la consulenza
        alloggioDAO.save(a);
        sendEmailRifugiato("Interesse manifestat", "mariozurolo00@gmail.com");
        return true;
    }

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
     * @param messaggio il messaggio da inviare
     * @param emailRifugiato l'email del rifugiato destinatario
     */
    @Override
    public void sendEmailRifugiato(final String messaggio,
                                   final String emailRifugiato) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("beehaveofficial@gmail.com");
            message.setTo("mariozurolo00@gmail.com");
            message.setSubject("PROVA");
            message.setText(messaggio);
            mailSender.send(message);
            System.out.println("email inviata");
        } catch (Exception e) {
            System.out.println("Errore nell invio dell'email a: " + emailRifugiato + e.getMessage());
        }

    }


    /**
     * Metodo che gestisce l'assegnazione di un alloggio a un rifugiato.
     *
     * @param id l'id dell'alloggio.
     * @param emailRifugiato l'email del rifugiato.
     * @return l'alloggio assegnato o null se non viene,
     * trovato un alloggio valido.
     * @throws IllegalArgumentException se il rifugiato,
     * non ha mostrato interesse per l'alloggio.
     */
    @Override
    public Alloggio assegnazioneAlloggio(final long id, final String emailRifugiato) {
        if (id < 1) {
            throw new IllegalArgumentException("ID non valido");
        }

        if (emailRifugiato == null || emailRifugiato.trim().isEmpty()) {
            throw new IllegalArgumentException("Email vuota o nulla");
        }

        // Recupera l'alloggio dal database
        Alloggio alloggio = alloggioDAO.findAlloggioById(id);
        if (alloggio == null) {
            throw new IllegalArgumentException("Alloggio non trovato");
        }

        // Recupera la lista dei candidati per l'alloggio
        List<Rifugiato> listaCandidati = alloggio.getListaCandidati();
        if (listaCandidati == null || listaCandidati.isEmpty()) {
            throw new IllegalArgumentException("Nessun candidato disponibile per questo alloggio");
        }

        // Cerca il rifugiato nella lista dei candidati
        Optional<Rifugiato> rifugiatoOpt = listaCandidati.stream()
                .filter(r -> r.getEmail().equals(emailRifugiato))
                .findFirst();

        if (rifugiatoOpt.isEmpty()) {
            throw new IllegalArgumentException("Il rifugiato non ha manifestato interesse per questo alloggio");
        }

        Rifugiato rifugiato = rifugiatoOpt.get();

        // Verifica se l'alloggio è già stato assegnato
        if (alloggio.getAssegnatoA() != null) {
            throw new IllegalArgumentException("L'alloggio è già stato assegnato a un altro rifugiato");
        }

        // Assegna l'alloggio al rifugiato
        alloggio.setAssegnatoA(rifugiato);

        // Salva l'alloggio aggiornato
        alloggioDAO.save(alloggio);

        return alloggio;
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

    @Override
    public List<Alloggio> getRandomAlloggi() {
        // Recupera tutti gli eventi
        List<Alloggio> alloggi = alloggioDAO.findAll();

        // Mischia la lista
        Collections.shuffle(alloggi);

        //Restituisce solo 5 eventi
        return alloggi.stream().limit(5).toList();
    }

    @Override
    public List<Alloggio> getAllAlloggiByEmail(String email){

        if(email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email nullo");
        }
        try{
            List<Alloggio> allAlloggi= alloggioDAO.getAllAlloggiByEmail(email);
            return allAlloggi;
        }catch(Exception e){
            throw new IllegalArgumentException("Non trovati gli alloggi " + e.getMessage());
        }
    }

    @Override
    public List<Rifugiato> getInteressati(long id) {
        try {
            Optional<Alloggio> optionalAlloggio = alloggioDAO.findById(id);
            if (!optionalAlloggio.isPresent()) {
                throw new IllegalArgumentException("Alloggio non trovato");
            }

            Alloggio alloggio = optionalAlloggio.get();
            List<Rifugiato> rifugiatiInteressati = alloggio.getListaCandidati();
            if (rifugiatiInteressati.isEmpty()) {
                throw new IllegalArgumentException("Lista dei interessati vuota");
            }
            return rifugiatiInteressati;
        } catch (Exception e) {
            // Log dell'errore lato server
            System.err.println("Errore durante il recupero dei rifugiati interessati: " + e.getMessage());
            throw new IllegalArgumentException("Non trovati i rifugiati: " + e.getMessage());
        }
    }

}
