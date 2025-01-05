package com.project.bridgebackend.GestioneAnnuncio.service;

import com.project.bridgebackend.Model.Entity.Consulenza;
import com.project.bridgebackend.GestioneAnnuncio.Service.GestioneAnnuncioServiceImp;
import com.project.bridgebackend.Model.Entity.FiguraSpecializzata;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.TipoConsulenza;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import com.project.bridgebackend.Model.dao.ConsulenzaDAO;
import com.project.bridgebackend.Model.dao.FiguraSpecializzataDAO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = BridgeBackendApplicationTests.class)
public class GestioneAnnuncioServiceImplTest {

    @InjectMocks
    private GestioneAnnuncioServiceImp gestioneAnnuncioServiceImpl;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mock
    private ConsulenzaDAO consulenzaDAO;

    @Mock
    private FiguraSpecializzata figuraSpecializzata;

    @Mock
    private FiguraSpecializzataDAO figuraSpecializzataDAO;

    @AfterEach
    void tearDown() {
        // Resetta tutti i mock di Mockito per evitare interferenze tra i test
        Mockito.reset(figuraSpecializzataDAO, consulenzaDAO);
    }

    /**
     * Metodo che viene eseguito prima di tutti gli altri metodi.
     * Si usa per istanziare le risorse necessarie.
     */
    @SneakyThrows
    @BeforeEach
    void setup() {
        figuraSpecializzata = new FiguraSpecializzata(
                "specializzato2@example.com",
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

        // Configura il comportamento predefinito del mock
        Mockito.when(figuraSpecializzataDAO.findByEmail("specializzato2@example.com")).thenReturn(figuraSpecializzata);

        // Simula il caso di un'email non trovata
        Mockito.when(figuraSpecializzataDAO.findByEmail("email.errata@gmail.com")).thenReturn(null);

        // Configura il comportamento predefinito del mock per il salvataggio di una consulenza
        Mockito.when(consulenzaDAO.save(Mockito.any(Consulenza.class))).thenAnswer(
                invocazione -> {
                    return invocazione.getArgument(0);
                }
        );
    }

    /**
     * Test per l'inserimento di una consulenza
     * con il campo tipologia vuoto.
     */
    @Test
    public void testInserimentoConsulenzaTipologia_Vuoto() {
        Indirizzo indirizzo = new Indirizzo(
                "Roma",
                12,
                "11118",
                "RM",
                "Via Roma"
        );

        indirizzo.setId(1L);

        Consulenza consulenza = new Consulenza();

        consulenza.setOrariDisponibili("Lunedi 12:43 - 23:42");
        consulenza.setDescrizione("Consulenza per documenti, permessi " +
                "di soggiorno, pratiche fiscali e gestione amministrativa.");

        consulenza.setNumero("3202347075");
        consulenza.setIndirizzo(indirizzo);
        consulenza.setProprietario(figuraSpecializzata);
        consulenza.setMaxCandidature(4);
        consulenza.setDisponibilita(true);
        consulenza.setTitolo("Consulenza legale per rifugiati");
        consulenza.setTipologia(true);
        consulenza.setId(1L);
        assertThrows(IllegalArgumentException.class, () -> {
            consulenza.setTipo(TipoConsulenza.valueOf(" "));
        });
    }

    /**
     * Test per l'inserimento di una consulenza
     * con il campo orariDisponibili vuoto.
     */
    @Test
    public void testInserimentoConsulenzaOrariDisponibilita_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Roma",
                12,
                "11118",
                "RM",
                "Via Roma"
        );

        indirizzo.setId(1L);

        Consulenza consulenza = new Consulenza();
        consulenza.setTipo(TipoConsulenza.LEGALE);
        consulenza.setOrariDisponibili("11:00");
        consulenza.setDescrizione("Consulenza per documenti, permessi " +
                "di soggiorno, pratiche fiscali e gestione amministrativa.");

        consulenza.setNumero("3202347075");
        consulenza.setIndirizzo(indirizzo);
        consulenza.setProprietario(figuraSpecializzata);
        consulenza.setMaxCandidature(4);
        consulenza.setDisponibilita(true);
        consulenza.setTitolo("Consulenza legale per rifugiati");
        consulenza.setTipologia(true);
        consulenza.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            gestioneAnnuncioServiceImpl.inserimentoConsulenza(consulenza); // Controllo eseguito qui
        });
    }

    /**
     * Test per l'inserimento di una consulenza
     * con il campo descrizione vuoto.
     */
    @Test
    public void testInserimentoConsulenzaDescrizione_Vuoto() {
        Indirizzo indirizzo = new Indirizzo(
                "Roma",
                12,
                "11118",
                "RM",
                "Via Roma"
        );

        indirizzo.setId(1L);

        Consulenza consulenza = new Consulenza();
        consulenza.setTipo(TipoConsulenza.LEGALE);
        consulenza.setOrariDisponibili("Lunedi 12:43 - 23:42");
        consulenza.setDescrizione("");
        consulenza.setNumero("3202347075");
        consulenza.setIndirizzo(indirizzo);
        consulenza.setProprietario(figuraSpecializzata);
        consulenza.setMaxCandidature(4);
        consulenza.setDisponibilita(true);
        consulenza.setTitolo("Consulenza legale per rifugiati");
        consulenza.setTipologia(true);
        consulenza.setId(1L);

        assertThrows(Exception.class, () -> {
            this.gestioneAnnuncioServiceImpl.inserimentoConsulenza(consulenza);
        });
    }

    /**
     * Test per l'inserimento di una consulenza
     * con il campo numero di telefono vuoto.
     */
    @Test
    public void testInserimentoConsulenzaNumeroTelefono_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Roma",
                12,
                "11118",
                "RM",
                "Via Roma"
        );

        indirizzo.setId(1L);

        Consulenza consulenza = new Consulenza();
        consulenza.setTipo(TipoConsulenza.LEGALE);
        consulenza.setOrariDisponibili("Lunedi 12:30 - 14:30");
        consulenza.setDescrizione("Consulenza per documenti, permessi " +
                "di soggiorno, pratiche fiscali e gestione amministrativa.");
        consulenza.setNumero("3202345");
        consulenza.setIndirizzo(indirizzo);
        consulenza.setProprietario(figuraSpecializzata);
        consulenza.setMaxCandidature(4);
        consulenza.setDisponibilita(true);
        consulenza.setTitolo("Consulenza legale per rifugiati");
        consulenza.setTipologia(true);
        consulenza.setId(1L);

        assertThrows(Exception.class, () -> {
            this.gestioneAnnuncioServiceImpl.inserimentoConsulenza(consulenza);
        });
    }

    /**
     * Test per l'inserimento di una consulenza
     * con il campo via vuoto.
     */
    @Test
    public void testInserimentoConsulenzaVia_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Roma",
                12,
                "11118",
                "RM",
                "A"
        );

        indirizzo.setId(1L);

        Consulenza consulenza = new Consulenza();
        consulenza.setTipo(TipoConsulenza.LEGALE);
        consulenza.setOrariDisponibili("Lunedi 12:30 - 14:30");
        consulenza.setDescrizione("Consulenza per documenti, permessi " +
                "di soggiorno, pratiche fiscali e gestione amministrativa.");
        consulenza.setNumero("3202347075");
        consulenza.setIndirizzo(indirizzo);
        consulenza.setProprietario(figuraSpecializzata);
        consulenza.setMaxCandidature(4);
        consulenza.setDisponibilita(true);
        consulenza.setTitolo("Consulenza legale per rifugiati");
        consulenza.setTipologia(true);
        consulenza.setId(1L);

        assertThrows(Exception.class, () -> {
            this.gestioneAnnuncioServiceImpl.inserimentoConsulenza(consulenza);
        });
    }

    /**
     * Test per l'inserimento di una consulenza
     * con il campo numero civico vuoto.
     */
    @Test
    public void testInserimentoConsulenzaNumeroCivico_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Roma (RM)",
                24908,
                "11118",
                "RM",
                "Via Roma"
        );

        indirizzo.setId(1L);

        Consulenza consulenza = new Consulenza();
        consulenza.setTipo(TipoConsulenza.LEGALE);
        consulenza.setOrariDisponibili("Lunedi 12:30 - 14:30");
        consulenza.setDescrizione("Consulenza per documenti, permessi " +
                "di soggiorno, pratiche fiscali e gestione amministrativa.");
        consulenza.setNumero("3202347075");
        consulenza.setIndirizzo(indirizzo);
        consulenza.setProprietario(figuraSpecializzata);
        consulenza.setMaxCandidature(4);
        consulenza.setDisponibilita(true);
        consulenza.setTitolo("Consulenza legale per rifugiati");
        consulenza.setTipologia(true);
        consulenza.setId(1L);

        assertThrows(Exception.class, () -> {
            this.gestioneAnnuncioServiceImpl.inserimentoConsulenza(consulenza);
        });
    }

    /**
     * Test per l'inserimento di una consulenza
     * con il campo città vuoto.
     */
    @Test
    public void testInserimentoConsulenzaCitta_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Roma (RM)",
                24,
                "11118",
                "RM",
                "Via Roma"
        );

        indirizzo.setId(1L);

        Consulenza consulenza = new Consulenza();
        consulenza.setTipo(TipoConsulenza.LEGALE);
        consulenza.setOrariDisponibili("Lunedi 12:30 - 14:30");
        consulenza.setDescrizione("Consulenza per documenti, permessi " +
                "di soggiorno, pratiche fiscali e gestione amministrativa.");
        consulenza.setNumero("3202347075");
        consulenza.setIndirizzo(indirizzo);
        consulenza.setProprietario(figuraSpecializzata);
        consulenza.setMaxCandidature(4);
        consulenza.setDisponibilita(true);
        consulenza.setTitolo("Consulenza legale per rifugiati");
        consulenza.setTipologia(true);
        consulenza.setId(1L);

        assertThrows(Exception.class, () -> {
            this.gestioneAnnuncioServiceImpl.inserimentoConsulenza(consulenza);
        });
    }

    /**
     * Test per l'inserimento di una consulenza
     * con il campo provincia vuoto.
     */
    @Test
    public void testInserimentoConsulenzaProvincia_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Roma",
                24,
                "11118",
                "R",
                "Via Roma"
        );

        indirizzo.setId(1L);

        Consulenza consulenza = new Consulenza();
        consulenza.setTipo(TipoConsulenza.LEGALE);
        consulenza.setOrariDisponibili("Lunedi 12:30 - 14:30");
        consulenza.setDescrizione("Consulenza per documenti, permessi " +
                "di soggiorno, pratiche fiscali e gestione amministrativa.");
        consulenza.setNumero("3202347075");
        consulenza.setIndirizzo(indirizzo);
        consulenza.setProprietario(figuraSpecializzata);
        consulenza.setMaxCandidature(4);
        consulenza.setDisponibilita(true);
        consulenza.setTitolo("Consulenza legale per rifugiati");
        consulenza.setTipologia(true);
        consulenza.setId(1L);

        assertThrows(Exception.class, () -> {
            this.gestioneAnnuncioServiceImpl.inserimentoConsulenza(consulenza);
        });
    }

    /**
     * Test per l'inserimento di una consulenza
     * con il campo CAP vuoto.
     */
    @Test
    public void testInserimentoConsulenzaCAP_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Roma",
                24,
                "0",
                "RM",
                "Via Roma"
        );

        indirizzo.setId(1L);

        Consulenza consulenza = new Consulenza();
        consulenza.setTipo(TipoConsulenza.LEGALE);
        consulenza.setOrariDisponibili("Lunedi 12:30 - 14:30");
        consulenza.setDescrizione("Consulenza per documenti, permessi " +
                "di soggiorno, pratiche fiscali e gestione amministrativa.");
        consulenza.setNumero("3202347075");
        consulenza.setIndirizzo(indirizzo);
        consulenza.setProprietario(figuraSpecializzata);
        consulenza.setMaxCandidature(4);
        consulenza.setDisponibilita(true);
        consulenza.setTitolo("Consulenza legale per rifugiati");
        consulenza.setTipologia(true);
        consulenza.setId(1L);

        assertThrows(Exception.class, () -> {
            this.gestioneAnnuncioServiceImpl.inserimentoConsulenza(consulenza);
        });
    }

    /**
     * Test per l'inserimento di una consulenza
     * con il campo max Candidatura vuoto.
     */
    @Test
    public void testInserimentoConsulenzaMaxCandidatura_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Roma",
                24,
                "11118",
                "RM",
                "Via Roma"
        );

        indirizzo.setId(1L);

        Consulenza consulenza = new Consulenza();
        consulenza.setTipo(TipoConsulenza.LEGALE);
        consulenza.setOrariDisponibili("Lunedi 12:30 - 14:30");
        consulenza.setDescrizione("Consulenza per documenti, permessi " +
                "di soggiorno, pratiche fiscali e gestione amministrativa.");
        consulenza.setNumero("3202347075");
        consulenza.setIndirizzo(indirizzo);
        consulenza.setProprietario(figuraSpecializzata);
        consulenza.setMaxCandidature(500);
        consulenza.setDisponibilita(true);
        consulenza.setTitolo("Consulenza legale per rifugiati");
        consulenza.setTipologia(true);
        consulenza.setId(1L);

        assertThrows(Exception.class, () -> {
            this.gestioneAnnuncioServiceImpl.inserimentoConsulenza(consulenza);
        });
    }

    /**
     * Test per l'inserimento di una consulenza
     * con il campo proprietario errato.
     */
    @Test
    public void testInserimentoConsulenzaProprietario_Errato() {
        Indirizzo indirizzo = new Indirizzo(
                "Roma",
                24,
                "11118",
                "RM",
                "Via Roma"
        );

        indirizzo.setId(1L);

        figuraSpecializzata.setEmail("email.errata@gmail.com");

        Consulenza consulenza = new Consulenza();
        consulenza.setTipo(TipoConsulenza.LEGALE);
        consulenza.setOrariDisponibili("Lunedi 12:30 - 14:30");
        consulenza.setDescrizione("Consulenza per documenti, permessi " +
                "di soggiorno, pratiche fiscali e gestione amministrativa.");
        consulenza.setNumero("3202347075");
        consulenza.setIndirizzo(indirizzo);
        consulenza.setProprietario(figuraSpecializzata);
        consulenza.setMaxCandidature(4);
        consulenza.setDisponibilita(true);
        consulenza.setTitolo("Consulenza legale per rifugiati");
        consulenza.setTipologia(true);
        consulenza.setId(1L);

        assertThrows(Exception.class, () -> {
            this.gestioneAnnuncioServiceImpl.inserimentoConsulenza(consulenza);
        });
    }

    /**
     * Test per l'inserimento di una consulenza corretto.
     */
    @Test
    public void testInserimentoConsulenza_Corretto() {
        Indirizzo indirizzo = new Indirizzo(
                "Roma",
                24,
                "11118",
                "RM",
                "Via Roma"
        );

        indirizzo.setId(1L);

        Consulenza consulenza = new Consulenza();
        consulenza.setTipo(TipoConsulenza.LEGALE);
        consulenza.setOrariDisponibili("Lunedi 12:30 - 14:30");
        consulenza.setDescrizione("Consulenza per documenti, permessi " +
                "di soggiorno, pratiche fiscali e gestione amministrativa.");
        consulenza.setNumero("3202347075");
        consulenza.setIndirizzo(indirizzo);
        consulenza.setProprietario(figuraSpecializzata);
        consulenza.setMaxCandidature(4);
        consulenza.setDisponibilita(true);
        consulenza.setTitolo("Consulenza legale per rifugiati");
        consulenza.setTipologia(true);
        consulenza.setId(1L);

        assertEquals(consulenza, this.gestioneAnnuncioServiceImpl.inserimentoConsulenza(consulenza));
    }
}
