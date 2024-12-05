package com.project.bridgebackend.Model.dao;

import org.springframework.stereotype.Repository;

/*
 * @author Geraldine Montella.
 * Creato il: 03/12/2024.
 * Questa Java Persistence Entity per un annuncio,
 * la inheritance è settata a JOINED perciò Ogni entità ha la propria tabella,
 * e le tabelle sono unite tramite chiavi primarie.
 */
@Repository
public interface ConsulenzaDAO extends AnnuncioDAO{

}
