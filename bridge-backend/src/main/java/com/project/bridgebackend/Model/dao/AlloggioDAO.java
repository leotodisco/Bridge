package com.project.bridgebackend.Model.dao;


/**
 * @author: Mario Zurolo
 * created: 3/12/24
 * entity per la gestione degli alloggi
 */

import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlloggioDAO extends JpaRepository<Alloggio, Long> {

    Optional<Alloggio> findById(long id);
}
