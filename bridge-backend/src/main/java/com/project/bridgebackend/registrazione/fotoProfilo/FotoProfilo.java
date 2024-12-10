package com.project.bridgebackend.registrazione.fotoProfilo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "foto_profilo")
@Data
public class FotoProfilo {
    @Id
    private String id;
    private String nome;
    private byte[] data;
}
