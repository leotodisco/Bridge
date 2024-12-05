package com.project.bridgebackend.registrazione.service;

import com.project.bridgebackend.Model.Entity.Admin;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.dao.AdminDAO;
import com.project.bridgebackend.Model.dao.FiguraSpecializzataDAO;
import com.project.bridgebackend.Model.dao.RifugiatoDAO;
import com.project.bridgebackend.Model.dao.VolontarioDAO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Antonio Ceruso.
 * Data creazione: 04/12/2024.
 * Classe che rappresenta il service della registrazione
 */
@Service
public class RegistrazioneServiceImpl implements RegistrazioneService {
    /**
     * Si occupa delle operazioni relative all'admin nel db
     * */
    private AdminDAO adminDAO;
    /**
     * Si occupa delle operazioni relative al rifugiato nel db
     * */
    private RifugiatoDAO rifugiatoDAO;
    /**
     * Si occupa delle operazioni relative al volontario nel db
     * */
    @Autowired
    private VolontarioDAO volontarioDAO;
    /**
     * Si occupa delle operazioni relative alla figura specializzata nel db
     * */
    private FiguraSpecializzataDAO figSpecDAO;


    /**
     *Implementazione metodo di registrazione di un volontario
     * @param volontario
     * @return response
     * */
    @Override
    public void registraVolontario(@Valid final Volontario volontario,
                                   String confermaPW)
            throws Exception {
        if(volontario == null){
            throw new IllegalArgumentException("Volontario non valido");
        }else if(volontarioDAO.findByEmail(volontario.getEmail())!= null){
            throw new IllegalArgumentException("Volontario già presente");
        }
        if(!volontario.getPassword().equals(confermaPW)){
            throw new IllegalArgumentException("Le due password non combaciano");
        }
        volontario.setPassword(confermaPW);
        volontarioDAO.save(volontario);
    }

    /**
     *Implementazione metodo di registrazione di un rifugiato
     * @param rifugiato
     * @return response
     * */
    @Override
    public void registraRifugiato(@Valid final Rifugiato rifugiato)
            throws Exception {

    }

    /**
     *Implementazione metodo di registrazione di un admin
     * @param admin
     * @return response
     * */
    @Override
    public void registraAdmin(@Valid final Admin admin)
            throws Exception {
    }

    /**
     *Implementazione metodo di registrazione di una Figura Specializzata
     * @param figspec
     * @return response
     * */
    @Override
    public void registraFiguraSpecializzata(@Valid final FiguraSpecializzata figspec)
            throws Exception {
    }
}