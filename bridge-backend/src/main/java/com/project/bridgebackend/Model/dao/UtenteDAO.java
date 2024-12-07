package com.project.bridgebackend.Model.dao;
import com.project.bridgebackend.Model.Entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Antonio Ceruso.
 * Data creazione: 04/12/2024.
 * Interfaccia che indica un DAO per l'utente.
 * Si usa questa interfaccia come "base".
 * per creare i DAO delle classi figlie di Utente.
 */
@Repository
public interface UtenteDAO extends JpaRepository<Utente, Long> {

    /**
     * Query che ci permette di ricercare un utente dalla email.
     * @param email dell'utente da ricercare.
     * @return utente corrispondente.
     * */
    Utente findByEmail(String email);
}