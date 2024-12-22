package com.project.bridgebackend.fotoProfilo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Geraldine Montella.
 * Questa classe rappresenta il servizio per la gestione delle foto profilo,
 * salvate nel database. Fornisce funzionalità per salvare, recuperare ed,
 * eliminare immagini utilizzando un repository basato su cloud.
 * */

@Service
public class FotoProfiloService {

    /**
     * Repository per accedere e gestire i dati relativi alle foto profilo.
     * Permette operazioni CRUD (Create, Read, Update, Delete).
     */
    @Autowired
    private FotoProfiloRepository fotoProfiloRepository;

    /**
     *
     * @param nome non identificativo, ma caratterizzante il contenuto della,
     *             foto inserita.
     * @param file stream di byte relativo al contenuto della foto.
     * @return stringa dell'id generato autoamaticamente nel database in cloud,
     *         e che segnala un corretto inserimento della foto.
     * @throws IOException Eccezione lanciata in caso di errori durante l'operazione di salvataggio.
     */
    public String saveIMG(final String nome,
                          final byte[] file) throws IOException {
        FotoProfilo fotoProfilo = new FotoProfilo();
        fotoProfilo.setNome(nome);
        fotoProfilo.setData(file);
        FotoProfilo fotoSalvata = fotoProfiloRepository.save(fotoProfilo);
        return fotoSalvata.getId();
    }

    /**
     * Recupera una foto profilo dal database utilizzando il suo ID univoco.
     * @param id L'ID della foto da recuperare.
     * @return Un oggetto FotoProfilo contenente i dati della foto, o null se non trovata.
     * @throws IOException Eccezione lanciata in caso di errori durante l'operazione di recupero.
     */
    public FotoProfilo getIMG(final String id) throws IOException {
        return fotoProfiloRepository.findById(id).orElse(null); }

    /**
     * Elimina una foto profilo dal database utilizzando il suo ID univoco.
     * @param id L'ID della foto da eliminare.
     * @throws IOException Eccezione lanciata in caso di errori durante l'operazione di eliminazione.
     */
    public void deleteIMG(final String id) throws IOException {
        fotoProfiloRepository.deleteById(id); }
}
