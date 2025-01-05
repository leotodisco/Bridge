package com.project.bridgebackend.GestioneAlloggio.service;

import com.project.bridgebackend.CDN.CDNService;
import com.project.bridgebackend.GestioneAlloggio.Service.AlloggioServiceImplementazione;
import com.project.bridgebackend.Model.Entity.Alloggio;
import com.project.bridgebackend.Model.Entity.Indirizzo;
import com.project.bridgebackend.Model.Entity.Volontario;
import com.project.bridgebackend.Model.Entity.enumeration.Gender;
import com.project.bridgebackend.Model.Entity.enumeration.Ruolo;
import com.project.bridgebackend.Model.Entity.enumeration.Servizi;
import com.project.bridgebackend.Model.Entity.enumeration.TitoloDiStudio;
import com.project.bridgebackend.Model.dao.AlloggioDAO;
import com.project.bridgebackend.Model.dao.IndirizzoDAO;
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
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Mock
    private Volontario volontario;
    @InjectMocks
    private AlloggioServiceImplementazione alloggioServiceImplementazione;
    @InjectMocks
    private CDNService  cdnService;

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
        alloggio.setTitolo("Casa Bellissima");
        alloggio.setListaCandidati(null);
        List<String> foto = new ArrayList<>();
        String fotoBase64 = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEASABIAAD/4Qm0RXhpZgAATU0AKgAAAAgACgEPAAIAAAAGAA";
        foto.add(fotoBase64);
        alloggio.setFoto(foto);
        alloggio.setAssegnatoA(null);
        alloggio.setProprietario(volontario);
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
        alloggio.setTitolo("87-ciao");
        alloggio.setServizi(WIFI);
        alloggio.setListaCandidati(null);
        List<String> foto = new ArrayList<>();
        String fotoBase64 = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEASABIAAD/4Qm0RXhpZgAATU0AKgAAAAgACgEPAAIAAAAGAA";
        foto.add(fotoBase64);
        alloggio.setFoto(foto);
        alloggio.setAssegnatoA(null);
        alloggio.setProprietario(volontario);
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
        List<String> foto = new ArrayList<>();
        String fotoBase64 = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEASABIAAD/4Qm0RXhpZgAATU0AKgAAAAgACgEPAAIAAAAGAA";
        foto.add(fotoBase64);
        alloggio.setFoto(foto);
        alloggio.setAssegnatoA(null);
        alloggio.setProprietario(proprietarioErrato);
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
