package com.project.bridgebackend.GestioneCorso.pdf;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pdf")
@Data
public class PDFDoc {
    @Id
    private String id;
    private String nome_pdf;
    private byte[] pdf;
}
