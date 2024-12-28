package com.project.bridgebackend.GestioneAlloggio.Service;

import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AlloggioService {
    /**
     *Firma del metodo che aggiunge un nuovo alloggio.
     * @param alloggio alloggio da inserire nel sistema.
     * @return l'alloggio appena inserito.
     */
    Alloggio addAlloggio(Alloggio alloggio);

    /**
     *Firma del metodo che invia una email al Rifugiato.
     * @param message messaggio che sarà mostrato nella email.
     * @param emailRifugiato id del rifugiato a cui inviare l'email.
     */
    void sendEmailRifugiato(String message, String emailRifugiato);


    /** Firma del metodo che permette ad un rifugiato di manifesare interesse per un'alloggio
     * @param emailRifugiato l'email del rifugiato
     * @param idAlloggio l'id dell'alloggio al quale si manifesta interesse
     *
     * @return ResponseEntity con lo stato dell'operazione
     */
    public boolean interesse(String emailRifugiato, long idAlloggio);

    /**
     *Firma del metodo che assegna un alloggio a un rifugiato.
     * @param id l'id dell'alloggio
     * @param emailRifugiato l'identificativo del rifugiato a cui è stato assegnato un alloggio.
     * @return l'alloggio che è stato assegnato al rifugiato.
     */
    Alloggio assegnazioneAlloggio(long id, String emailRifugiato);

    /**
     *Firma del metodo che notifica il volontario nel caso,
     * in cui ci sia una nuova manifestazione di interesse per il suo annuncio.
     * @param message messaggio che viene inviato al volontario.
     * @param emailVolontario id del volontario da notificare.
     */
    void sendEmailVolontario(String message, String emailVolontario);

    /**
     *Firma del metodo che mostra tutti gli alloggi presenti nel sistema.
     * @return lista di tutti gli alloggi presenti nel db.
     */
    List<Alloggio> getAllAlloggio();

    /**
     * Firma del metodo per salvare l'indirizzo dell'alloggio.
     * @param indirizzo indirizzo dell'alloggio con tutte le caratteristiche.
     * @return l'indirizzo salvato
     */
    long salvaIndirizzoAlloggio(Indirizzo indirizzo);

    /**
     * Firma del metodo per prendere un alloggio,
     * sulla base del titolo dato in input.
     * @param titolo dell'alloggio che si vuole prelevare.
     * @return alloggio prelevato sulla base del titolo.
     */
    Alloggio getAlloggioByTitolo(@RequestBody String titolo);

    /**
     * Firma del metodo per prendere l'id dell'indirizzo dell'alloggio.
     * @param indirizzo indirizzo dell'alloggio con tutte le caratteristiche.
     * @return l'id dell'indirizzo richiesto.
     */
    long getIdIndirizzo(Indirizzo indirizzo);


    /**
     * Firma del metodo per ottenere 5 alloggi casuali.
     * @return lista di 5 alloggi casuali.
     */
    List<Alloggio> getRandomAlloggi();

    /**
     * Firma del metodo per ottenere tutti gli alloggi di un proprietario.
     * @param email l'email del Volontario che ha caricato l'annuncio.
     * @return La lista di tutti gli alloggi caricati
     */
    List<Alloggio> getAllAlloggiByEmail(String email);

    /**
     * Metodo per ottenere tutti i rifugiati che hanno manifestato interesse per un alloggio.
     * @param id id dell'alloggio.
     * @return Lista di rifugiati che hanno manifestato interesse
     * */
    List<Rifugiato> getInteressati(long id);

}
