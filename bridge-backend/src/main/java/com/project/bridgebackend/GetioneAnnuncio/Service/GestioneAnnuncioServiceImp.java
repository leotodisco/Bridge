package com.project.bridgebackend.GetioneAnnuncio.Service;

import com.project.bridgebackend.Model.Entity.Consulenza;
import com.project.bridgebackend.Model.dao.ConsulenzaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Valid
@Service
public class GestioneAnnuncioServiceImp implements GestioneAnnuncioService {
    @Autowired
    private ConsulenzaDAO consulenzaDAO;

    @Override
    public Consulenza inserimentoConsulenza(Consulenza consulenza) {
        // Validazioni custom (se necessarie)
        if (consulenza.getMaxCandidature() < 1) {
            throw new IllegalArgumentException("Il numero massimo di candidature deve essere almeno 1.");
        }
        // Altri controlli sul dominio possono essere aggiunti qui

        // Salva la consulenza nel database
        return consulenzaDAO.save(consulenza);
    }

}
