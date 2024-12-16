package com.project.bridgebackend.GestioneAlloggio.FotoAlloggio;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document("foto_alloggio")
public class FotoAlloggio {
    @Id
    private String id;
    private String nome;
    private byte[] data;
}
