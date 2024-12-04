package com.project.bridgebackend.GestioneAlloggio.Service;

import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.Entity.Utente;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.dao.AlloggioDAO;
import com.project.bridgebackend.Model.dao.VolontarioDAO;
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
    private AlloggioDAO alloggioDAO;

    /**
     * Metodo per l'aggiunta di un nuovo alloggio nel sistema
     * Precondizione l'alloggio non deve essere nullo
     * @Param alloggio l'alloggio che si desidera caricare
     * @Param idAlloggio l'identificativo dell'alloggio
     *
     * @Return true o false
     * PostCondizione l'alloggio sarà caricato nel sistema
     */
    @Override
    public boolean addAlloggio(Alloggio alloggio, int idAlloggio) {

        if(alloggio == null){
            return false;
        }

        Volontario volontario = volontarioDAO.findByEmail(alloggio.getProprietario().getEmail());

        if(volontario == null){
            return false;
        }

        alloggio.setProprietario((Volontario) volontario);

        alloggioDAO.save(alloggio);
        return true;
    }

    @Override
    public void sendEmailRifugiato(String message, int idRifugiato) {

    }

    @Override
    public void manifestazioneInteresse(Rifugiato rifugiato, com.project.bridgebackend.Model.Entity.Alloggio alloggio) {

    }

    @Override
    public com.project.bridgebackend.Model.Entity.Alloggio assegnazioneAlloggio(int idAlloggio, int idRifugiato) {
        return null;
    }

    @Override
    public void sendEmailVolontario(String message, int idVolontario) {

    }

    @Override
    public List<com.project.bridgebackend.Model.Entity.Alloggio> getAllAlloggio() {
        return List.of();
    }
}
