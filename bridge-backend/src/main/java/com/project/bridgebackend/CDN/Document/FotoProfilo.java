package com.project.bridgebackend.CDN.Document;

import com.project.bridgebackend.CDN.CDNDocument;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * @author Geraldine Montella.
 * Questa classe rappresenta il documento FotoProfilo
 * memorizzato nel database MongoDB.
 *
 * <p>Ogni istanza di questa classe corrisponde a un documento
 * nella collezione "foto_profilo" del database MongoDB.
 * Contiene informazioni relative alle foto profilo
 * degli utenti, tra cui un ID univoco, un nome descrittivo,
 * e i dati binari della foto.</p>
 */
@Document(collection = "foto_profilo")
@Data
public class FotoProfilo {

    /**
     * ID univoco del documento, utilizzato come chiave primaria nel database MongoDB.
     * Generato automaticamente da MongoDB al momento dell'inserimento.
     */
    @Id
    private String id;

    /**
     * Nome descrittivo della foto profilo, utilizzato per identificare il contenuto
     * in modo umano leggibile (non necessariamente unico).
     */
    private String nome;

    /**
     * Dati binari della foto, rappresentati come array di byte.
     * Contiene il contenuto effettivo dell'immagine caricata.
     */
    private byte[] data;
}
