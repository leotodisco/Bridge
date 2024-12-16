package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.Model.Entity.Lavoro;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.bridgebackend.Model.Entity.Volontario;
import org.springframework.stereotype.Repository;
import com.project.bridgebackend.Model.Entity.Utente;
import java.util.List;

/*
 * @author Vito Vernellati.
 * Creato il: 04/12/2024.
 * Questa Java Persistence Entity per un Annuncio di Lavoro,
 * la inheritance è settata a JOINED perciò Ogni entità ha la propria tabella,
 * e le tabelle sono unite tramite chiavi primarie.
 */

@Repository
public interface LavoroDAO extends JpaRepository<Lavoro, Long> {
    List<Lavoro> findByProprietario(Utente proprietario);
}
