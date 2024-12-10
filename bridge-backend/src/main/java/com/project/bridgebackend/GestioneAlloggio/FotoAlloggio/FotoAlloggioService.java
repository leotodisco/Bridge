package com.project.bridgebackend.GestioneAlloggio.FotoAlloggio;

import com.project.bridgebackend.registrazione.fotoProfilo.FotoProfilo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FotoAlloggioService {
    @Autowired
    private FotoAlloggioRepository fotorep;

    public String saveIMG(String nome, byte[] file) throws IOException {
        FotoAlloggio fotoAlloggio = new FotoAlloggio();
        fotoAlloggio.setNome(nome);
        fotoAlloggio.setData(file);
        FotoAlloggio fotoSalvata = fotorep.save(fotoAlloggio);
        return fotoSalvata.getId();
    }

    public FotoAlloggio getIMG(String id) throws IOException { return fotorep.findById(id).orElse(null); }

    public void deleteIMG(String id) throws IOException { fotorep.deleteById(id); }
}
