package com.project.bridgebackend.GestioneCorso.pdf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Geraldine Montella.
 * Questa classe rappresenta il servizio per la gestione dei pdf,
 * salvate nel database. Fornisce funzionalità per salvare, recuperare ed,
 * eliminare pdf utilizzando un repository basato su cloud.
 * */
@Service
public class PDFService {

    /**
     * Repository per accedere e gestire i dati relativi ai pdf.
     * Permette operazioni CRUD (Create, Read, Update, Delete).
     */
    @Autowired
    private PDFRepository pdfRep;

    /**
     *
     * @param nome non identificativo, ma caratterizzante il contenuto del,
     *             pdf inserito.
     * @param file stream di byte relativo al contenuto del pdf inserito.
     * @return stringa dell'id generato automaticamente nel database in cloud,
     *         e che segnala un corretto inserimento del pdf.
     * @throws IOException Eccezione lanciata in caso di errori,
     * durante l'operazione di salvataggio.
     */
    public PDFDoc savePdf(
            final String nome,
            final MultipartFile file) throws IOException {
                PDFDoc pdf = new PDFDoc();
                pdf.setNomePdf(nome);
                pdf.setPdf(file.getBytes());
                return pdfRep.save(pdf);
    }

    /**
     * Recupera un pdf dal database utilizzando il suo ID univoco.
     * @param id L'ID del pdf da recuperare.
     * @return Un oggetto PDFDoc contenente i dati del pdf, o null se non trovata.
     */
    public PDFDoc getPdf(
            final String id) {
            return pdfRep.findById(id).orElse(null);
    }


    /**
     * Elimina un pdf dal database utilizzando il suo ID univoco.
     * @param id L'ID del pdf da eliminare.
     */
    public void deletePdf(
            final String id) {
        pdfRep.deleteById(id);
    }
}
