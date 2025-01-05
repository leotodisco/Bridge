package com.project.bridgebackend.CDN.Document;


import com.project.bridgebackend.CDN.CDNDocument;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Geraldine Montella.
 * Questa classe rappresenta il documento pdf
 * memorizzato nel database MongoDB.
 *
 * <p>Ogni istanza di questa classe corrisponde a un documento
 * nella collezione "pdf" del database MongoDB.
 * Contiene informazioni relative ai pdf
 * degli utenti, tra cui un ID univoco, un nome descrittivo,
 * e i dati binari dei pdf.</p>
 */
@Document(collection = "pdf")
@Data
public class PDFDoc implements CDNDocument {

    /**
     * ID univoco del documento, utilizzato come chiave primaria nel database MongoDB.
     * Generato automaticamente da MongoDB al momento dell'inserimento.
     */
    @Id
    private String id;

    /**
     * Nome descrittivo del pdf, utilizzato per identificare il contenuto
     * in modo umano leggibile (non necessariamente unico).
     */
    private String nomePdf;

    /**
     * Dati binari del pdf, rappresentati come array di byte.
     * Contiene il contenuto effettivo del pdf.
     */
    private byte[] pdf;
}
