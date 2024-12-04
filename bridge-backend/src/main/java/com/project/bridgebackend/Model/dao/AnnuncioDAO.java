package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.Annuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnuncioDAO extends JpaRepository<Annuncio, Long> {

    /**
     * la funzione del fetch per tipologia
     * @param tipologia è un enum
     * @return
     */
    List<Annuncio> findByTipologia(Boolean tipologia);
}
