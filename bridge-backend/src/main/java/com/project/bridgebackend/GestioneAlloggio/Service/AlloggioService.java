package com.project.bridgebackend.GestioneAlloggio.Service;

import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.dto.AlloggioDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AlloggioService {
    /**
     *Firma del metodo che aggiunge un nuovo alloggio
     * @Param alloggio alloggio da inserire nel sistema
     * @Param titolo titolo dell'alloggio
     * rimuovere
     */
    public Alloggio addAlloggio(Alloggio alloggio);

    /**
     *Firma del metodo che invia una email al Rifugiato
     * @Param message messaggio che sarà mostrato nella email
     * @Param idRifugiato id del rifugiato a cui inviare l'email
     */
    public void sendEmailRifugiato(String message, String emailRifugiato);

    /**
     *Firma del metodo che manifesta interesse per un alloggio
     * @Param rifugiato il rifugiato che ha manifestato interesse
     * @Parama alloggio a quale alloggio ha manifestato interesse il rifugiato
     */
    public boolean manifestazioneInteresse(String emailRifugiato, String emailAlloggio);

    /**
     *Firma del metodo che assegna un alloggio ad un rifugiato
     * @Param idAlloggio l'alloggio che è sato assegnato al rifugiato
     * @Param idRifugiato l'identificativo del rifugiato a cui è stato assegnato un alloggio
     */
    public Alloggio assegnazioneAlloggio(String titolo, String emailRifugiato);

    /**
     *Firma del metodo che notifica il volontario nel caso in cui ci sia una nuova manifestazione di interesse per il suo annuncio
     * @Param message messaggio che viene inviato al volontario
     * @Param idVolontario id del volontario da notificare
     */
    public void sendEmailVolontario(String message, String emailVolontario);

    /**
     *Firma del metodo che mostra tutti gli alloggi presenti nel sistema
     */
    public List<Alloggio> getAllAlloggio();

    /**
     * Firma del metodo per salvare l'indirizzo dell'alloggio
     * @param indirizzo indirizzo dell'alloggio con tutte le caratteristiche
     * @return l'indirizzo salvato
     */
    long salvaIndirizzoAlloggio(Indirizzo indirizzo);
    Alloggio getAlloggioByTitolo(@RequestBody String titolo);

    long getIdIndirizzo(Indirizzo indirizzo);

}
