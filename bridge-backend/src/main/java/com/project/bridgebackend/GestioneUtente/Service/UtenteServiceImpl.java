package com.project.bridgebackend.GestioneUtente.Service;

import com.project.bridgebackend.Model.Entity.Utente;
import com.project.bridgebackend.Model.dao.UtenteDAO;
import com.project.bridgebackend.fotoProfilo.FotoProfiloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @author Antonio Ceruso
 * Data creazione: 12/12/2024
 * Classe che implemeta il service per l'utente
 * */
@Service
public class UtenteServiceImpl implements UtenteService {

    @Autowired
    private FotoProfiloService fotoProfiloService;

    @Autowired
    private UtenteDAO utenteDAO;

    @Override
    public void eliminaUtente(String email) throws Exception {
        try {
            String idFoto = utenteDAO.findByEmail(email).getFotoProfilo();
            utenteDAO.delete(utenteDAO.findByEmail(email));
            fotoProfiloService.deleteIMG(idFoto);
        }catch(Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Utente getUtente(String email) {
        Utente u = utenteDAO.findByEmail(email);
        return u;
    }
}
