package com.project.bridgebackend.GestioneAlloggio.service;

import com.project.bridgebackend.GestioneAlloggio.Service.AlloggioServiceImplementazione;
import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Consulenza;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.Servizi;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import com.project.bridgebackend.Model.dao.AlloggioDAO;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
import com.project.bridgebackend.Model.dao.VolontarioDAO;
import com.project.bridgebackend.Service.BridgeBackendApplicationTests;
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
import java.util.List;

import static com.project.bridgebackend.Model.Entity.enumeration.Servizi.WIFI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Antonio Ceruso.
 * Creato il: 02/02/2025.
 * Classe di test per la classe GestioneAlloggio Service.
 */

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = BridgeBackendApplicationTests.class)
public class GestioneAlloggioServiceImplTest {
    @Mock
    private AlloggioDAO alloggioDAO;
    @Mock
    private VolontarioDAO volontarioDAO;
    @Mock
    private IndirizzoDAO indirizzoDAO;
    @Mock
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Mock
    private Volontario volontario;
    @InjectMocks
    private Alloggio alloggio;
    @InjectMocks
    private AlloggioServiceImplementazione alloggioServiceImplementazione;

    @AfterEach
    void tearDown() {
        // Resetta tutti i mock di Mockito per evitare interferenze tra i test
        Mockito.reset(alloggioDAO, volontarioDAO,indirizzoDAO);
    }

    /**
     * Metodo che viene eseguito prima di tutti gli altri metodi.
     * Si usa per istanziare le risorse necessarie.
     */
    @SneakyThrows
    @BeforeEach
    public void setup() {
        volontario = new Volontario(
                        "montecorvino.rovella@gmail.com",
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
                        passwordEncoder.encode("Passroad69!"));

        // Configura il comportamento predefinito del mock
        Mockito.lenient().when(volontarioDAO.findByEmail("montecorvino.rovella@gmail.com")).thenReturn(volontario);

        // Simula il caso di un'email non trovata
        Mockito.lenient().when(volontarioDAO.findByEmail("email.sbagliata@gmail.com")).thenReturn(null);
    }

    /**
     * Test del metodo "addAlloggio" quando si inserisce
     * una via non valida per l'indirizzo.
     */
    @Test
    public void testInserimentoAlloggioViaIndirizzo_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
              "Montecorvino rovella",
              101,
                "84096",
              "SA",
              "c"
        );
        indirizzo.setId(1L);


        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(1);
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setDescrizione("");
        alloggio.setFoto(null);
        alloggio.setAssegnatoA(null);
        alloggio.setServizi(null);
        alloggio.setMaxPersone(1);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    /**
     * Test del metodo "addAlloggio" quando si inserisce
     * un numero civico non valido per l'indirizzo.
     */
    @Test
    public void testInserimentoAlloggioNCivicoIndirizzo_FormatoErrato() {

        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino rovella",
                27000,
                "84096",
                "SA",
                "Via Garibaldi"
        );
        indirizzo.setId(1L);

        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(1);
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setFoto(null);
        alloggio.setDescrizione("");
        alloggio.setAssegnatoA(null);
        alloggio.setServizi(null);
        alloggio.setMaxPersone(1);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    /**
     * Test del metodo "addAlloggio" quando si inserisce
     * una città non valida per l'indirizzo.
     */
    @Test
    public void testInserimentoAlloggioCittaIndirizzo_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "MontecorvinoRovella1000",
                101,
                "84096",
                "",
                "Via Garibaldi"
        );
        indirizzo.setId(1L);

        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(1);
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setDescrizione("");
        alloggio.setFoto(null);
        alloggio.setAssegnatoA(null);
        alloggio.setServizi(null);
        alloggio.setMaxPersone(1);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    /**
     * Test del metodo "addAlloggio" quando si inserisce
     * una provincia non valida per l'indirizzo.
     */
    @Test
    public void testInserimentoAlloggioProvinciaIndirizzo_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                "84096",
                "CAP-",
                "Via Garibaldi"
        );
        indirizzo.setId(1L);

        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(1);
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setFoto(null);
        alloggio.setDescrizione("");
        alloggio.setAssegnatoA(null);
        alloggio.setServizi(null);
        alloggio.setMaxPersone(1);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    /**
     * Test del metodo "addAlloggio" quando si inserisce
     * un CAP non valido per l'indirizzo.
     */
    @Test
    public void testInserimentoAlloggioCAPIndirizzo_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                "840960",
                "SA",
                "Via Garibaldi"
        );

        indirizzo.setId(1L);
        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(1);
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setFoto(null);
        alloggio.setDescrizione("");
        alloggio.setAssegnatoA(null);
        alloggio.setServizi(null);
        alloggio.setMaxPersone(1);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    /**
     * Test del metodo "addAlloggio" quando si inserisce
     * una metratura non valida per l'alloggio.
     */
    @Test
    public void testInserimentoAlloggioMetratura_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                "84096",
                "SA",
                "Via Garibaldi"
        );

        indirizzo.setId(1L);
        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(10000000);
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setFoto(null);
        alloggio.setDescrizione("");
        alloggio.setAssegnatoA(null);
        alloggio.setServizi(null);
        alloggio.setMaxPersone(1);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    /**
     * Test del metodo "addAlloggio" quando si inserisce
     * un numero massimo di persone non valido per l'alloggio.
     */
    @Test
    public void testInserimentoAlloggioMaxPersone_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                "84096",
                "SA",
                "Via Garibaldi"
        );

        indirizzo.setId(1L);
        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(100);
        alloggio.setMaxPersone(200);
        alloggio.setDescrizione("");
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setFoto(null);
        alloggio.setAssegnatoA(null);
        alloggio.setServizi(null);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    /**
     * Test del metodo "addAlloggio" quando si inserisce
     * una descrizione non valida per l'alloggio.
     */
    @Test
    public void testInserimentoAlloggioDescrizione_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                "84096",
                "SA",
                "Via Garibaldi"
        );

        indirizzo.setId(1L);
        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(100);
        alloggio.setMaxPersone(3);
        alloggio.setDescrizione("@@@");
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setFoto(null);
        alloggio.setAssegnatoA(null);
        alloggio.setServizi(null);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    /**
     * Test del metodo "addAlloggio" quando si inserisce
     * un servizio non valido per l'alloggio.
     */
    @Test
    public void testInserimentoAlloggioServizi_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                "84096",
                "SA",
                "Via Garibaldi"
        );

        indirizzo.setId(1L);
        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(100);
        alloggio.setMaxPersone(3);
        alloggio.setDescrizione("Casa nel centro Storico");
        alloggio.setTitolo("");
        alloggio.setListaCandidati(null);
        alloggio.setFoto(null);
        alloggio.setAssegnatoA(null);
        alloggio.setProprietario(null);
        alloggio.setId(1L);

         assertThrows(IllegalArgumentException.class, () -> {
            alloggio.setServizi(Servizi.valueOf("#####"));
        });
    }

    /**
     * Test del metodo "addAlloggio" quando si inserisce
     * un titolo non valido per l'alloggio.
     */
    @Test
    public void testInserimentoAlloggioTitolo_FormatoErrato() {
        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                "84096",
                "SA",
                "Via Garibaldi"
        );

        indirizzo.setId(1L);
        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(100);
        alloggio.setMaxPersone(3);
        alloggio.setDescrizione("Casa nel centro Storico");
        alloggio.setTitolo("87-Ciao");
        alloggio.setServizi(WIFI);
        alloggio.setListaCandidati(null);
        alloggio.setFoto(null);
        alloggio.setAssegnatoA(null);
        alloggio.setProprietario(null);
        alloggio.setId(1L);


        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    /**
     * Test del metodo "addAlloggio" quando un proprietario
     * che non esiste nel db cerca di creare un alloggio.
     */
    @Test
    public void testInserimentoAlloggioProprietario_FormatoErrato() {

        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                "84096",
                "SA",
                "Via Garibaldi"
        );
        indirizzo.setId(1L);

        Volontario proprietarioErrato = new Volontario(
                "email.sbagliata@gmail.com", // Email non valida
                "Giovanni",
                "Rossi",
                "Inglese",
                "id-unico",
                "Descrizione",
                LocalDate.of(1985, 5, 20),
                TitoloDiStudio.Laurea,
                Ruolo.Volontario,
                Gender.Maschio,
                "Italia",
                passwordEncoder.encode("123Password!")
        );
        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(100);
        alloggio.setMaxPersone(3);
        alloggio.setDescrizione("Casa nel centro Storico");
        alloggio.setTitolo("Casa Bellissima");
        alloggio.setServizi(WIFI);
        alloggio.setListaCandidati(null);
        alloggio.setFoto(null);
        alloggio.setAssegnatoA(null);
        alloggio.setProprietario(proprietarioErrato);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    /**
     * Test del metodo "addAlloggio" quando si inserisce
     * un immagine non valida per l'alloggio
     */
    @Test
    public void testInserimentoAlloggioFoto_FormatoErrato() {

        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                "84096",
                "SA",
                "Via Garibaldi"
        );
        indirizzo.setId(1L);

        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(100);
        alloggio.setMaxPersone(3);
        alloggio.setDescrizione("Casa nel centro Storico");
        alloggio.setTitolo("Casa Bellissima");
        alloggio.setServizi(WIFI);
        alloggio.setListaCandidati(null);
        List<String> foto = new ArrayList<>();
        foto.add("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA");
        alloggio.setFoto(foto);
        alloggio.setAssegnatoA(null);
        alloggio.setProprietario(volontario);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    /**
     * Test del metodo "addAlloggio" quando si inserisce
     * un immagine con un dimensione troppo grande.
     */
    @Test
    public void testInserimentoAlloggioFoto_DimensioneErrata() {

        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                "84096",
                "SA",
                "Via Garibaldi"
        );
        indirizzo.setId(1L);

        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(100);
        alloggio.setMaxPersone(3);
        alloggio.setDescrizione("Casa nel centro Storico");
        alloggio.setTitolo("Casa Bellissima");
        alloggio.setServizi(WIFI);
        alloggio.setListaCandidati(null);
        List<String> foto = new ArrayList<>();
        String fotoBase64 = "data:image/jpeg;base64,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dXQ9IiIKICAgICAgICAgICAgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIgogICAgICAgICAgICB4bWxuczptd2ctcnM9Imh0dHA6Ly93d3cubWV0YWRhdGF3b3JraW5nZ3JvdXAuY29tL3NjaGVtYXMvcmVnaW9ucy8iCiAgICAgICAgICAgIHhtbG5zOnN0QXJlYT0iaHR0cDovL25zLmFkb2JlLmNvbS94bXAvc1R5cGUvQXJlYSMiCiAgICAgICAgICAgIHhtbG5zOnN0RGltPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvRGltZW5zaW9ucyMiCiAgICAgICAgICAgIHhtbG5zOnBob3Rvc2hvcD0iaHR0cDovL25zLmFkb2JlLmNvbS9waG90b3Nob3AvMS4wLyI+CiAgICAgICAgIDx4bXA6Q3JlYXRlRGF0ZT4yMDI0LTEyLTIxVDE2OjIzOjA1PC94bXA6Q3JlYXRlRGF0ZT4KICAgICAgICAgPHhtcDpDcmVhdG9yVG9vbD4xOC4wPC94bXA6Q3JlYXRvclRvb2w+CiAgICAgICAgIDx4bXA6TW9kaWZ5RGF0ZT4yMDI0LTEyLTIxVDE2OjIzOjA1PC94bXA6TW9kaWZ5RGF0ZT4KICAgICAgICAgPG13Zy1yczpSZWdpb25zIHJkZjpwYXJzZVR5cGU9IlJlc291cmNlIj4KICAgICAgICAgICAgPG13Zy1yczpSZWdpb25MaXN0PgogICAgICAgICAgICAgICA8cmRmOlNlcT4KICAgICAgICAgICAgICAgICAgPHJkZjpsaSByZGY6cGFyc2VUeXBlPSJSZXNvdXJjZSI+CiAgICAgICAgICAgICAgICAgICAgIDxtd2ctcnM6QXJlYSByZGY6cGFyc2VUeXBlPSJSZXNvdXJjZSI+CiAgICAgICAgICAgICAgICAgICAgICAgIDxzdEFyZWE6eT4wLjY1NzAwMDAwMDAwMDAwMDAzPC9zdEFyZWE6eT4KICAgICAgICAgICAgICAgICAgICAgICAgPHN0QXJlYTp3PjAuMTE4MzgwOTUyMzgwOTUyNDI8L3N0QXJlYTp3PgogICAgICAgICAgICAgICAgICAgICAgICA8c3RBcmVhOng+MC4z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";
        foto.add(fotoBase64);
        foto.add(fotoBase64);
        foto.add(fotoBase64);
        alloggio.setFoto(foto);
        alloggio.setAssegnatoA(null);
        alloggio.setProprietario(volontario);
        alloggio.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {this.alloggioServiceImplementazione.addAlloggio(alloggio);});
    }

    @Test
    public void testInserimentoAlloggio_Corretto() {

        Indirizzo indirizzo = new Indirizzo(
                "Montecorvino Rovella",
                101,
                "84096",
                "SA",
                "Via Garibaldi"
        );
        indirizzo.setId(1L);

        Alloggio alloggio = new Alloggio();
        alloggio.setIndirizzo(indirizzo);
        alloggio.setMetratura(100);
        alloggio.setMaxPersone(3);
        alloggio.setDescrizione("Casa nel centro Storico");
        alloggio.setTitolo("Casa Bellissima");
        alloggio.setServizi(WIFI);
        alloggio.setListaCandidati(null);
        List<String> foto = new ArrayList<>();
        String fotoBase64 = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEASABIAAD/4Qm0RXhpZgAATU0AKgAAAAgACgEPAAIAAAAGAA";
        foto.add(fotoBase64);
        alloggio.setFoto(foto);
        alloggio.setAssegnatoA(null);
        alloggio.setProprietario(volontario);
        alloggio.setId(1L);

        // Configura il comportamento predefinito del mock per il salvataggio di una consulenza
        Mockito.when(alloggioDAO.save(Mockito.any(Alloggio.class))).thenAnswer(
                invocazione -> {
                    return invocazione.getArgument(0);
                }
        );
        assertEquals(alloggio,this.alloggioServiceImplementazione.addAlloggio(alloggio));
    }
}
