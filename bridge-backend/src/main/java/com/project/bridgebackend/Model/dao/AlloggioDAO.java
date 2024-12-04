package com.project.bridgebackend.Model.dao;


/**
 * @author: Mario Zurolo
 * created: 3/12/24
 * entity per la gestione degli alloggi
 */

import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlloggioDAO {
    /**
     *Firma del metodo che aggiunge un nuovo alloggio
     * @Param alloggio alloggio da inserire nel sistema
     */
    public boolean addAlloggio(Alloggio alloggio);

    /**
     *Firma del metodo che invia una email al Rifugiato
     * @Param message messaggio che sarà mostrato nella email
     * @Param idRifugiato id del rifugiato a cui inviare l'email
     */
    public void sendEmailRifugiato(String message, int idRifugiato);

    /**
     *Firma del metodo che manifesta interesse per un alloggio
     * @Param rifugiato il rifugiato che ha manifestato interesse
     * @Parama alloggio a quale alloggio ha manifestato interesse il rifugiato
     */
    public void manifestazioneInteresse(Rifugiato rifugiato, Alloggio alloggio);

    /**
     *Firma del metodo che assegna un alloggio ad un rifugiato
     * @Param idAlloggio l'alloggio che è sato assegnato al rifugiato
     * @Param idRifugiato l'identificativo del rifugiato a cui è stato assegnato un alloggio
     */
    public Alloggio assegnazioneAlloggio(int idAlloggio, int idRifugiato);

    /**
     *Firma del metodo che notifica il volontario nel caso in cui ci sia una nuova manifestazione di interesse per il suo annuncio
     * @Param message messaggio che viene inviato al volontario
     * @Param idVolontario id del volontario da notificare
     */
    public void sendEmailVolontario(String message, int idVolontario);

    /**
     *Firma del metodo che mostra tutti gli alloggi presenti nel sistema
     */
    public List <Alloggio> getAllAlloggio();
}
