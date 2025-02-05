package com.project.bridgebackend.ModuloFiaUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.project.bridgebackend.Model.Entity.*;
import com.project.bridgebackend.Model.Entity.enumeration.*;
import com.project.bridgebackend.Model.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.FileReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVImportService {

    @Autowired
    private RifugiatoDAO rifugiatoDAO;

    @Autowired
    private FiguraSpecializzataDAO figuraSpecializzataDAO;

    @Autowired
    private VolontarioDAO volontarioDAO;

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


}
