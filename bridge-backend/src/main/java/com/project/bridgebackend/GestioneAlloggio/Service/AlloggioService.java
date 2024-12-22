package com.project.bridgebackend.GestioneAlloggio.Service;

import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Indirizzo;
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

    /**
     *Firma del metodo che manifesta interesse per un alloggio.
     * @param emailRifugiato il rifugiato che ha manifestato interesse.
     * @param emailAlloggio a quale alloggio ha manifestato interesse,
     *                      il rifugiato.
     * @return true se l'alloggio è andato correttamente nei preferiti,
     * del rifugiato, false in caso di errore.
     */
    boolean manifestazioneInteresse(String emailRifugiato, String emailAlloggio);

    public boolean interesse(String emailRifugiato, long idAlloggio);

    /**
     *Firma del metodo che assegna un alloggio a un rifugiato.
     * @param titolo l'alloggio che è stato assegnato al rifugiato.
     * @param emailRifugiato l'identificativo del rifugiato,
     *                       a cui è stato assegnato un alloggio.
     * @return l'alloggio che è stato assegnato al rifugiato.
     */
    Alloggio assegnazioneAlloggio(String titolo, String emailRifugiato);

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
}
