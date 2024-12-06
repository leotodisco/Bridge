package com.project.bridgebackend.GestioneCorso.Service;

import com.project.bridgebackend.Model.Entity.Corso;
import com.project.bridgebackend.Model.Entity.enumeration.Lingua;
import com.project.bridgebackend.Model.dao.CorsoDAO;
import com.project.bridgebackend.Model.dto.CorsoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Biagio Gallo.
 * Creato il: 06/12/2024.
 * Implementazione del service per la gestione dei corsi.
 * Questa classe fornisce metodi per
 * creare, modificare, eliminare e trovare corsi.
 * Utilizza il CorsoDAO per interagire con il database.
 * L'annotazione @RequiredArgsConstructor di Lombok
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
     * Crea un nuovo corso.
     * @param corsoDTO il DTO del corso contenente i dettagli del corso.
     * @return il corso creato come CorsoDTO.
     */
    @Override
    public CorsoDTO creaCorso(final CorsoDTO corsoDTO) {
        // Crea una nuova entità Corso e imposta le sue proprietà dal CorsoDTO
        Corso corso = new Corso();
        corso.setDescrizione(corsoDTO.getDescrizione());
        corso.setCategoriaCorso(corsoDTO.getCategoriaCorso());
        corso.setTitolo(corsoDTO.getTitolo());
        corso.setPdf(corsoDTO.getPdf());
        corso.setLingua(Lingua.valueOf(corsoDTO.getLingua()));

        // Salva l'entità Corso nel database
        Corso savedCorso = corsoDAO.save(corso);

        // Crea un nuovo CorsoDTO e imposta le sue
        // proprietà dall'entità Corso salvata
        CorsoDTO result = new CorsoDTO();
        result.setDescrizione(savedCorso.getDescrizione());
        result.setCategoriaCorso(savedCorso.getCategoriaCorso());
        result.setTitolo(savedCorso.getTitolo());
        result.setPdf(savedCorso.getPdf());
        result.setLingua(savedCorso.getLingua().name());

        // Ritorna il CorsoDTO creato
        return result;
    }

    /**
     * Modifica un corso esistente.
     * @param corsoDTO il DTO del corso contenente
     * i dettagli aggiornati del corso.
     * @return il corso modificato come CorsoDTO.
     */
    @Override
    public CorsoDTO modificaCorso(final CorsoDTO corsoDTO) {
        // Implementazione per modificare un corso
        return null; // Placeholder return statement
    }

    /**
     * Elimina un corso esistente.
     * @param corsoDTO il DTO del corso contenente
     * i dettagli del corso da eliminare.
     */
    @Override
    public void eliminaCorso(final CorsoDTO corsoDTO) {
        // Recupera tutte le entità Corso dal database
        List<Corso> corsi = corsoDAO.findAll();
        Corso corsoToDelete = null;

        // Itera attraverso la lista per trovare un corso che
        // corrisponde ai dettagli nel CorsoDTO
        for (Corso corso : corsi) {
            if (corso.getDescrizione().equals(corsoDTO.getDescrizione())
                    && corso.getCategoriaCorso().equals(corsoDTO.getCategoriaCorso())
                    && corso.getTitolo().equals(corsoDTO.getTitolo())
                    && corso.getPdf().equals(corsoDTO.getPdf())
                    && corso.getLingua().name().equals(corsoDTO.getLingua())) {
                corsoToDelete = corso;
                break;
            }
        }

        // Se non viene trovato nessun corso corrispondente, lancia un'eccezione
        if (corsoToDelete == null) {
            throw new IllegalArgumentException("Corso non trovato");
        }

        // Elimina il corso trovato dal database
        corsoDAO.delete(corsoToDelete);
    }

    /**
     * Trova tutti i corsi.
     * @return una lista di tutti i corsi come CorsoDTO.
     */
    @Override
    public List<CorsoDTO> findAll() {
        // Recupera tutte le entità Corso dal database
        List<Corso> corsi = corsoDAO.findAll();

        // Crea una lista per contenere i CorsoDTO
        List<CorsoDTO> corsoDTOList = new ArrayList<>();

        // Converte ogni entità Corso in un CorsoDTO e lo aggiunge alla lista
        for (Corso corso : corsi) {
            CorsoDTO corsoDTO = new CorsoDTO();
            corsoDTO.setDescrizione(corso.getDescrizione());
            corsoDTO.setCategoriaCorso(corso.getCategoriaCorso());
            corsoDTO.setTitolo(corso.getTitolo());
            corsoDTO.setPdf(corso.getPdf());
            corsoDTO.setLingua(corso.getLingua().name());
            corsoDTOList.add(corsoDTO);
        }

        // Ritorna la lista di CorsoDTO
        return corsoDTOList;
    }
}
