package com.project.bridgebackend.GestioneUtente.Service;

import com.project.bridgebackend.Model.Entity.Utente;
import com.project.bridgebackend.Model.dao.UtenteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtenteServiceImpl implements UtenteService {


    @Autowired
    private UtenteDAO utenteDAO;

    @Override
    public void eliminaUtente(String email) throws Exception {
        try {
            utenteDAO.delete(utenteDAO.findByEmail(email));
        }catch(Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Utente getUtente(String email) {
        Utente u = utenteDAO.findByEmail(email);
        return u;
    }
}
