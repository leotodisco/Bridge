package com.project.bridgebackend.Model.dao;

import com.project.bridgebackend.CDN.Document.FotoProfilo;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * @author
 * Geraldine Montella.

 * Questa interfaccia rappresenta il repository per la gestione delle foto profilo,
 * nel database. Si basa su Spring Data MongoDB e fornisce metodi CRUD (Create,
 * Read, Update, Delete) per interagire con il database MongoDB in modo semplice e strutturato.
 *
 * <p>Estendendo l'interfaccia {@link MongoRepository}, eredita automaticamente,
 * una serie di metodi predefiniti per operazioni standard come il salvataggio,
 * la ricerca, l'eliminazione e l'aggiornamento di documenti nel database MongoDB.</p>
 * FotoProfilo è Il tipo del documento gestito dal repository.
 * String Il tipo dell'ID del documento.
 */
public interface FotoProfiloRepository extends MongoRepository<FotoProfilo, String> {
}
