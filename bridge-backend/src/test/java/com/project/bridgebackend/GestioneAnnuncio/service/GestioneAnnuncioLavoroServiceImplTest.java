package com.project.bridgebackend.GestioneAnnuncio.service;


import com.project.bridgebackend.GestioneAnnuncio.Service.GestioneAnnuncioServiceImp;
import com.project.bridgebackend.Model.Entity.*;
import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TipoContratto;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import com.project.bridgebackend.Model.dao.LavoroDAO;
import com.project.bridgebackend.Model.dao.VolontarioDAO;
import com.project.bridgebackend.BridgeBackendApplicationTests;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = BridgeBackendApplicationTests.class)
public class GestioneAnnuncioLavoroServiceImplTest {

    @InjectMocks
    private GestioneAnnuncioServiceImp gestioneAnnuncioServiceImpl;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mock
    private LavoroDAO lavoroDAO;

    @Mock
    private Volontario volontario;

    @Mock
    private VolontarioDAO volontarioDAO;

    @AfterEach
    void tearDown() {
        // Resetta tutti i mock di Mockito per evitare interferenze tra i test
        Mockito.reset(volontarioDAO, lavoroDAO);
    }

    @SneakyThrows
    @BeforeEach
    void setup() {
         volontario = new Volontario(
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

        // Configura il comportamento predefinito del mock
        Mockito.when(volontarioDAO.findByEmail("volontario1@example.com")).thenReturn(volontario);

        // Simula il caso di un'email non trovata
        Mockito.when(volontarioDAO.findByEmail("volontario.inesistente@email.com")).thenReturn(null);

        // Configura il comportamento predefinito del mock per il salvataggio di una consulenza
        Mockito.when(lavoroDAO.save(Mockito.any(Lavoro.class))).thenAnswer(
                invocazione -> {
                    return invocazione.getArgument(0);
                }
        );
    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo via in formato errato.
     */
    @Test
    public void testInserimentoLavoroViaSbagliata(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "V1@"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setTitolo("Cercasi Sviluppatore Software");
            lavoro.setDisponibilita(true);
        lavoro.setIndirizzo(indirizzo);
        lavoro.setMaxCandidature(20);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setProprietario(volontario);
        lavoro.setInfoUtili("info sede centrale di roma");

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo numero civico in formato errato.
     */
    @Test
    public void testInserimentoLavoroNumCivicoSbagliata(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12234,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setDisponibilita(true);
        lavoro.setIndirizzo(indirizzo);
        lavoro.setMaxCandidature(20);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setProprietario(volontario);
        lavoro.setInfoUtili("info sede centrale di roma");

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo numero citta in formato errato.
     */
    @Test
    public void testInserimentoLavoroCittaSbagliata(){
        Indirizzo indirizzo = new Indirizzo(
                "Sal@rn°",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setDisponibilita(true);
        lavoro.setIndirizzo(indirizzo);
        lavoro.setMaxCandidature(20);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setProprietario(volontario);
        lavoro.setInfoUtili("info sede centrale di roma");

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo provincia in formato errato.
     */
    @Test
    public void testInserimentoLavoroProvinciaSbagliata(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "S3",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setDisponibilita(true);
        lavoro.setIndirizzo(indirizzo);
        lavoro.setMaxCandidature(20);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setProprietario(volontario);
        lavoro.setInfoUtili("info sede centrale di roma");

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo cap in formato errato.
     */
    @Test
    public void testInserimentoLavoroCapSbagliata(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "123SN",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);

        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setDisponibilita(true);
        lavoro.setMaxCandidature(20);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setProprietario(volontario);
        lavoro.setInfoUtili("info sede centrale di roma");

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo Posizione Lavorativa vuota.
     */
    @Test
    public void testInserimentoLavoroPosizioneLavorativaVuoto(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);

        //test con posizione lavorativa vuoto
        lavoro.setPosizioneLavorativa("");

        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setDisponibilita(true);
        lavoro.setMaxCandidature(20);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setProprietario(volontario);
        lavoro.setInfoUtili("info sede centrale di roma");

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo Posizione Lavorativa nel formato errato.
     */
    @Test
    public void testInserimentoLavoroPosizioneLavorativaSbagliata(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);

        //test con posizione lavorativa vuoto
        lavoro.setPosizioneLavorativa("Svil*ppator3 Soft4ar3");

        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setDisponibilita(true);
        lavoro.setMaxCandidature(20);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setProprietario(volontario);
        lavoro.setInfoUtili("info sede centrale di roma");

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo Nome Azienda è vuoto.
     */
    @Test
    public void testInserimentoLavoroNomeAziendaVuoto(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        //test con il nome azienda vuoto
        lavoro.setNomeAzienda("");

        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setDisponibilita(true);
        lavoro.setMaxCandidature(20);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setProprietario(volontario);
        lavoro.setInfoUtili("info sede centrale di roma");

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo Nome Azienda nel formato sbagliato.
     */
    @Test
    public void testInserimentoLavoroNomeAziendaSbagliata(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        //test con il nome azienda vuoto
        lavoro.setNomeAzienda("Aziend@ 1nform@t1c@");

        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setDisponibilita(true);
        lavoro.setMaxCandidature(20);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setProprietario(volontario);
        lavoro.setInfoUtili("info sede centrale di roma");

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo Orario Lavoro nel formato sbagliato.
     */
    @Test
    public void testInserimentoLavoroOrarioLavoroSbagliata(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setNomeAzienda("Azienda informatica");
        //orario di lavoro nel formato errato
        lavoro.setOrarioLavoro("09:-- --:00");

        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setDisponibilita(true);
        lavoro.setMaxCandidature(20);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setProprietario(volontario);
        lavoro.setInfoUtili("info sede centrale di roma");

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo Tipo Contratto nullo.
     */
    @Test
    public void testInserimentoLavoroTipoContrattoVuoto(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setNomeAzienda("Azienda informatica");
        lavoro.setOrarioLavoro("09:00-18:00");
        //test Tipo contratto vuoto
        lavoro.setTipoContratto(null);

        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setDisponibilita(true);
        lavoro.setMaxCandidature(20);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setProprietario(volontario);
        lavoro.setInfoUtili("info sede centrale di roma");

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo Retribuzione nel formato sbagliato.
     */
    @Test
    public void testInserimentoLavoroRetribuzioneSbagliati(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setNomeAzienda("Azienda informatica");
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        //retribuzione nel formato errato
        lavoro.setRetribuzione(123456789.00);

        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setDisponibilita(true);
        lavoro.setMaxCandidature(20);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setProprietario(volontario);
        lavoro.setInfoUtili("info sede centrale di roma");

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo Nome Sede vuoto.
     */
    @Test
    public void testInserimentoLavoroNomeSedeVuoto(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setNomeAzienda("Azienda informatica");
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        //test nome sede vuoto
        lavoro.setNomeSede("");

        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setDisponibilita(true);
        lavoro.setMaxCandidature(20);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);
        lavoro.setProprietario(volontario);
        lavoro.setInfoUtili("info sede centrale di roma");

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo Nome Sede non rispetta il formato.
     */
    @Test
    public void testInserimentoLavoroNomeSedeSbagliato(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setNomeAzienda("Azienda informatica");
        lavoro.setOrarioLavoro("09:00-18:00");
            lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        //test nome sede vuoto
        lavoro.setNomeSede("Sede#1");

        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setDisponibilita(true);
        lavoro.setMaxCandidature(20);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);
        lavoro.setProprietario(volontario);
        lavoro.setInfoUtili("info sede centrale di roma");

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo Info Utili vuoto.
     */
    @Test
    public void testInserimentoLavoroInfoUtiliVuoto(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setNomeAzienda("Azienda informatica");
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        //test per info utili vuoto
        lavoro.setInfoUtili("");

        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setDisponibilita(true);
        lavoro.setMaxCandidature(20);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);
        lavoro.setProprietario(volontario);


        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo Info Utili con un formato.
     */
    @Test
    public void testInserimentoLavoroInfoUtiliSbagliato(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setNomeAzienda("Azienda informatica");
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        //test per info utili vuoto
        lavoro.setInfoUtili("info###sede");

        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setDisponibilita(true);
        lavoro.setMaxCandidature(20);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);
        lavoro.setProprietario(volontario);

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo volontario non corrisponde a nessun volontario nel db.
     */
    @Test
    public void testInserimentoLavoroVolontarioInesistente(){
        // Creazione di un volontario con un'email falsa (che non esiste nel database)
        Volontario volontarioInesistente = new Volontario();
        volontarioInesistente.setEmail("volontario.inesistente@email.com");  // Email finta che non esiste nel DB

        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setNomeAzienda("Azienda informatica");
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setInfoUtili("info sede centrale di roma");
        //test volontario inesistente
        lavoro.setProprietario(volontarioInesistente);

        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setDisponibilita(true);
        lavoro.setMaxCandidature(20);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);


        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo max candidature non rispetta il formato.
     */
    @Test
    public void testInserimentoLavoroMaxCandidaturaSbagliato(){
         Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setNomeAzienda("Azienda informatica");
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setInfoUtili("info sede centrale di roma");
        lavoro.setProprietario(volontario);
        //test max candidature formato errato
        lavoro.setMaxCandidature(999);

        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setDisponibilita(true);
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);


        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});

    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo max candidature non rispetta il formato.
     */
    @Test
    public void testInserimentoLavoroDisponibilitaVuoto(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setNomeAzienda("Azienda informatica");
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setInfoUtili("info sede centrale di roma");
        lavoro.setProprietario(volontario);
        lavoro.setMaxCandidature(20);
        //test disponibilità nulla
        lavoro.setDisponibilita(null);

        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});
    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo titolo che non rispetta il formato.
     */
    @Test
    public void testInserimentoLavoroTitoloSbagliato(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setNomeAzienda("Azienda informatica");
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setInfoUtili("info sede centrale di roma");
        lavoro.setProprietario(volontario);
        lavoro.setMaxCandidature(20);
        lavoro.setDisponibilita(true);

        //test formato tiolo errato
        lavoro.setTitolo("88– 78@");
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});
    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con il campo Tipologia non può essere nullo.
     */
    @Test
    public void testInserimentoLavoroTipologiaVuota(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setNomeAzienda("Azienda informatica");
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setInfoUtili("info sede centrale di roma");
        lavoro.setProprietario(volontario);
        lavoro.setMaxCandidature(20);
        lavoro.setDisponibilita(true);
        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setCandidati(new ArrayList<>());
        //test tipologia nulla
        lavoro.setTipologia(null);

        assertThrows(IllegalArgumentException.class, () -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});
    }

    /**
     * Test per l'inserimento di un nuovo annuncio,
     * di lavoro,
     * con tutti i campi corretti.
     */
    @Test
    public void testInserimentoLavoroCorretto(){
        Indirizzo indirizzo = new Indirizzo(
                "Salerno",
                12,
                "12345",
                "SA",
                "Via Garibaldi"
        );

        Lavoro lavoro = new Lavoro();
        lavoro.setIndirizzo(indirizzo);
        lavoro.setPosizioneLavorativa("Sviluppatore Software");
        lavoro.setNomeAzienda("Azienda informatica");
        lavoro.setOrarioLavoro("09:00-18:00");
        lavoro.setTipoContratto(TipoContratto.APPRENDISTATO);
        lavoro.setRetribuzione(3000.00);
        lavoro.setNomeSede("Sede centrale Roma");
        lavoro.setInfoUtili("info sede centrale di roma");
        lavoro.setProprietario(volontario);
        lavoro.setMaxCandidature(20);
        lavoro.setDisponibilita(true);
        lavoro.setTitolo("Cercasi Sviluppatore Software");
        lavoro.setCandidati(new ArrayList<>());
        lavoro.setTipologia(true);

        assertDoesNotThrow(() -> {this.gestioneAnnuncioServiceImpl.inserimentoLavoro(lavoro);});
    }



}
