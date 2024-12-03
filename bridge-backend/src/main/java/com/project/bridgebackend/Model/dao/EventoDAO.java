package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.Indirizzo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alessia De Filippo.
 * creato il 03/12/2024.
 * DAO della classe Evento.
 */

@Repository
public interface EventoDAO extends JpaRepository<Indirizzo, Long> {

}
