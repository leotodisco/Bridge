package com.project.bridgebackend.GestioneCorso.pdf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PDFService {
    @Autowired
    private PDFRepository pdfRep;

    public PDFDoc savePdf(String nome, MultipartFile file) throws IOException {
                PDFDoc pdf = new PDFDoc();
                pdf.setNome_pdf(nome);
                pdf.setPdf(file.getBytes());
                return pdfRep.save(pdf);
    }

    public PDFDoc getPdf(String id) {
            return pdfRep.findById(id).orElse(null);
    }

    public void deletePdf(String id) {
        pdfRep.deleteById(id);
    }
}
