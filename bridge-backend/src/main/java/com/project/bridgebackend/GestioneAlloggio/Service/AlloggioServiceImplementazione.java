package com.project.bridgebackend.GestioneAlloggio.Service;

import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.dao.AlloggioDAO;
import com.project.bridgebackend.Model.dao.RifugiatoDAO;
import com.project.bridgebackend.Model.dao.UtenteDAO;
import com.project.bridgebackend.Model.dao.VolontarioDAO;
import com.project.bridgebackend.Model.dto.AlloggioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

    /**
     * Metodo per l'aggiunta di un nuovo alloggio nel sistema
     * Precondizione: l'alloggio non deve essere nullo
     * @Param alloggio l'alloggio che si desidera caricare
     * @Param idAlloggio l'identificativo dell'alloggio
     *
     * @Return true o false
     * PostCondizione l'alloggio sarà caricato nel sistema
     */
    @Override
    public boolean addAlloggio(AlloggioDTO alloggioDTO, List<String> fotoIds) {
        if (alloggioDTO == null) {
            throw new IllegalArgumentException("Il DTO dell'alloggio non può essere nullo");
        }

        // Recupera il volontario dal database utilizzando l'email
        Volontario proprietario = volontarioDAO.findByEmail(alloggioDTO.getEmailProprietario());
        if (proprietario == null) {
            throw new IllegalArgumentException("Proprietario dell'alloggio non trovato");
        }

        // Converti il DTO in un'entità Alloggio
        Alloggio alloggio = new Alloggio();
        alloggio.setDescrizione(alloggioDTO.getDescrizione());
        alloggio.setMaxPersone(alloggioDTO.getMaxPersone());
        alloggio.setFoto(fotoIds);
        alloggio.setProprietario(proprietario);
        alloggio.setMetratura(alloggioDTO.getMetratura());
        alloggio.setServizi(alloggioDTO.getServizi());

        // Salva l'entità nel database
        alloggioDAO.save(alloggio);
        return true;
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
     *
     * @Return void
     *
     * PostCondizione: Il rifugiato mostra interesse per un alloggio
     */

    @Override
    public boolean manifestazioneInteresse(Rifugiato rifugiato, Alloggio alloggio) {

        if(alloggio == null){
            throw new IllegalArgumentException("L'allogio non può essere nullo");
        }

        if(rifugiato == null){
            throw new IllegalArgumentException("Il rifugiato non può essere nullo");
        }

        Optional<Alloggio> interesseHelper = alloggioDAO.findById(alloggio.getId());

        if(interesseHelper.isEmpty()){
            throw new IllegalArgumentException("Alloggio non trovato");
        }

        Alloggio interesse = interesseHelper.get();

        if(!interesse.getListaCandidati().contains(rifugiato)){
            interesse.getListaCandidati().add(rifugiato);
            alloggioDAO.save(interesse);
            return true;
        }
        else{
            throw new IllegalArgumentException("Il rifugiato già ha mostrato interesse per questo alloggio");
        }
    }


    //cambiare in valore di ritorno di manifestazione interesse
    @Override
    public Alloggio assegnazioneAlloggio(long idAlloggio, String emailRifugiato) {

        Optional <Alloggio> alloggioOptional = alloggioDAO.findById(idAlloggio);

        if(alloggioOptional == null || !alloggioOptional.isPresent()){
            throw new IllegalArgumentException("Alloggio non trovato");
        }

        Rifugiato rifugiato = rifugiatoDAO.findByEmail(emailRifugiato);

        if(rifugiato == null){
            throw new IllegalArgumentException("Il rifugiato non trovato");
        }

        Alloggio alloggio = alloggioOptional.get();

        //Se non ha mostrato interesse per l'alloggio
        if(!manifestazioneInteresse(rifugiato, alloggio)){
            throw new IllegalArgumentException("Il rifugiato non ha mostrato interesse per questo alloggio");
        }
        else{ // se ha mostrato interesse per l'alloggio
            alloggio.getListaCandidati().clear();
            alloggio.getListaCandidati().add(rifugiato);

            alloggioDAO.save(alloggio);
            return alloggio;
            //throw new IllegalArgumentException("Già mostrato interesse");
        }
    }

    @Override
    public void sendEmailVolontario(String message, String emailVolontario) {

    }

    @Override
    public List<Alloggio> getAllAlloggio() {
        return alloggioDAO.findAll();
    }
}
