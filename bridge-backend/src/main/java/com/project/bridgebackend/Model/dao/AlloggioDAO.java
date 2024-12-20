package com.project.bridgebackend.Model.dao;


/**
 * @author: Mario Zurolo
 * created: 3/12/24
 * entity per la gestione degli alloggi
 */

import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Rifugiato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlloggioDAO extends JpaRepository<Alloggio, Long> {

    Optional<Alloggio> findById(long id);
    Optional<Alloggio> findByTitolo(String titolo);

    @Query("SELECT a FROM Alloggio a JOIN FETCH a.proprietario")
    List<Alloggio> findAllAlloggiWithProprietario();

    @Query("SELECT a FROM Alloggio a WHERE a.titolo = :titolo")
    Alloggio findAllAlloggiWithTitolo(@Param("titolo") String titolo);

    Optional<Alloggio>  findAlloggioByTitolo(String titolo);
}
