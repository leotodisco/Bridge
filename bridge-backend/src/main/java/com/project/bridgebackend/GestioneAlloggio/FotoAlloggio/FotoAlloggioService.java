package com.project.bridgebackend.GestioneAlloggio.FotoAlloggio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FotoAlloggioService {

    /**
     * Repository per accedere e gestire i dati relativi alle foto alloggio.
     * Permette operazioni CRUD (Create, Read, Update, Delete).
     */
    @Autowired
    private FotoAlloggioRepository fotorep;

    /**
     *
     * @param nome non identificativo, ma caratterizzante il contenuto della,
     *             foto inserita.
     * @param file stream di byte relativo al contenuto della foto.
     * @return stringa dell'id generato automaticamente nel database in cloud,
     *         e che segnala un corretto inserimento della foto.
     * @throws IOException Eccezione lanciata in caso di errori durante,
     * l'operazione di salvataggio.
     */
    public String saveIMG(
            final String nome,
            final byte[] file) throws IOException {
        FotoAlloggio fotoAlloggio = new FotoAlloggio();
        fotoAlloggio.setNome(nome);
        fotoAlloggio.setData(file);
        FotoAlloggio fotoSalvata = fotorep.save(fotoAlloggio);
        return fotoSalvata.getId();
    }

    /**
     * Recupera una foto alloggio dal database utilizzando il suo ID univoco.
     * @param id L'ID della foto da recuperare.
     * @return Un oggetto FotoAlloggio contenente i dati della foto,
     * o null se non trovata.
     * @throws IOException Eccezione lanciata,
     * in caso di errori durante l'operazione di recupero.
     */
    public FotoAlloggio getIMG(final String id) throws IOException {
        return fotorep.findById(id).orElse(null); }

    /**
     * Elimina una foto alloggio dal database utilizzando il suo ID univoco.
     * @param id L'ID della foto da eliminare.
     * @throws IOException Eccezione lanciata,
     * in caso di errori durante l'operazione di eliminazione.
     */
    public void deleteIMG(final String id) throws IOException {
        fotorep.deleteById(id); }
}
