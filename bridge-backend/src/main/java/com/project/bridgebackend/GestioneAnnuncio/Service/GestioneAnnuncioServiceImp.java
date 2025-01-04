package com.project.bridgebackend.GestioneAnnuncio.Service;

import com.project.bridgebackend.GestioneAlloggio.Service.AlloggioServiceImplementazione;
import com.project.bridgebackend.Model.Entity.*;
import com.project.bridgebackend.Model.Entity.enumeration.TipoConsulenza;
import com.project.bridgebackend.Model.Entity.enumeration.TipoContratto;
import com.project.bridgebackend.Model.dao.*;
import com.project.bridgebackend.util.Indirizzo.service.IndirizzoService;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.*;

/**
 * @author Geraldine Montella, Vito Vernellati.
 * Creato il: 04/12/2024.

 * Questa classe implementa il servizio per la gestione degli annunci,
 * interagendo con i repository di Consulenza e Indirizzo per il salvataggio nel database.
 */

@Validated
@Service
public class GestioneAnnuncioServiceImp implements GestioneAnnuncioService {

    /**
     * Repository per la gestione delle consulenze nel database.
     */
    @Autowired
    private ConsulenzaDAO consulenzaDAO;

    /**
     * Repository per la gestione degli indirizzi nel database.
     */
    @Autowired
    private IndirizzoDAO indirizzoDAO;

    /**
     * Repository per la gestione dei rifugiati nel database.
     */
    @Autowired
    private RifugiatoDAO rifugiatoDAO;

    /**
     * Repository per la gestione dei lavori nel database.
     */
    @Autowired
    private LavoroDAO lavoroDAO;

    /**
    * Servizi per la gestione degli indirizzi.
     */
    @Autowired
    private IndirizzoService indirizzoService;

    /**
     * Bean per l'invio di email tramite JavaMailSender.
     * Utilizzato per inviare email al rifugiato e,
     * al volontario nel sistema.
     */

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Validator validator;

    private static final Logger logger = LoggerFactory.getLogger(AlloggioServiceImplementazione.class);
    @Autowired
    private FiguraSpecializzataDAO figuraSpecializzataDAO;

    // Implementazioni dei metodi per Consulenza


    /**
     * Inserisce una nuova consulenza nel database.
     * Verifica che il numero massimo di candidature sia almeno 1.
     *
     * @param consulenza l'oggetto Consulenza da inserire.
     * @return la consulenza salvata nel database.
     */
    @Override
    public Consulenza inserimentoConsulenza(@Valid final Consulenza consulenza) {

        // Tipologia non nulla
        if (consulenza.getTipologia() == null) {
            throw new IllegalArgumentException("La tipologia della consulenza non può essere vuota.");
        }

        // Descrizione non vuota
        if (consulenza.getDescrizione() == null || consulenza.getDescrizione().trim().isEmpty()) {
            throw new IllegalArgumentException("La descrizione della consulenza non può essere vuota.");
        }

        // Titolo non vuoto
        if (consulenza.getTitolo() == null || consulenza.getTitolo().trim().isEmpty()) {
            throw new IllegalArgumentException("Il titolo della consulenza non può essere vuoto.");
        }

        // Numero massimo di candidature deve essere almeno 1
        if (consulenza.getMaxCandidature() < 1 || consulenza.getMaxCandidature() > 99) {
            throw new IllegalArgumentException("Il numero massimo di candidature deve essere almeno 1.");
        }

        // Disponibilità non nulla
        if (consulenza.getDisponibilita() == null) {
            throw new IllegalArgumentException("La disponibilità non può essere nulla.");
        }

        // Numero di telefono valido
        if (consulenza.getNumero() == null || !consulenza.getNumero().matches("^((00|\\+)39[\\. ]??)??3\\d{2}[\\. ]??\\d{6,7}$")) {
            throw new IllegalArgumentException("Il numero di telefono non è valido.");
        }

        // Orari disponibili con formato valido
        if (consulenza.getOrariDisponibili() == null || !consulenza.getOrariDisponibili().matches("^(Lunedi|Martedi|Mercoledi|Giovedi|Venerdi|Sabato|Domenica)\\s+(0\\d|1\\d|2[0-3]):[0-5]\\d\\s*-\\s*(0\\d|1\\d|2[0-3]):[0-5]\\d$")) {
            throw new IllegalArgumentException("Gli orari di disponibilità non rispettano il formato richiesto.");
        }

        // Indirizzo non nullo
        if (consulenza.getIndirizzo() == null) {
            throw new IllegalArgumentException("L'indirizzo non può essere nullo.");
        }

        if(consulenza.getProprietario() == null || figuraSpecializzataDAO.findByEmail(consulenza.getProprietario().getEmail()) == null){
            throw new IllegalArgumentException("Il proprietario non può essere nullo.");
        }

        // Validazione dei campi dell'indirizzo
        Indirizzo indirizzo = consulenza.getIndirizzo();
        if (indirizzo.getCitta() == null || indirizzo.getCitta().trim().isEmpty() || !indirizzo.getCitta().matches("^[A-zÀ-ù ‘]{2,50}$")) {
            throw new IllegalArgumentException("La città non può essere vuota.");
        }
        if (indirizzo.getProvincia() == null || indirizzo.getProvincia().trim().length() != 2) {
            throw new IllegalArgumentException("La provincia deve essere composta da 2 caratteri.");
        }
        if (indirizzo.getCap().length() != 5) {
            throw new IllegalArgumentException("Il CAP deve essere un numero di 5 cifre.");
        }
        if (indirizzo.getVia() == null || indirizzo.getVia().trim().isEmpty() || !indirizzo.getVia().matches("^[A-zÀ-ù ‘]{2,50}$")) {
            throw new IllegalArgumentException("La via non può essere vuota.");
        }
        if(indirizzo.getNumCivico() > 999) {
            throw new IllegalArgumentException("Il numero civico non può essere maggiore di 3 cifre.");
        }

        return consulenzaDAO.save(consulenza);
    }


    /**
     * Salva un indirizzo di consulenza nel database.
     *
     * @param indirizzo l'oggetto Indirizzo da salvare.
     * @return l'ID dell'indirizzo salvato.
     */
    @Override
    public long salvaIndirizzoConsulenza(final Indirizzo indirizzo) {
        indirizzoDAO.save(indirizzo);
        return indirizzo.getId();
    }

    //      **Implementazioni dei metodi per Lavoro**

    /**
     * Inserisce un nuovo annuncio di lavoro nel database.
     * Verifica che la retribuzione sia positiva.
     *
     * @param lavoro l'oggetto Lavoro da inserire.
     * @return l'annuncio di lavoro salvato nel database.
     */
    @Override
    public Lavoro inserimentoLavoro(final Lavoro lavoro) {
        if (lavoro.getRetribuzione() <= 0) {
            throw new IllegalArgumentException("La retribuzione deve essere positiva.");
        }
        return lavoroDAO.save(lavoro);
    }

    /**
     * Salva un indirizzo di lavoro nel database.
     *
     * @param indirizzo l'oggetto Indirizzo da salvare.
     * @return l'ID dell'indirizzo salvato.
     */
    @Override
    public long salvaIndirizzoLavoro(final Indirizzo indirizzo) {
        indirizzoDAO.save(indirizzo);
        return indirizzo.getId();
    }

    @Override
    public List<Lavoro> getAllLavori() {
        return lavoroDAO.findAll();
    }

    @Override
    public Lavoro getLavori(final long id) {
        return lavoroDAO.findById(id).get();
    }

    @Override
    public List<Lavoro> getLavoriByProprietario(final Utente proprietario) {
        return lavoroDAO.findByProprietario(proprietario);
    }

    @Override
    public Lavoro modificaAnnuncioLavoro(final long idAnnuncio,
                                         final HashMap<String, Object> aggiornamenti) {
        Optional<Lavoro> lavoroOptional = lavoroDAO.findById(idAnnuncio);
        if (lavoroOptional.isEmpty()) {
            throw new IllegalArgumentException("Annuncio di lavoro non trovato.");
        }

        Lavoro lavoro = lavoroOptional.get();

        aggiornamenti.forEach((campo, valore) -> {
            switch (campo) {
                case "titolo":
                    lavoro.setTitolo((String) valore);
                    break;
                case "posizioneLavorativa":
                    lavoro.setPosizioneLavorativa((String) valore);
                    break;
                case "nomeAzienda":
                    lavoro.setNomeAzienda((String) valore);
                    break;
                case "orarioLavoro":
                    lavoro.setOrarioLavoro((String) valore);
                    break;
                case "tipoContratto":
                    lavoro.setTipoContratto((TipoContratto) valore);
                    break;
                case "retribuzione":
                    double parsedValue = Double.parseDouble(valore.toString());
                    lavoro.setRetribuzione(parsedValue);
                    break;
                case "nomeSede":
                    lavoro.setNomeSede((String) valore);
                    break;
                case "infoUtili":
                    lavoro.setInfoUtili((String) valore);
                    break;
                case "indirizzo":
                    if (valore instanceof HashMap) {
                        HashMap<String, Object> indirizzoData = (HashMap<String, Object>) valore;

                        Indirizzo indirizzo = lavoro.getIndirizzo(); // L'indirizzo deve già esistere

                        // Aggiorna i campi dell'indirizzo utilizzando un metodo di servizio
                        indirizzoService.aggiornaIndirizzo(indirizzo.getId(), indirizzoData);

                    } else {
                        throw new IllegalArgumentException("Formato indirizzo non valido.");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Campo non valido per la modifica: " + campo);
            }
        });

        return lavoroDAO.save(lavoro);
    }

    @Override
    public void eliminaAnnuncioLavoro(final long idAnnuncio) {
        if (!lavoroDAO.existsById(idAnnuncio)) {
            throw new IllegalArgumentException("Annuncio di lavoro non trovato.");
        }
        lavoroDAO.deleteById(idAnnuncio);
    }

    @Override
    public List<Lavoro> getLavoroByTipoContratto(TipoContratto tipo) {
        return lavoroDAO.findByTipoContratto(tipo);
    }

    //      **Implementazioni dei metodi per Consulenza**

    /**
     * Prelevare tutte le consulenze sulla base dell'identificativo dato in input.
     *
     * @return la consulenze con l'identificativo id.
     */
    @Override
    public List<Consulenza> getAllConsulenze() {
        return consulenzaDAO.findAll();
    }

    /**
     * Prelevare la consulenza sulla base dell'identificativo dato in input.
     *
     * @return la lista di tutte le consulenza presenti nel db.
     */
    @Override
    public Consulenza getConsulenze(final long id) {
        return consulenzaDAO.findById(id).get();
    }

    /**
     * Prelevare gli annunci di una consulenza.
     *
     * @param proprietario l'utente proprietario delle consulenze.
     *
     * @return le consulenze di cui l'utente risulta proprietario.
     */

    @Override
    public List<Consulenza> getConsulenzeByProprietario(final Utente proprietario) {
        return consulenzaDAO.findByProprietario(proprietario);
    }

    /**
     * Prelevare tutti gli annunci di una consulenza,
     * per cui il rifugiato si è candidato.
     *
     * @param candidato l'utente candidato alle consulenze.
     *
     * @return le consulenze di cui l'utente risulta candidato.
     */
    @Override
    public List<Consulenza> getConsulenzeByCandidato(final Utente candidato) {
        return consulenzaDAO.findAllByRifugiatoEmail(candidato.getEmail());
    }

    /**
     * Metodo asincrono che invia una email al volontario.
     *
     * @param messaggio il messaggio da inviare.
     * @param emailVolontario l'email del destinatario.
     */
    @Override
    public void sendEmailVolontario(final String messaggio, final String emailVolontario) {
        logger.info("Invio email a: {}, con messaggio: {}", emailVolontario, messaggio);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("beehaveofficial@gmail.com");
            message.setTo(emailVolontario);
            message.setSubject("Notifica Candidatura Consulenza");
            message.setText(messaggio);
            mailSender.send(message);
            logger.info("Email inviata con successo a: {}", emailVolontario);
        } catch (Exception e) {
            logger.error("Errore nell'invio dell'email a: {} - {}", emailVolontario, e.getMessage(), e);
        }
    }

    /**
     * Metodo che invia una email al rifugiato.
     *
     * @param messaggio il messaggio da inviare
     * @param emailRifugiato l'email del rifugiato destinatario
     */
    @Override
    public void sendEmailRifugiato(final String messaggio,
                                   final String emailRifugiato) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("beehaveofficial@gmail.com");
            message.setTo(emailRifugiato);
            message.setSubject("PROVA");
            message.setText(messaggio);
            mailSender.send(message);
            System.out.println("email inviata");
        } catch (Exception e) {
            System.out.println("Errore nell invio dell'email a: " + emailRifugiato + e.getMessage());
        }

    }
    @Override
    public List<Consulenza> getConsulenzeByTipo(TipoConsulenza tipo) {
        return consulenzaDAO.findByTipo(tipo);
    }

    /**
     * Modifica una consulenza esistente nel database.
     *
     * @param idConsulenza l'ID della consulenza da modificare.
     * @param aggiornamenti mappa contenente i campi da aggiornare e i relativi valori.
     * @return la consulenza aggiornata.
     */
    @Override
    public Consulenza modificaAnnuncioConsulenza(final long idConsulenza,
                                                 final HashMap<String, Object> aggiornamenti) {
        Optional<Consulenza> consulenzaOptional = consulenzaDAO.findById(idConsulenza);
        if (consulenzaOptional.isEmpty()) {
            throw new IllegalArgumentException("Consulenza non trovata.");
        }

        Consulenza consulenza = consulenzaOptional.get();

        aggiornamenti.forEach((campo, valore) -> {
            switch (campo) {
                case "titolo":
                    consulenza.setTitolo((String) valore);
                    break;
                case "descrizione":
                    consulenza.setDescrizione((String) valore);
                    break;
                case "orariDisponibili":
                    consulenza.setOrariDisponibili((String) valore);
                    break;
                case "maxCandidature":
                    int maxCandidature = (Integer) valore;
                    if (maxCandidature < 1) {
                        throw new IllegalArgumentException("Il numero "
                                + "massimo di candidature deve essere almeno 1.");
                    }
                    consulenza.setMaxCandidature(maxCandidature);
                    break;
                case "indirizzo":
                    if (valore instanceof HashMap) {
                        HashMap<String, Object> indirizzoData = (HashMap<String, Object>) valore;

                        Indirizzo indirizzo = consulenza.getIndirizzo(); // L'indirizzo deve già esistere

                        // Aggiorna i campi dell'indirizzo utilizzando un metodo di servizio
                        indirizzoService.aggiornaIndirizzo(indirizzo.getId(), indirizzoData);

                    } else {
                        throw new IllegalArgumentException("Formato indirizzo non valido.");
                    }
                    break;
                case "candidati":
                    if (valore instanceof Map<?,?>) {
                        Map<String,Boolean> nuoviCandidati = (Map<String,Boolean>) valore;
                        consulenza.setCandidati(nuoviCandidati);
                    } else {
                        throw new IllegalArgumentException("Formato candidati non valido.");
                    }
                    break;
                case "numero":
                    consulenza.setNumero((String) valore);
                    break;
                case "tipo":
                    //obbligatorio questo check dato che dall'object si
                    //riesce a castare l'enum direttamente
                    if (valore instanceof String) {
                        try {
                            TipoConsulenza tipoConsulenza = TipoConsulenza.valueOf((String) valore);
                            consulenza.setTipo(tipoConsulenza);
                        } catch (IllegalArgumentException e) {
                            throw new IllegalArgumentException("TipoConsulenza non valido: " + valore);
                        }
                    } else {
                        throw new IllegalArgumentException("Il valore per 'tipo' deve essere una stringa.");
                    }
                    break;
                case "disponibilita":
                    consulenza.setDisponibilita((Boolean) valore);
                    break;
                default:
                    throw new IllegalArgumentException("Campo non valido "
                            + "per la modifica: " + campo);
            }
        });

        return consulenzaDAO.save(consulenza);
    }

    @Override
    public void eliminaConsulenza(final long idConsulenza) {
        if (!consulenzaDAO.existsById(idConsulenza)) {
            throw new IllegalArgumentException("Nessun riscontro con di consulenze con id " + idConsulenza);
        }
        consulenzaDAO.deleteById(idConsulenza);
    }

    @Override
    public void interesseConsulenza(final long idConsulenza, final String emailRifugiato){
        if (emailRifugiato == null || emailRifugiato.trim().isEmpty()) {
            throw new IllegalArgumentException("Email vuota o nulla");
        }
        if (idConsulenza <= 0) {
            throw new IllegalArgumentException("ID consulenza non valido");
        }

        // Recupera il rifugiato dal database
        Rifugiato r = rifugiatoDAO.findByEmail(emailRifugiato);
        if (r == null) {
            throw new IllegalArgumentException("Rifugiato non trovato");
        }

        Consulenza c = consulenzaDAO.findConsulenzaById(idConsulenza);
        if (c == null) {
            throw new IllegalArgumentException("Alloggio non trovato");
        }

        // Verifica se l'email del rifugiato è già nella lista candidati
        if (c.getCandidati().containsKey(r.getEmail())) {
            throw new IllegalArgumentException("Interesse già manifestato");
        }

        c.getCandidati().put(r.getEmail(), false);
        //aggiorno la consulenza
        consulenzaDAO.save(c);
    }

    @Override
    public void rimuoviInteresseConsulenza(final long idConsulenza, final String emailRifugiato){
        if (emailRifugiato == null || emailRifugiato.trim().isEmpty()) {
            throw new IllegalArgumentException("Email vuota o nulla");
        }
        if (idConsulenza <= 0) {
            throw new IllegalArgumentException("ID consulenza non valido");
        }

        // Recupera il rifugiato dal database
        Rifugiato r = rifugiatoDAO.findByEmail(emailRifugiato);
        if (r == null) {
            throw new IllegalArgumentException("Rifugiato non trovato");
        }

        Consulenza c = consulenzaDAO.findConsulenzaById(idConsulenza);
        if (c == null) {
            throw new IllegalArgumentException("Consulenza non trovato");
        }

        // Verifica se l'email del rifugiato non è presente in lista
        if (!c.getCandidati().containsKey(r.getEmail())) {
            throw new IllegalArgumentException(
                    "Il rifugito non risulata interessato alla consulenza");
        }
        //rimuovo dalla lista candidati
        c.getCandidati().remove(r.getEmail());
        //aggiorno la consulenza
        consulenzaDAO.save(c);
    }

    @Override
    public void accettaConsulenzaRifugiato(final long idConsulenza, final String emailRifugiato) {
        if (emailRifugiato == null || emailRifugiato.trim().isEmpty()) {
            throw new IllegalArgumentException("Email vuota o nulla");
        }
        if (idConsulenza <= 0) {
            throw new IllegalArgumentException("ID consulenza non valido");
        }

        // Recupera il rifugiato dal database
        Rifugiato r = rifugiatoDAO.findByEmail(emailRifugiato);
        if (r == null) {
            throw new IllegalArgumentException("Rifugiato non trovato");
        }

        Consulenza c = consulenzaDAO.findConsulenzaById(idConsulenza);
        if (c == null) {
            throw new IllegalArgumentException("Alloggio non trovato");
        }

        // Verifica se l'email del rifugiato è già nella lista candidati
        if (c.getCandidati().containsKey(r.getEmail())) {
            throw new IllegalArgumentException("Interesse già manifestato");
        }

        c.getCandidati().put(r.getEmail(), true);
        //aggiorno la consulenza
        consulenzaDAO.save(c);
    }

    /**
     *
     */
    @Override
    public List<Lavoro> getRandomLavori() {
        // Recupera tutti gli eventi
        List<Lavoro> lavori = lavoroDAO.findAll();

        // Mischia la lista
        Collections.shuffle(lavori);

        // Restituisce solo 5 lavori
        return lavori.stream().limit(5).toList();
    }

    @Override
    public List<String> getCandidatiPerLavoro(final long lavoroId) {
        // Recupera l'evento specificato tramite ID
        Lavoro lavoro = lavoroDAO.findById(lavoroId)
                .orElseThrow(() -> new IllegalArgumentException("Lavoro non trovato"));

        // Restituisce la lista dei partecipanti (Rifugiato) associati all'evento
        return lavoro.getCandidati();
    }

}
