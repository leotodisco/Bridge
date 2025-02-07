package com.project.bridgebackend.ModuloFiaUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.project.bridgebackend.Model.Entity.*;
import com.project.bridgebackend.Model.Entity.enumeration.*;
import com.project.bridgebackend.Model.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVImportService {

    @Autowired
    private LavoroDAO lavoroDAO; // DAO per Lavoro
    @Autowired
    private VolontarioDAO volontarioDAO; // DAO per Volontario
    @Autowired
    private IndirizzoDAO indirizzoDAO; // DAO per Indirizzo

    @Autowired
    private RifugiatoDAO rifugiatoDAO;

    @Autowired
    private FiguraSpecializzataDAO figuraSpecializzataDAO;


    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void importCSV(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            CsvToBean<UserCSV> csvToBean = new CsvToBeanBuilder<UserCSV>(reader)
                    .withType(UserCSV.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<UserCSV> users = csvToBean.parse();

            for (UserCSV user : users) {
                saveUser(user);
            }

            System.out.println("Importazione completata con successo!");

        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'importazione del CSV: " + e.getMessage(), e);
        }
    }

    private void saveUser(UserCSV user) {
        try {
            // Deserializzazione degli embeddings se presenti
            String embeddingsJson = user.getEmbeddingsJson();
            float[] embeddings = deserializeEmbeddings(embeddingsJson);

            Rifugiato rifugiato = new Rifugiato(
                    user.getEmail(),
                    user.getNome(),
                    user.getCognome(),
                    user.getLingueParlate(),
                    null, // Qui puoi generare un ID unico o usare un altro valore
                    user.getSkill(),
                    LocalDate.parse(user.getDataNascita()),
                    TitoloDiStudio.valueOf(user.getTitoloDiStudio()),
                    Ruolo.Rifugiato,
                    Gender.valueOf(user.getGenere()),
                    user.getNazionalita(),
                    passwordEncoder.encode("Password123!") // Password di default, meglio cambiarla
            );

            // Aggiungi gli embeddings nel rifugiato se necessario
            rifugiato.setEmbeddings(embeddings);

            rifugiatoDAO.save(rifugiato);
            //System.out.println("Utente salvato: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("Errore nel salvataggio dell'utente " + user.getEmail() + ": " + e.getMessage());
        }
    }

    private float[] deserializeEmbeddings(String embeddingsString) {
        try {
            if (embeddingsString == null || embeddingsString.isEmpty()) {
                return new float[0]; // Array vuoto se non ci sono embeddings
            }

            embeddingsString = embeddingsString.replace("'", "\"");

            // Log per verificare il formato
            System.out.println("🔍 Embeddings raw: " + embeddingsString);

            // Deserializza la stringa JSON in un oggetto JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(embeddingsString);

            // Creiamo una lista per accumulare tutti i valori degli embeddings
            List<Float> embeddingsList = new ArrayList<>();

            // Iteriamo sulle chiavi del JSON e deserializziamo ciascun array di float
            jsonNode.fields().forEachRemaining(entry -> {
                String key = entry.getKey();
                JsonNode valueNode = entry.getValue();

                // Verifica se il valore è una stringa (che rappresenta un array)
                if (valueNode.isTextual()) {
                    // Deserializza la stringa come un array
                    try {
                        String arrayString = valueNode.asText();
                        arrayString = arrayString.replace("[", "").replace("]",""); // Rimuovi le parentesi per creare una lista separata da virgola
                                String[] strValues = arrayString.split(",");
                        for (String strValue : strValues) {
                            embeddingsList.add(Float.parseFloat(strValue.trim()));
                        }
                    } catch (Exception e) {
                        System.err.println("Errore nella deserializzazione della stringa in float array: " + e.getMessage());
                    }
                }
            });

            // Converti la lista di float in un array di float
            float[] embeddingsArray = new float[embeddingsList.size()];
            for (int i = 0; i < embeddingsList.size(); i++) {
                embeddingsArray[i] = embeddingsList.get(i);
            }

            return embeddingsArray;

        } catch (JsonProcessingException e) {
            System.out.println("❌ Error during deserialization: " + e.getMessage());
            throw new RuntimeException("Errore nella deserializzazione degli embeddings", e);
        }
    }


    public void importCSVIndirizzi(String filePath) {

        try (Reader reader = new FileReader(filePath)) {

            CsvToBean<IndirizzoCSV> csvToBean = new CsvToBeanBuilder<IndirizzoCSV>(reader)
                    .withType(IndirizzoCSV.class)
                    .withIgnoreLeadingWhiteSpace(true) // Ignora spazi all'inizio delle celle
                    .withIgnoreQuotations(true) // Ignora eventuali doppi apici
                    .withSeparator(';') // Assicurati che il separatore sia corretto
                    .build();



            List<IndirizzoCSV> indirizzi = csvToBean.parse();

            for (IndirizzoCSV indirizzoCSV : indirizzi) {
                System.out.println("DEBUG Lettura CSV -> Citta: [" + indirizzoCSV.getCitta() +
                        "], Provincia: [" + indirizzoCSV.getProvincia() +
                        "], Via: [" + indirizzoCSV.getVia() +
                        "], CAP: [" + indirizzoCSV.getCap() +
                        "], NumeroCivico: [" + indirizzoCSV.getNumCivico() +
                        "], Latitudine: [" + indirizzoCSV.getLatitudine() +
                        "], Longitudine: [" + indirizzoCSV.getLongitudine() + "]");
                saveIndirizzo(indirizzoCSV);
            }


            System.out.println("Importazione indirizzi completata con successo!");

        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'importazione del CSV degli indirizzi: " + e.getMessage(), e);
        }
    }

    private void saveIndirizzo(IndirizzoCSV indirizzoCSV) {
        try {
            // Rimuove spazi extra e assicura che i campi non siano nulli
            String citta = indirizzoCSV.getCitta() != null ? indirizzoCSV.getCitta().trim() : "";
            String provincia = indirizzoCSV.getProvincia() != null ? indirizzoCSV.getProvincia().trim() : "";
            String cap = indirizzoCSV.getCap() != null ? indirizzoCSV.getCap().trim() : "";

            // Controllo che i dati siano validi
            if (citta.isEmpty() || provincia.isEmpty()) {
                throw new IllegalArgumentException("Città o provincia non possono essere vuote!");
            }

            Indirizzo indirizzo = new Indirizzo(
                    citta,
                    indirizzoCSV.getNumCivico(),
                    cap,
                    provincia,
                    indirizzoCSV.getVia(),
                    indirizzoCSV.getLatitudine(),
                    indirizzoCSV.getLongitudine()
            );

            indirizzoDAO.save(indirizzo);
            System.out.println("Indirizzo salvato: " + indirizzo.getCitta() + ", " + indirizzo.getProvincia());
        } catch (Exception e) {
            System.err.println("Errore nel salvataggio dell'indirizzo: " + e.getMessage());
        }
    }

    public void importCSVLavori(String filePath) {
        printCSVHeaders("bridge-backend/src/main/java/com/project/bridgebackend/ModuloFiaUtil/annunci_lav.csv");
        try (Reader reader = new FileReader(filePath)) {

            CsvToBean<AnnunciLavoroCSV> csvToBean = new CsvToBeanBuilder<AnnunciLavoroCSV>(reader)
                    .withType(AnnunciLavoroCSV.class)
                    .withIgnoreLeadingWhiteSpace(true) // Ignora spazi all'inizio delle celle
                    .withIgnoreQuotations(true) // Ignora eventuali doppi apici
                    .withSeparator(';') // Assicurati che il separatore sia corretto
                    .build();

            List<AnnunciLavoroCSV> lavori = csvToBean.parse();

            for (AnnunciLavoroCSV lavoroCSV : lavori) {
                System.out.println("DEBUG Lettura CSV -> Titolo: [" + lavoroCSV.getTitolo() +
                        "], Disponibilità: [" + lavoroCSV.getDisponibilita() +
                        "], Indirizzo: [" + lavoroCSV.getIndirizzo() +
                        "], Numero Candidature: [" + lavoroCSV.getNumeroCandidature() +
                        "], Tipologia: [" + lavoroCSV.getTipologia() +
                        "], Posizione: [" + lavoroCSV.getPosizioneLavorativa() +
                        "], Azienda: [" + lavoroCSV.getNomeAzienda() +
                        "], Orario: [" + lavoroCSV.getOrarioLavoro() +
                        "], Retribuzione: [" + lavoroCSV.getRetribuzione() +
                        "], Sede: [" + lavoroCSV.getNomeSede() +
                        "], Info Utili: [" + lavoroCSV.getInfoUtili() +
                        "], Email Proprietario: [" + lavoroCSV.getProprietarioEmail() + "]");

                saveLavoro(lavoroCSV);
            }

            System.out.println("Importazione lavori completata con successo!");

        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'importazione del CSV dei lavori: " + e.getMessage(), e);
        }
    }

    private void saveLavoro(AnnunciLavoroCSV lavoroCSV) {
        try {
            Volontario proprietario = volontarioDAO.findByEmail(lavoroCSV.getProprietarioEmail());

            if (proprietario == null) {
                throw new IllegalArgumentException("Proprietario con email " + lavoroCSV.getProprietarioEmail() + " non trovato.");
            }

            // Rimozione caratteri non validi da nomeSede e posizioneLavorativa
            String nomeSede = lavoroCSV.getNomeSede().replaceAll("[^A-Za-zÀ-ÿ0-9'\\- ]", "").trim();
            String posizioneLavorativa = lavoroCSV.getPosizioneLavorativa().replaceAll("[^A-Za-zÀ-ÿ0-9'\\- ]", "").trim();
            String titolo = lavoroCSV.getTitolo().replaceAll("[^A-Za-zÀ-ÿ0-9'\\- ]", "").trim();

            // Validazione per orario di lavoro (rimuove eventuali caratteri speciali non accettati)
            String orarioLavoro = lavoroCSV.getOrarioLavoro().replaceAll("[^0-9:-]", "").trim();

            // Validazione per info utili (limitando la lunghezza e rimuovendo caratteri speciali non accettati)
            String infoUtili = lavoroCSV.getInfoUtili().replaceAll("[^\\wÀ-ÿ\\s,.!?@'\\-]", "").trim();
            if (infoUtili.length() > 500) {
                infoUtili = infoUtili.substring(0, 500);  // Tronca a 500 caratteri
            }

            // Assicurati che l'indirizzo esista
            Indirizzo indirizzo = indirizzoDAO.findById(Long.parseLong(lavoroCSV.getIndirizzo())).orElse(null);
            if (indirizzo == null) {
                throw new IllegalArgumentException("Indirizzo con ID " + lavoroCSV.getIndirizzo() + " non trovato.");
            }

            // Creazione dell'oggetto Lavoro
            Lavoro lavoro = new Lavoro();
            lavoro.setTitolo(titolo);
            lavoro.setDisponibilita(lavoroCSV.getDisponibilita());
            lavoro.setIndirizzo(indirizzo);
            lavoro.setMaxCandidature(lavoroCSV.getNumeroCandidature());
            lavoro.setCandidati(new ArrayList<>());
            lavoro.setTipologia(lavoroCSV.getTipologia());
            lavoro.setPosizioneLavorativa(posizioneLavorativa);
            lavoro.setNomeAzienda(lavoroCSV.getNomeAzienda());
            lavoro.setOrarioLavoro(orarioLavoro);
            lavoro.setTipoContratto(TipoContratto.valueOf(lavoroCSV.getTipoContratto()));
            lavoro.setRetribuzione(lavoroCSV.getRetribuzione());
            lavoro.setNomeSede(nomeSede);
            lavoro.setInfoUtili(infoUtili);
            lavoro.setProprietario(proprietario);


            lavoroDAO.save(lavoro);
            System.out.println("Lavoro salvato: " + lavoro.getTitolo());
        } catch (Exception e) {
            System.err.println("Errore nel salvataggio dell'annuncio di lavoro: " + e.getMessage());
        }
    }


    public void printCSVHeaders(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] headers = reader.readNext(); // Legge la prima riga (gli header)

            if (headers != null) {
                System.out.println("Headers del CSV:");
                for (String header : headers) {
                    System.out.print(header + " | ");
                }
                System.out.println(); // Vai a capo dopo aver stampato gli header
            } else {
                System.out.println("Il file CSV è vuoto o non ha header.");
            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del CSV: " + e.getMessage());
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }


}
