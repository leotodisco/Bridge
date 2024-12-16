package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.Corso;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Utente;
import com.project.bridgebackend.Model.Entity.enumeration.Lingua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @author Biagio Gallo.
 * Creato il 04/12/2024.
 * Questa classe rappresenta il DAO della classe Corso.
 */


@Repository
public interface CorsoDAO extends JpaRepository<Corso, Long> {

    /**
     * Questo metodo permette di trovare un corso tramite il suo titolo.
     * @param titolo titolo del corso da trovare.
     * @return il corso trovato.
     */
    Corso findByTitolo(String titolo);

    /**
     * Questo metodo permette di trovare un corso tramite il proprietario.
     * @param proprietario proprietario del corso da trovare.
     * @return il corso trovato.
     */
    Corso findByProprietario(FiguraSpecializzata proprietario);

    /**
     * Questo metodo permette di trovare un corso tramite la lingua.
     * @param lingua lingua del corso da trovare.
     * @return il corso trovato.
     */
    Corso findByLingua(Lingua lingua);
}
