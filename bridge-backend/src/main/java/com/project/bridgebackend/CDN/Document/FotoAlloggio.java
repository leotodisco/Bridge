package com.project.bridgebackend.CDN.Document;

import com.project.bridgebackend.CDN.CDNDocument;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Geraldine Montella.
 * Questa classe rappresenta le foto di un alloggio,
 * memorizzate nel database MongoDB.
 *
 * <p>Ogni istanza di questa classe corrisponde a un documento
 * nella collezione "foto_alloggio" del database MongoDB.
 * Contiene informazioni relative alle foto degli alloggi,
 * caricati dagli utenti,
 * tra cui un ID univoco, un nome descrittivo,
 * e i dati binari della foto.</p>
 */
@Data
@Document("foto_alloggio")
public class FotoAlloggio implements CDNDocument {

    /**
     * ID univoco del documento,
     * utilizzato come chiave primaria nel database MongoDB.
     * Generato automaticamente da MongoDB al momento dell'inserimento.
     */
    @Id
    private String id;

    /**
     * Nome del file della foto.
     */
    private String nome;


    /**
     * Contenuto della foto in formato binario.
     */
    private byte[] data;
}
