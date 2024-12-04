package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.Corso;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.Entity.enumeration.Lingua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author Biagio Gallo.
 * Creato il 04/12/2024.
 * Questa classe rappresenta il DAO della classe Corso.
 */


@Repository
public interface CorsoDAO extends JpaRepository<Corso, Long> {

    /**
     * Questo metodo permette di trovare un corso tramite il suo proprietario
     * @param proprietario proprietario del corso
     * @return il corso trovato.
     */
    List<Corso> findByProprietario(Volontario proprietario);

    /**
     * Questo metodo permette di trovare un corso tramite il genere
     * @param categoriaCorso genere del corso
     * @return il corso trovato.
     */
    Corso findByCategoriaCorso(String categoriaCorso);

    /**
     * Questo metodo permette di trovare un corso tramite la lingua
     * @param lingua lingua del corso
     * @return il corso trovato.
     */
    Corso findByLingua(Lingua lingua);

}
