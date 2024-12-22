package com.project.bridgebackend.GestioneCorso.Service;

import com.project.bridgebackend.GestioneCorso.pdf.PDFService;
import com.project.bridgebackend.Model.Entity.Corso;
import com.project.bridgebackend.Model.dao.CorsoDAO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author Biagio Gallo.
 * Creato il: 06/12/2024.
 * Implementazione del service per la gestione dei corsi.
 * Questa classe fornisce metodi per,
 * creare, modificare, eliminare e trovare corsi.
 * Utilizza il CorsoDAO per interagire con il database.
 * L'annotazione @RequiredArgsConstructor di Lombok,
 * viene utilizzata per generare un costruttore con gli argomenti richiesti.
 */
@Service
@RequiredArgsConstructor
public class GestioneCorsoServiceImpl implements GestioneCorsoService {

    /**
     * Iniezione del DAO per interagire con il database.
     */
    @Autowired
    private final CorsoDAO corsoDAO;

    /**
     * Iniezione logica di gestione per i pdf.
     */
    @Autowired
    private final PDFService pdfService;
    /**
     * Crea un nuovo corso.
     * @param corso il DTO del corso contenente i dettagli del corso.
     * @return il corso creato come CorsoDTO.
     */

    @Override
    public Corso creaCorso(final Corso corso) {
        if (corso == null) {
            throw new IllegalArgumentException("Corso non valido");
        }

        return corsoDAO.save(corso);
    }
    /**
     * Modifica un corso esistente.
     * todo: inserire possibilità modifica pdf
     * @param corso il DTO del corso contenente.
     * i dettagli aggiornati del corso.
     * @return il corso modificato come CorsoDTO.
     */
    @Override
    public Corso modificaCorso(final Corso corso) {
        if (corso == null || corso.getTitolo() == null || corso.getProprietario() == null) {
            throw new IllegalArgumentException("Il corso o i dettagli richiesti non sono validi");
        }

        Corso existingCorso = corsoDAO.findById(corso.getId())
                .orElseThrow(() -> new IllegalArgumentException("Corso non trovato"));
        if (existingCorso == null) {
            throw new IllegalArgumentException("Corso non trovato");
        }

        // Aggiorna i campi del corso esistente
        existingCorso.setId(corso.getId());
        existingCorso.setTitolo(corso.getTitolo());
        existingCorso.setDescrizione(corso.getDescrizione());
        existingCorso.setCategoriaCorso(corso.getCategoriaCorso());
        existingCorso.setLingua(corso.getLingua());
        existingCorso.setPdf(corso.getPdf());
        return corsoDAO.save(existingCorso);
    }

    /**
     * Elimina un corso esistente.
     * @param corso il DTO del corso contenente.
     * i dettagli del corso da eliminare.
     */
    @Override
    @Transactional
    public void eliminaCorso(final Corso corso) {
        if (corso.getId() == null) {
            throw new IllegalArgumentException("ID del corso non valido");
        }

        Corso existingCorso = corsoDAO.findById(corso.getId())
                .orElseThrow(() -> new IllegalArgumentException("Corso non trovato"));

        // Elimina il PDF associato
        if (existingCorso.getPdf() != null) {
            pdfService.deletePdf(existingCorso.getPdf());
        }

        corsoDAO.delete(existingCorso);
    }

    /**
     * Trova tutti i corsi.
     * @return una lista di tutti i corsi come CorsoDTO.
     */
    @Override
    public List<Corso> findAll() {
        return corsoDAO.findAll();
    }
}
