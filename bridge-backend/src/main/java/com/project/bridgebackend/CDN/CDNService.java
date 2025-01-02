package com.project.bridgebackend.CDN;

import com.project.bridgebackend.CDN.Document.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CDNService {
    @Autowired
    private FotoAlloggioRepository fotoAlloggioRepository;

    @Autowired
    private PDFDocRepository pdfDocRepository;

    @Autowired
    private FotoProfiloRepository fotoProfiloRepository;


    // Salvataggio foto profilo
    public String saveFotoProfilo(final String nome, final byte[] file) throws IOException {
        FotoProfilo fotoProfilo = new FotoProfilo();
        fotoProfilo.setNome(nome);
        fotoProfilo.setData(file);
        FotoProfilo fotoSalvata = fotoProfiloRepository.save(fotoProfilo);
        return fotoSalvata.getId();
    }

    // Recupero foto profilo
    public FotoProfilo getFotoProfilo(final String id) throws IOException {
        return fotoProfiloRepository.findById(id).orElse(null);
    }

    // Eliminazione foto profilo
    public void deleteFotoProfilo(final String id) throws IOException {
        fotoProfiloRepository.deleteById(id);
    }

    // Salvataggio foto alloggio
    public String saveFotoAlloggio(final String nome, final byte[] file) throws IOException {
        FotoAlloggio fotoAlloggio = new FotoAlloggio();
        fotoAlloggio.setNome(nome);
        fotoAlloggio.setData(file);
        FotoAlloggio fotoSalvata = fotoAlloggioRepository.save(fotoAlloggio);
        return fotoSalvata.getId();
    }

    // Recupero foto alloggio
    public FotoAlloggio getFotoAlloggio(final String id) throws IOException {
        return fotoAlloggioRepository.findById(id).orElse(null);
    }

    // Eliminazione foto alloggio
    public void deleteFotoAlloggio(final String id) throws IOException {
        fotoAlloggioRepository.deleteById(id);
    }

    // Salvataggio PDF
    public PDFDoc savePdf(final String nome, final MultipartFile file) throws IOException {
        PDFDoc pdf = new PDFDoc();
        pdf.setNomePdf(nome);
        pdf.setPdf(file.getBytes());
        return pdfDocRepository.save(pdf);
    }

    // Recupero PDF
    public PDFDoc getPdf(final String id) {
        return pdfDocRepository.findById(id).orElse(null);
    }

    // Eliminazione PDF
    public void deletePdf(final String id) {
        pdfDocRepository.deleteById(id);
    }
}
