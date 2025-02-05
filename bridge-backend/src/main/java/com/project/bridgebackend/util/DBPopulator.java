package com.project.bridgebackend.util;

import com.project.bridgebackend.Model.Entity.*;
import com.project.bridgebackend.Model.Entity.enumeration.*;
import com.project.bridgebackend.Model.dao.*;
import com.project.bridgebackend.ModuloFiaUtil.CSVImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Biagio Gallo.
 * Creato il: 29/12/2024.
 * Questa classe è responsabile della popolazione
 * del database con dati di esempio.
 */
@Component
@Scope("singleton")
public class DBPopulator {

    @Autowired
    private CSVImportService csvImportService;

    @Autowired
    private RifugiatoDAO rifugiatoDAO;

    @Autowired
    private FiguraSpecializzataDAO figuraSpecializzataDAO;

    @Autowired
    private VolontarioDAO volontarioDAO;

    @Autowired
    private EventoDAO eventoDAO;

    @Autowired
    private ConsulenzaDAO consulenzaDAO;

    @Autowired
    private AlloggioDAO alloggioDAO;

    @Autowired
    private CorsoDAO corsoDAO;

    @Autowired
    private IndirizzoDAO indirizzoDAO;

    @Autowired
    private LavoroDAO lavoroDAO;

    @EventListener(ApplicationReadyEvent.class)
    public void populateDB() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Creazione di tre rifugiati
        Rifugiato rifugiato1 = new Rifugiato(
                "rifugiato1@example.com",
                "Ali",
                "Khan",
                "Italiano, Inglese",
                "6772deb930698b3a76d304a1",
                "Cucina, Carpenteria",
                LocalDate.of(1995, 3, 15),
                TitoloDiStudio.ScuolaPrimaria,
                Ruolo.Rifugiato,
                Gender.Maschio,
                "Pakistan",
                passwordEncoder.encode("Cazzarola69!")
        );

        Rifugiato rifugiato2 = new Rifugiato(
                "rifugiato2@example.com",
                "Maria",
                "Gonzalez",
                "Spagnolo, Inglese",
                "6772dd6130698b3a76d304a0",
                "Educazione, Cucito",
                LocalDate.of(1987, 7, 22),
                TitoloDiStudio.Laurea,
                Ruolo.Rifugiato,
                Gender.Femmina,
                "Messico",
                passwordEncoder.encode("Passroad69!")
        );

        Rifugiato rifugiato3 = new Rifugiato(
                "rifugiato3@example.com",
                "Ahmed",
                "Said",
                "Arabo, Italiano",
                "6772df3730698b3a76d304a2",
                "Manutenzione, Giardinaggio",
                LocalDate.of(1990, 12, 10),
                TitoloDiStudio.ScuolaPrimaria,
                Ruolo.Rifugiato,
                Gender.Maschio,
                "Siria",
                passwordEncoder.encode("Passroad69!")
        );

        // Creazione di tre figure specializzate
        FiguraSpecializzata figura1 = new FiguraSpecializzata(
                "figura1@example.com",
                "Laura",
                "Pausini",
                "Italiano, Inglese",
                "6772dfdc30698b3a76d304a3",
                "Medicina",
                LocalDate.of(1980, 5, 20),
                TitoloDiStudio.Specializzazione,
                Ruolo.FiguraSpecializzata,
                Gender.Femmina,
                "Italia",
                passwordEncoder.encode("Passroad69!"),
                "09:00-17:00"
        );

        FiguraSpecializzata figura2 = new FiguraSpecializzata(
                "figura2@example.com",
                "John",
                "Doe",
                "Inglese, Francese",
                "6772e0ad30698b3a76d304a4",
                "Informatica",
                LocalDate.of(1985, 8, 15),
                TitoloDiStudio.Laurea,
                Ruolo.FiguraSpecializzata,
                Gender.Maschio,
                "Francia",
                passwordEncoder.encode("Passroad69!"),
                "10:00-18:00"
        );

        FiguraSpecializzata figura3 = new FiguraSpecializzata(
                "figura3@example.com",
                "Olimpia",
                "Peroni",
                "Inglese, Spagnolo",
                "6772e22730698b3a76d304a5",
                "Insegnamento",
                LocalDate.of(1990, 11, 30),
                TitoloDiStudio.Laurea,
                Ruolo.FiguraSpecializzata,
                Gender.Femmina,
                "Germania",
                passwordEncoder.encode("Passroad69!"),
                "08:00-16:00"
        );

        // Creazione di tre volontari
        Volontario volontario1 = new Volontario(
                "volontario1@example.com",
                "Valentino",
                "Rossi",
                "Italiano, Inglese",
                "6772e2de30698b3a76d304a6",
                "Organizzazione eventi",
                LocalDate.of(1995, 3, 15),
                TitoloDiStudio.Laurea,
                Ruolo.Volontario,
                Gender.Maschio,
                "Italia",
                passwordEncoder.encode("Passroad69!")
        );

        Volontario volontario2 = new Volontario(
                "volontario2@example.com",
                "Mara",
                "Venier",
                "Italiano, Francese",
                "6772e45a30698b3a76d304a8",
                "Assistenza sociale",
                LocalDate.of(1987, 7, 22),
                TitoloDiStudio.Laurea,
                Ruolo.Volontario,
                Gender.Femmina,
                "Italia",
                passwordEncoder.encode("Passroad69!")
        );

        Volontario volontario3 = new Volontario(
                "volontario3@example.com",
                "Edoardo",
                "D'Erme",
                "Italiano, Spagnolo",
                "6772e37230698b3a76d304a7",
                "Supporto tecnico",
                LocalDate.of(1990, 12, 10),
                TitoloDiStudio.ScuolaSecondariaPrimoGrado,
                Ruolo.Volontario,
                Gender.Maschio,
                "Italia",
                passwordEncoder.encode("Passroad69!")
        );

        // Creazione di quattro indirizzi
        Indirizzo indirizzo1 = new Indirizzo("Lazio", 28, "00100", "RM", "Via del Corso");
        Indirizzo indirizzo2 = new Indirizzo("Lombardia", 10, "20100", "MI", "Via Milano");
        Indirizzo indirizzo3 = new Indirizzo("Piemonte", 15, "10100", "TO", "Via Torino");
        Indirizzo indirizzo4 = new Indirizzo("Campania", 20, "80100", "NA", "Via Napoli");
        Indirizzo indirizzo5= new Indirizzo("Lombardia", 69, "20190", "PE", "Via cazzo");
        Indirizzo indirizzo6 = new Indirizzo("Lombardia", 12, "20121", "MI", "Via Roma");
        Indirizzo indirizzo7 = new Indirizzo("Piemonte", 5, "10122", "TO", "Via Garibaldi");
        Indirizzo indirizzo8 = new Indirizzo("Toscana", 20, "50123", "FI", "Via Dante");
        indirizzoDAO.saveAll(List.of(indirizzo1, indirizzo2, indirizzo3, indirizzo4, indirizzo5, indirizzo6, indirizzo7, indirizzo8));


        Evento evento1 = new Evento();
        evento1.setNome("Evento di Benvenuto");
        evento1.setData(LocalDate.of(2025, 12, 29));
        evento1.setOra(LocalTime.of(10, 0));
        evento1.setLinguaParlata(Lingua.ITALIANO);
        evento1.setDescrizione("Evento di benvenuto per i nuovi rifugiati.");
        evento1.setLuogo(indirizzo1);
        evento1.setOrganizzatore(volontario1);
        evento1.setMaxPartecipanti(50);

        Evento evento2 = new Evento();
        evento2.setNome("Workshop di Informatica");
        evento2.setData(LocalDate.of(2025, 12, 30));
        evento2.setOra(LocalTime.of(14, 0));
        evento2.setLinguaParlata(Lingua.INGLESE);
        evento2.setDescrizione("Workshop di informatica per rifugiati.");
        evento2.setLuogo(indirizzo2);
        evento2.setOrganizzatore(volontario2);
        evento2.setMaxPartecipanti(30);

        Evento evento3 = new Evento();
        evento3.setNome("Corso di Cucina");
        evento3.setData(LocalDate.of(2025, 12, 31));
        evento3.setOra(LocalTime.of(16, 0));
        evento3.setLinguaParlata(Lingua.ITALIANO);
        evento3.setDescrizione("Corso di cucina italiana per rifugiati.");
        evento3.setLuogo(indirizzo3);
        evento3.setOrganizzatore(volontario3);
        evento3.setMaxPartecipanti(20);

        Evento evento4 = new Evento();
        evento4.setNome("Evento di Networking");
        evento4.setData(LocalDate.of(2025, 12, 10));
        evento4.setOra(LocalTime.of(18, 0));
        evento4.setLinguaParlata(Lingua.INGLESE);
        evento4.setDescrizione("Evento di networking per rifugiati e aziende.");
        evento4.setLuogo(indirizzo4);
        evento4.setOrganizzatore(volontario3);
        evento4.setMaxPartecipanti(40);

        // Creazione di quattro alloggi
        Alloggio alloggio1 = new Alloggio();
        alloggio1.setMetratura(100);
        alloggio1.setMaxPersone(4);
        alloggio1.setDescrizione("Un bellissimo appartamento nel centro di Roma.");
        alloggio1.setProprietario(volontario1);
        alloggio1.setServizi(Servizi.WIFI);
        alloggio1.setFoto(List.of("6772f05d67ee1d482dd9ef22", "6772f05d67ee1d482dd9ef23"));
        alloggio1.setTitolo("Appartamento a Roma");
        alloggio1.setIndirizzo(indirizzo1);

        Alloggio alloggio2 = new Alloggio();
        alloggio2.setMetratura(80);
        alloggio2.setMaxPersone(3);
        alloggio2.setDescrizione("Un accogliente appartamento a Milano.");
        alloggio2.setProprietario(volontario2);
        alloggio2.setServizi(Servizi.RISCALDAMENTO);
        alloggio2.setFoto(List.of("6772f22f67ee1d482dd9ef24", "6772f22f67ee1d482dd9ef25"));
        alloggio2.setTitolo("Appartamento a Milano");
        alloggio2.setIndirizzo(indirizzo2);

        Alloggio alloggio3 = new Alloggio();
        alloggio3.setMetratura(120);
        alloggio3.setMaxPersone(5);
        alloggio3.setDescrizione("Un ampio appartamento a Torino.");
        alloggio3.setProprietario(volontario3);
        alloggio3.setServizi(Servizi.ARIACONDIZIONATA);
        alloggio3.setFoto(List.of("6772f2ac67ee1d482dd9ef26", "6772f2ac67ee1d482dd9ef27"));
        alloggio3.setTitolo("Appartamento a Torino");
        alloggio3.setIndirizzo(indirizzo3);

        Alloggio alloggio4 = new Alloggio();
        alloggio4.setMetratura(90);
        alloggio4.setMaxPersone(4);
        alloggio4.setDescrizione("Un moderno appartamento a Napoli.");
        alloggio4.setProprietario(volontario1);
        alloggio4.setServizi(Servizi.WIFI);
        alloggio4.setFoto(List.of("6772f5d967ee1d482dd9ef28", "6772f5d967ee1d482dd9ef29"));
        alloggio4.setTitolo("Appartamento a Napoli");
        alloggio4.setIndirizzo(indirizzo4);

        // Creazione di quattro corsi
        Corso corso1 = new Corso();
        corso1.setCategoriaCorso(CategoriaCorso.LINGUE);
        corso1.setDescrizione("Corso di Italiano per principianti.");
        corso1.setLingua(Lingua.ITALIANO);
        corso1.setPdf("6772e95230698b3a76d304af");
        corso1.setTitolo("Italiano per Principianti");
        corso1.setProprietario(figura1);

        Corso corso2 = new Corso();
        corso2.setCategoriaCorso(CategoriaCorso.INFORMATICA);
        corso2.setDescrizione("Corso di Programmazione Java.");
        corso2.setLingua(Lingua.INGLESE);
        corso2.setPdf("6772e9be30698b3a76d304b1");
        corso2.setTitolo("Programmazione Java");
        corso2.setProprietario(figura2);

        Corso corso3 = new Corso();
        corso3.setCategoriaCorso(CategoriaCorso.LINGUE);
        corso3.setDescrizione("Corso di Spagnolo avanzato.");
        corso3.setLingua(Lingua.SPAGNOLO);
        corso3.setPdf("6772e8dc30698b3a76d304ae");
        corso3.setTitolo("Spagnolo Avanzato");
        corso3.setProprietario(figura3);

        Corso corso4 = new Corso();
        corso4.setCategoriaCorso(CategoriaCorso.ALTRO);
        corso4.setDescrizione("Corso di Inglese avanzato.");
        corso4.setLingua(Lingua.ITALIANO);
        corso4.setPdf("6772e98830698b3a76d304b0");
        corso4.setTitolo("Inglese Avanzato");
        corso4.setProprietario(figura1);

        // Creazione di una consulenza
        // Creazione di quattro consulenze
        Consulenza consulenza1 = new Consulenza();
        consulenza1.setTipo(TipoConsulenza.LEGALE); // Tipo di consulenza
        consulenza1.setOrariDisponibili("Lunedi 12:43-23:42"); // Orari di disponibilità
        consulenza1.setDescrizione("Consulenza legale per rifugiati"); // Descrizione
        consulenza1.setNumero("3331234567");// Numero di telefono
        Map<String, Boolean> candidatiMock1 = new HashMap<>();
        candidatiMock1.put(rifugiato1.getEmail(), true);  // Accettato
        candidatiMock1.put(rifugiato2.getEmail(), false); // Non accettato
        consulenza1.setCandidati(candidatiMock1); // Candidati (inizialmente null)
        consulenza1.setTitolo("Consulenza per la Gestione di Progetti Sociali" );// Titolo
        consulenza1.setDisponibilita(true); // Disponibilità
        consulenza1.setIndirizzo(indirizzo1); // Indirizzo
        consulenza1.setMaxCandidature(5);// Max candidature
        consulenza1.setProprietario(figura1);// Proprietario
        consulenza1.setTipologia(true); // Tipologia

        Consulenza consulenza2 = new Consulenza();
        consulenza2.setTipo(TipoConsulenza.SANITARIA); // Tipo di consulenza
        consulenza2.setOrariDisponibili("Martedi 09:00-17:00"); // Orari di disponibilità
        consulenza2.setDescrizione("Consulenza psicologica per rifugiati"); // Descrizione
        consulenza2.setNumero("3337654321");// Numero di telefono
        Map<String, Boolean> candidatiMock2 = new HashMap<>();
        candidatiMock2.put(rifugiato1.getEmail(), false);  // Accettato
        candidatiMock2.put(rifugiato3.getEmail(), false); // Non accettato
        consulenza2.setCandidati(candidatiMock2); // Candidati (inizialmente null)
        consulenza2.setTitolo("Consulenza Psicologica per il Benessere Mentale" );// Titolo
        consulenza2.setDisponibilita(true); // Disponibilità
        consulenza2.setIndirizzo(indirizzo2); // Indirizzo
        consulenza2.setMaxCandidature(3);// Max candidature
        consulenza2.setProprietario(figura2);// Proprietario
        consulenza2.setTipologia(true); // Tipologia

        Consulenza consulenza3 = new Consulenza();
        consulenza3.setTipo(TipoConsulenza.COMMERCIALE); // Tipo di consulenza
        consulenza3.setOrariDisponibili("Mercoledi 10:00-18:00"); // Orari di disponibilità
        consulenza3.setDescrizione("Consulenza legale per rifugiati"); // Descrizione
        consulenza3.setNumero("3339876543");// Numero di telefono
        Map<String, Boolean> candidatiMock3 = new HashMap<>();
        candidatiMock3.put(rifugiato1.getEmail(), true);  // Accettato
        candidatiMock3.put(rifugiato2.getEmail(), true); // Non accettato
        consulenza3.setCandidati(candidatiMock3); // Candidati (inizialmente null)
        consulenza3.setTitolo("Consulenza per la Gestione di Pratiche Legali" );// Titolo
        consulenza3.setDisponibilita(true); // Disponibilità
        consulenza3.setIndirizzo(indirizzo3); // Indirizzo
        consulenza3.setMaxCandidature(4);// Max candidature
        consulenza3.setProprietario(figura3);// Proprietario
        consulenza3.setTipologia(true); // Tipologia

        Consulenza consulenza4 = new Consulenza();
        consulenza4.setTipo(TipoConsulenza.PSICOLOGICA); // Tipo di consulenza
        consulenza4.setOrariDisponibili("Giovedi 08:00-16:00"); // Orari di disponibilità
        consulenza4.setDescrizione("Consulenza sociale per rifugiati"); // Descrizione
        consulenza4.setNumero("3334567890");// Numero di telefono
        consulenza4.setCandidati(new HashMap<String, Boolean>()); // Candidati (inizialmente null)
        consulenza4.setTitolo("Consulenza per l'Integrazione Sociale" );// Titolo
        consulenza4.setDisponibilita(true); // Disponibilità
        consulenza4.setIndirizzo(indirizzo4); // Indirizzo
        consulenza4.setMaxCandidature(6);// Max candidature
        consulenza4.setProprietario(figura1);// Proprietario
        consulenza4.setTipologia(true); // Tipologia

//     Creazione di quattro lavori
        Lavoro lavoro1 = new Lavoro();
        lavoro1.setTitolo("Sviluppatore Java");
        lavoro1.setDisponibilita(true);
        lavoro1.setIndirizzo(indirizzo1);
        lavoro1.setMaxCandidature(5);
        lavoro1.setCandidati(new ArrayList<>());
        lavoro1.setTipologia(true);
        lavoro1.setPosizioneLavorativa("Sviluppatore Java");
        lavoro1.setNomeAzienda("Tech Company");
        lavoro1.setOrarioLavoro("09:00-17:00");
        lavoro1.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro1.setRetribuzione(3000.00);
        lavoro1.setNomeSede("Sede Centrale");
        lavoro1.setProprietario(volontario1);
        lavoro1.setInfoUtili("Esperienza di almeno 2 anni in Java");

        Lavoro lavoro2 = new Lavoro();
        lavoro2.setTitolo("Analista Dati");
        lavoro2.setDisponibilita(true);
        lavoro2.setIndirizzo(indirizzo2);
        lavoro2.setMaxCandidature(3);
        lavoro2.setCandidati(new ArrayList<>());
        lavoro2.setTipologia(true);
        lavoro2.setPosizioneLavorativa("Analista Dati");
        lavoro2.setNomeAzienda("Data Corp");
        lavoro2.setOrarioLavoro("10:00-18:00");
        lavoro2.setTipoContratto(TipoContratto.FULL_TIME);
        lavoro2.setRetribuzione(2800.00);
        lavoro2.setNomeSede("Sede Nord");
        lavoro2.setProprietario(volontario2);
        lavoro2.setInfoUtili("Conoscenza avanzata di SQL e Python");

        Lavoro lavoro3 = new Lavoro();
        lavoro3.setTitolo("Progettista Interfacce");
        lavoro3.setDisponibilita(true);
        lavoro3.setIndirizzo(indirizzo3);
        lavoro3.setMaxCandidature(4);
        lavoro3.setCandidati(new ArrayList<>());
        lavoro3.setTipologia(true);
        lavoro3.setPosizioneLavorativa("Progettista Interfacce");
        lavoro3.setNomeAzienda("Design Studio");
        lavoro3.setOrarioLavoro("08:00-16:00");
        lavoro3.setTipoContratto(TipoContratto.FULL_TIME);
        lavoro3.setRetribuzione(3200.00);
        lavoro3.setNomeSede("Sede Sud");
        lavoro3.setProprietario(volontario3);
        lavoro3.setInfoUtili("Esperienza con strumenti di design come Figma e Sketch");

        Lavoro lavoro4 = new Lavoro();
        lavoro4.setTitolo("Responsabile Marketing");
        lavoro4.setDisponibilita(true);
        lavoro4.setIndirizzo(indirizzo4);
        lavoro4.setMaxCandidature(6);
        lavoro4.setCandidati(new ArrayList<>());
        lavoro4.setTipologia(true);
        lavoro4.setPosizioneLavorativa("Responsabile Marketing");
        lavoro4.setNomeAzienda("Marketing Agency");
        lavoro4.setOrarioLavoro("11:00-19:00");
        lavoro4.setTipoContratto(TipoContratto.FULL_TIME);
        lavoro4.setRetribuzione(3500.00);
        lavoro4.setNomeSede("Sede Ovest");
        lavoro4.setProprietario(volontario1);
        lavoro4.setInfoUtili("Esperienza in campagne di marketing digitale");

        // Salvataggio dei rifugiati nel database
        List.of(rifugiato1, rifugiato2, rifugiato3).forEach(rifugiato -> {
            try {
                rifugiatoDAO.save(rifugiato);
            } catch (Exception e) {
                throw new RuntimeException("Errore durante la registrazione del rifugiato: " + rifugiato.getEmail(), e);
            }
        });

        // Salvataggio delle figure specializzate nel database
        List.of(figura1, figura2, figura3).forEach(figura -> {
            try {
                figuraSpecializzataDAO.save(figura);
            } catch (Exception e) {
                throw new RuntimeException("Errore durante la registrazione della figura specializzata: " + figura.getEmail(), e);
            }
        });

        // Salvataggio dei volontari nel database
        List.of(volontario1, volontario2, volontario3).forEach(volontario -> {
            try {
                volontarioDAO.save(volontario);
            } catch (Exception e) {
                throw new RuntimeException("Errore durante la registrazione del volontario: " + volontario.getEmail(), e);
            }
        });

        // Salvataggio degli indirizzi nel database
        List.of(indirizzo1, indirizzo2, indirizzo3, indirizzo4).forEach(indirizzo -> {
            try {
                indirizzoDAO.save(indirizzo);
            } catch (Exception e) {
                throw new RuntimeException("Errore durante la registrazione dell'indirizzo: " + indirizzo.getVia(), e);
            }
        });

        // Salvataggio degli eventi nel database
        List.of(evento1, evento2, evento3, evento4).forEach(evento -> {
            try {
                eventoDAO.save(evento);
            } catch (Exception e) {
                throw new RuntimeException("Errore durante la registrazione dell'evento: " + evento.getNome(), e);
            }
        });

        // Salvataggio degli alloggi nel database
        List.of(alloggio1, alloggio2, alloggio3, alloggio4).forEach(alloggio -> {
            try {
                alloggioDAO.save(alloggio);
            } catch (Exception e) {
                System.out.println("Errore durante la registrazione dell'alloggio: " + alloggio.getTitolo());
            }
        });

        // Salvataggio dei corsi nel database
        List.of(corso1, corso2, corso3, corso4).forEach(corso -> {
            try {
                corsoDAO.save(corso);
            } catch (Exception e) {
                throw new RuntimeException("Errore durante la registrazione del corso: " + corso.getTitolo(), e);
            }
        });

        // Salvataggio delle consulenze nel database
        List.of(consulenza1, consulenza2, consulenza3, consulenza4).forEach(consulenza -> {
            try {
                consulenzaDAO.save(consulenza);
            } catch (Exception e) {
                throw new RuntimeException("Errore durante la registrazione della consulenza: " + consulenza.getTitolo(), e);
            }
        });

        // Salvataggio dei lavori nel database
        List.of(lavoro1, lavoro2, lavoro3, lavoro4).forEach(lavoro -> {
            try {
                lavoroDAO.save(lavoro);
            } catch (Exception e) {
                throw new RuntimeException("Errore durante la registrazione del lavoro: " + lavoro.getPosizioneLavorativa(), e);
            }
        });

        //modulo fia
        //aggiunge 50 rifugiati per aumentare il db
        csvImportService.importCSV("bridge-backend/src/main/java/com/project/bridgebackend/ModuloFia/rifugiati_with_embeddings.csv");

    }
}
