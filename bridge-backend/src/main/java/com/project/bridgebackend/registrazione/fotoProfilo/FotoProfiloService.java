package com.project.bridgebackend.registrazione.fotoProfilo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FotoProfiloService {
    @Autowired
    private FotoProfiloRepository fotoProfiloRepository;

    public String saveIMG(String nome, byte[] file) throws IOException {
        FotoProfilo fotoProfilo = new FotoProfilo();
        fotoProfilo.setNome(nome);
        fotoProfilo.setData(file);
        FotoProfilo fotoSalvata = fotoProfiloRepository.save(fotoProfilo);
        return fotoSalvata.getId();
    }

    public FotoProfilo getIMG(String id) throws IOException { return fotoProfiloRepository.findById(id).orElse(null); }

    public void deleteIMG(String id) throws IOException { fotoProfiloRepository.deleteById(id); }
}
